package kr.ac.kopo.gnuyog.bookmarket.controller;

import jakarta.servlet.http.HttpSession;
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

    // [수정] 컨트롤러는 모든 사용자가 같이 쓰는 객체 하나뿐이라서
    //        주문 정보를 필드에 두면 여러 사람의 주문이 섞인다.
    //        → 사용자마다 따로 있는 HttpSession에 저장한다.
    //        아래는 세션에 저장할 때 쓰는 이름(키)이다. 오타를 막으려고 상수로 만들었다.
    private static final String SESSION_ORDER = "order";             // 단계별로 채워 나가는 주문 객체
    private static final String SESSION_LIST_OF_BOOKS = "listOfBooks"; // 주문 확인 화면에 보여줄 도서 목록
    private static final String SESSION_CART_ID = "cartId";     // 주문이 끝나면 비울 장바구니 번호

    // 1단계: 장바구니 내용으로 주문(Order)을 만든다
    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value = "cartId") String cartId, HttpSession session)
    {
        Cart cart = cartService.read(cartId);

        // [추가] 장바구니가 없거나 비어 있으면 주문하지 않고 장바구니로 돌아간다
        if (cart == null || cart.getCartItems().isEmpty())
        {
            return "redirect:/cart/" + cartId;
        }

        Order order = new Order();
        List<Book> listOfBooks = new ArrayList<Book>();

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

        // [수정] 만든 주문 정보를 이 사용자의 세션에 저장한다
        session.setAttribute(SESSION_ORDER, order);
        session.setAttribute(SESSION_LIST_OF_BOOKS, listOfBooks);
        session.setAttribute(SESSION_CART_ID, cartId);

        return "redirect:/order/orderCustomerInfo";
    }

    // 2단계: 고객 정보 입력 화면 보여주기
    @GetMapping("/orderCustomerInfo")
    public String requestCustomerInfoForm(Model model, HttpSession session)
    {
        Order order = (Order) session.getAttribute(SESSION_ORDER);   // 세션에서 주문 꺼내기
        if (order == null)          // 주문을 시작하지 않고 주소로 바로 들어온 경우
        {
            return "redirect:/cart";
        }
        model.addAttribute("customer", order.getCustomer());
        return "orderCustomerInfo";
    }

    // 2단계: 고객 정보 입력 처리
    @PostMapping("/orderCustomerInfo")
    public String requestCustomerInfo(@ModelAttribute Customer customer, HttpSession session)
    {
        Order order = (Order) session.getAttribute(SESSION_ORDER);
        if (order == null)
        {
            return "redirect:/cart";
        }
        // 세션에 들어 있는 주문 객체를 직접 고치므로 다시 setAttribute 하지 않아도 된다
        order.setCustomer(customer);   // 입력받은 고객 정보를 주문에 저장
        return "redirect:/order/orderShippingInfo";
    }

    // 3단계: 배송 정보 입력 화면 보여주기
    @GetMapping("/orderShippingInfo")
    public String requestShippingInfoForm(Model model, HttpSession session)
    {
        Order order = (Order) session.getAttribute(SESSION_ORDER);
        if (order == null)
        {
            return "redirect:/cart";
        }
        model.addAttribute("shipping", order.getShipping());
        return "orderShippingInfo";
    }

    // 3단계: 배송 정보 입력 처리
    @PostMapping("/orderShippingInfo")
    public String requestShippingInfo(@ModelAttribute Shipping shipping, BindingResult bindingResult, HttpSession session)
    {
        Order order = (Order) session.getAttribute(SESSION_ORDER);
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
    public String requestConfirmOrder(Model model, HttpSession session)
    {
        Order order = (Order) session.getAttribute(SESSION_ORDER);
        if (order == null)
        {
            return "redirect:/cart";
        }
        model.addAttribute("order", order);
        model.addAttribute("bookList", session.getAttribute(SESSION_LIST_OF_BOOKS));
        return "orderConfirmation";
    }

    // 4단계: [주문완료] 버튼 처리 → DB에 저장하고 장바구니를 비운다
    @PostMapping("/orderConfirmation")
    public String requestConfirmOrderFinished(Model model, HttpSession session)
    {
        Order order = (Order) session.getAttribute(SESSION_ORDER);
        if (order == null)
        {
            return "redirect:/cart";
        }

        // [수정] orderProService.save(order) → orderService.saveOrder(order)
        //        saveOrder는 책마다 DB 재고를 줄인 다음 주문을 저장한다
        // Order를 저장하면 cascade 설정 때문에 고객·배송·주소·주문항목도 함께 저장된다
        // 저장이 끝나면 order.getOrderId()에 DB가 만든 주문번호가 들어 있다
        try
        {
            orderService.saveOrder(order);
        }
        catch (IllegalArgumentException e)
        {
            // 재고가 부족하면 주문 확인 화면을 다시 보여주고 오류 메시지를 띄운다
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("order", order);
            model.addAttribute("bookList", session.getAttribute(SESSION_LIST_OF_BOOKS));
            return "orderConfirmation";
        }

        // 주문이 끝났으니 장바구니 비우기
        String cartId = (String) session.getAttribute(SESSION_CART_ID);
        if (cartService.read(cartId) != null)
        {
            cartService.delete(cartId);
        }

        return "redirect:/order/orderFinished";
    }

    // 5단계: 주문 완료 화면
    @GetMapping("/orderFinished")
    public String requestFinishOrder(Model model, HttpSession session)
    {
        Order order = (Order) session.getAttribute(SESSION_ORDER);
        if (order == null)          // 완료 화면에서 새로고침한 경우
        {
            return "redirect:/books";
        }
        model.addAttribute("order", order);

        // 화면에 넘겼으니 다음 주문을 위해 세션에서 지운다
        clearOrderSession(session);

        return "orderFinished";
    }

    // 주문 취소 (장바구니는 그대로 남는다)
    @GetMapping("/orderCancelled")
    public String requestCancelOrder(HttpSession session)
    {
        clearOrderSession(session);
        return "orderCancelled";
    }

    // [추가] 세션에 저장해 둔 주문 정보 3가지를 모두 지운다
    private void clearOrderSession(HttpSession session)
    {
        session.removeAttribute(SESSION_ORDER);
        session.removeAttribute(SESSION_LIST_OF_BOOKS);
        session.removeAttribute(SESSION_CART_ID);
    }
}
