package kr.ac.kopo.gnuyog.bookmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kopo.gnuyog.bookmarket.domain.*;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import kr.ac.kopo.gnuyog.bookmarket.service.CartService;
import kr.ac.kopo.gnuyog.bookmarket.service.OrderProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/order")
public class OrderController
{
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderProService orderProService;

    @Autowired
    private BookService bookService;

    List<Book> listOfBooks; // 주문할 도서들을 담아 두는 리스트입니다. 주문 확인 화면에서 책 제목 등을 보여 줄 때 씁니다.
    Order order; // 지금 진행 중인 주문 객체입니다. 여러 단계(고객정보 → 배송정보 → 확인)를 거치는 동안 이 필드에 계속 내용을 채워 나갑니다.

    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value = "cartId") String cartId, Model model)
    {
        Cart cart = cartService.read(cartId);
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

        order.setCustomer(new Customer());
        order.setShipping(new Shipping());
        order.setGrandTotal(cart.getGrandTotal());

        return "redirect:/order/orderCustomerInfo";
    }

    @GetMapping("/orderCustomerInfo")
    public String requestCustomerInfoForm(Model model)
    {
        model.addAttribute("customer", order.getCustomer());
        return "orderCustomerInfo";
    }

    @PostMapping("/orderCustomerInfo")
    public String requestCustomerInfo(@ModelAttribute Customer customer)
    {
        order.setCustomer(customer);
        return "redirect:/order/orderShippingInfo";
    }

    @GetMapping("/orderShippingInfo")
    public String requestShippingInfoForm(Model model)
    {
        model.addAttribute("shipping", order.getShipping());
        return "orderShippingInfo";
    }

    @PostMapping("/orderShippingInfo")
    public String requestShippingInfo(@Valid @ModelAttribute Shipping shipping, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            return "orderShippingInfo";
        }
        order.setShipping(shipping);
        model.addAttribute("order", order);
        return "redirect:/order/orderConfirmation";
    }

    @GetMapping("/orderConfirmation")
    public String requestConfirmation(Model model){
        model.addAttribute("bookList", listOfBooks);
        model.addAttribute("order", order);
        return "orderConfirmation";
    }
    // [추가] [주문완료] 버튼(POST)을 받아서 주문 완료 화면으로 보낸다
    //        이 메서드가 없어서 405 오류(화이트라벨)가 났다
    @PostMapping("/orderConfirmation")
    public String requestConfirmationFinished(Model model)
    {
        model.addAttribute("order", order);
        orderProService.save(order);
        return "redirect:/order/orderFinished";
    }

    @GetMapping("/orderFinished")
    public String requestOrderFinished(HttpServletRequest request, Model model)
    {
        // [수정] 저장은 POST /orderConfirmation 에서 이미 했으므로 여기서는 저장하지 않는다
        //        (여기서도 저장하면 이중 저장되고, 새로고침할 때마다 또 저장된다)
        model.addAttribute("order", order);
        HttpSession session = request.getSession();

        if (session != null)
        {
            session.invalidate();
        }
        return "orderFinished";
    }

    @GetMapping("/orderCancelled") // GET /order/orderCancelled를 받습니다(각 화면의 [취소] 버튼)
    public String requestCancelled(HttpServletRequest request) // 완료 때처럼 세션을 비웁니다. 저장은 하지 않습니다.
    {
        HttpSession session = request.getSession();

        if(session != null)
        {
            session.invalidate();
        }
        return "orderCancelled";
    }
    @GetMapping("/list")
    public String viewHomePage(Model model)
    {
        return viewPage(1, "orderId", "asc", model);
    }
    @GetMapping("/page")
    public String viewPage(@RequestParam("pageNum") int pageNum, @RequestParam("sortField") String sortField,
                           @RequestParam("sortDir") String sortDir, Model model)
    {
        Page<Order> page = orderProService.listAll(pageNum, sortField, sortDir);
        List<Order> orderList = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField); // 현재 정렬 필드
        model.addAttribute("sortDir", sortDir); // 현재 정렬 방향
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("orderList", orderList);

        return "orderList";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewOrder(@PathVariable(name = "id") Long id)
    {
        Order order = orderProService.get(id); // DB에서 해당 ID의 주문을 가져옵니다.
        // 여기서 order는 메서드 안 지역변수라서 35줄의 필드 order와는 다른 변수입니다(이름만 같음).
        List<Book> listOfBooks = new ArrayList<>(); // 이 주문의 도서들을 담을 새 리스트입니다(역시 지역변수).

        for (OrderItem item : order.getOrderItems().values()) // 주문 항목들을 하나씩 꺼냅니다.
        {
            String bookId = item.getBookId(); // 주문 항목에는 도서 ID만 저장되어 있으므로 ID를 꺼냅니다.
            Book book = bookService.getBookById(bookId); // 그 ID로 도서 전체 정보(제목, 가격 등)를 조회합니다.
            listOfBooks.add(book); // 리스트에 추가합니다.
        }

        ModelAndView modelAndView = new ModelAndView("orderView");
        modelAndView.addObject("order", order);
        modelAndView.addObject("bookList", listOfBooks);
        return modelAndView; // 스프링이 orderView.html에 데이터를 넣어 보여 줍니다.
    }
}