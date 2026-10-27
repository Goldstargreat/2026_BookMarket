package kr.ac.kopo.gnuyog.bookmarket.controller;

import kr.ac.kopo.gnuyog.bookmarket.domain.*;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import kr.ac.kopo.gnuyog.bookmarket.service.CartService;
import kr.ac.kopo.gnuyog.bookmarket.service.OrderProService;
import kr.ac.kopo.gnuyog.bookmarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

// 주문 흐름
// 장바구니 [주문하기] → /order/{cartId}
//   → 고객 정보 입력 (/order/orderCustomerInfo)
//   → 배송 정보 입력 (/order/orderShippingInfo)
//   → 주문 확인     (/order/orderConfirmation)
//   → 주문 완료     (/order/orderFinished)
//   (어느 단계에서든 [취소] → /order/orderCancelled)
@Controller
@RequestMapping(value = "/order")
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderProService orderProService;   // JPA로 주문을 DB에 저장

    @Autowired
    private BookService bookService;

    List<Book> listOfBooks;   // 주문 확인 화면에 보여줄 도서 목록
    Order order;              // 단계별로 채워 나가는 주문 객체
    String cartId;            // [추가] 주문이 끝나면 비울 장바구니 번호

    // 1단계: 장바구니 내용으로 주문(Order)을 만든다
    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value = "cartId") String cartId)
    {
        Cart cart = cartService.read(cartId);

        // [추가] 장바구니가 없거나 비어 있으면 주문하지 않고 장바구니로 돌아간다
        if (cart == null || cart.getCartItems().isEmpty())
        {
            return "redirect:/cart/" + cartId;
        }

        this.cartId = cartId;
        order = new Order();
        listOfBooks = new ArrayList<Book>();

        for (CartItem item : cart.getCartItems().values())
        {
            OrderItem orderItem = new OrderItem();

            Book book = item.getBook();

            listOfBooks.add(book);

            orderItem.setBookId(book.getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());

            order.getOrderItems().put(book.getBookId(), orderItem);
        }

        // [수정] 화면의 address.country 같은 입력칸이 동작하려면 Address 객체가 미리 있어야 한다
        Customer customer = new Customer();
        customer.setAddress(new Address());
        order.setCustomer(customer);

        Shipping shipping = new Shipping();
        shipping.setAddress(new Address());
        order.setShipping(shipping);

        order.setGrandTotal(cart.getGrandTotal());

        return "redirect:/order/orderCustomerInfo";
    }

    // 2단계: 고객 정보 입력 화면 보여주기
    @GetMapping("/orderCustomerInfo")
    public String requestCustomerInfoForm(Model model)
    {
        if (order == null)          // 주문을 시작하지 않고 주소로 바로 들어온 경우
        {
            return "redirect:/cart";
        }
        model.addAttribute("customer", order.getCustomer());
        return "orderCustomerInfo";
    }

    // 2단계: 고객 정보 입력 처리
    @PostMapping("/orderCustomerInfo")
    public String requestCustomerInfo(@ModelAttribute Customer customer)
    {
        if (order == null)
        {
            return "redirect:/cart";
        }
        order.setCustomer(customer);   // 입력받은 고객 정보를 주문에 저장
        return "redirect:/order/orderShippingInfo";
    }

    // 3단계: 배송 정보 입력 화면 보여주기
    @GetMapping("/orderShippingInfo")
    public String requestShippingInfoForm(Model model)
    {
        if (order == null)
        {
            return "redirect:/cart";
        }
        model.addAttribute("shipping", order.getShipping());
        return "orderShippingInfo";
    }

    // 3단계: 배송 정보 입력 처리
    @PostMapping("/orderShippingInfo")
    public String requestShippingInfo(@ModelAttribute Shipping shipping, BindingResult bindingResult)
    {
        if (order == null)
        {
            return "redirect:/cart";
        }
        // 배송일을 2026/11/01 형식이 아니게 입력하면 오류 → 입력 화면을 다시 보여준다
        if (bindingResult.hasErrors())
        {
            return "orderShippingInfo";
        }
        order.setShipping(shipping);   // 입력받은 배송 정보를 주문에 저장
        return "redirect:/order/orderConfirmation";
    }

    // 4단계: 주문 확인 화면 보여주기
    @GetMapping("/orderConfirmation")
    public String requestConfirmOrder(Model model)
    {
        if (order == null)
        {
            return "redirect:/cart";
        }
        model.addAttribute("order", order);
        model.addAttribute("bookList", listOfBooks);
        return "orderConfirmation";
    }

    // 4단계: [주문완료] 버튼 처리 → DB에 저장하고 장바구니를 비운다
    @PostMapping("/orderConfirmation")
    public String requestConfirmOrderFinished()
    {
        if (order == null)
        {
            return "redirect:/cart";
        }

        // Order를 저장하면 cascade 설정 때문에 고객·배송·주소·주문항목도 함께 저장된다
        // 저장이 끝나면 order.getOrderId()에 DB가 만든 주문번호가 들어 있다
        orderProService.save(order);

        // 주문이 끝났으니 장바구니 비우기
        if (cartService.read(cartId) != null)
        {
            cartService.delete(cartId);
        }

        return "redirect:/order/orderFinished";
    }

    // 5단계: 주문 완료 화면
    @GetMapping("/orderFinished")
    public String requestFinishOrder(Model model)
    {
        if (order == null)          // 완료 화면에서 새로고침한 경우
        {
            return "redirect:/books";
        }
        model.addAttribute("order", order);

        // 화면에 넘겼으니 다음 주문을 위해 비워 둔다
        order = null;
        listOfBooks = null;
        cartId = null;

        return "orderFinished";
    }

    // 주문 취소 (장바구니는 그대로 남는다)
    @GetMapping("/orderCancelled")
    public String requestCancelOrder()
    {
        order = null;
        listOfBooks = null;
        cartId = null;
        return "orderCancelled";
    }
}