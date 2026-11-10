package kr.ac.kopo.gnuyog.bookmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.domain.Order;
import kr.ac.kopo.gnuyog.bookmarket.domain.*;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import kr.ac.kopo.gnuyog.bookmarket.service.CartService;
import kr.ac.kopo.gnuyog.bookmarket.service.OrderProService;
import kr.ac.kopo.gnuyog.bookmarket.service.OrderService;
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
public class OrderController {
    // OrderService : 주문한 수량만큼 도서 재고를 확인하고 줄이는 confirmOrder() 를 쓰기 위해 필요
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderProService orderProService;

    @Autowired
    private BookService bookService;

    List<Book> listOfBooks;
    Order order;

    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value = "cartId") String cartId, Model model){
        Cart cart = cartService.read(cartId);
        order = new Order();
        listOfBooks = new ArrayList<Book>();

        for (CartItem item : cart.getCartItems().values()){
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
    public String requestCustomerInfoForm(Model model){
        model.addAttribute("customer", order.getCustomer());
        return "orderCustomerInfo";
    }

    @PostMapping("/orderCustomerInfo")
    public String requestCustomerInfo(@ModelAttribute Customer customer){
        order.setCustomer(customer);
        return "redirect:/order/orderShippingInfo";
    }

    @GetMapping("/orderShippingInfo")
    public String requestShippingInfoForm(Model model){
        model.addAttribute("shipping", order.getShipping());
        return "orderShippingInfo";
    }

    @PostMapping("/orderShippingInfo")
    public String requestShippingInfo(@Valid @ModelAttribute Shipping shipping, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors())
            return "orderShippingInfo";
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

    @PostMapping("/orderConfirmation")
    public String requestConfirmationFinished(Model model){
        // [추가] 주문에 담긴 책마다 재고를 확인하고 DB 재고를 줄인다
        //        (재고가 모자라면 confirmOrder() 가 예외를 던져서 아래 저장까지 가지 않는다)
        for (OrderItem item : order.getOrderItems().values()){
            orderService.confirmOrder(item.getBookId(), item.getQuantity());
        }

        model.addAttribute("order", order);
        orderProService.save(order);
        return "redirect:/order/orderFinished";
    }

    @GetMapping("/orderFinished")
    public String requestOrderFinished(HttpServletRequest request, Model model){
        // [수정] orderService.saveOrder(order); 삭제
        //        재고 감소와 저장은 POST /orderConfirmation 에서 이미 했다
        //        여기서 또 하면 이중 저장되고, 새로고침할 때마다 재고가 또 줄어든다
        model.addAttribute("order", order);
        HttpSession session = request.getSession();

        if (session != null)
            session.invalidate();

        return "orderFinished";
    }

    @GetMapping("/orderCancelled")
    public String requestCancelled(HttpServletRequest request){
        HttpSession session = request.getSession();

        if(session != null)
            session.invalidate();

        return "orderCancelled";
    }

    @GetMapping("/list")
    public String viewHomePage(Model model){
        return viewPage(1, "orderId", "asc", model);
    }

    @GetMapping("/page")
    public String viewPage(@RequestParam("pageNum") int pageNum, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model){
        Page<Order> page = orderProService.listAll(pageNum, sortField, sortDir);
        List<Order> orderList = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("orderList", orderList);
        return "orderList";
    }

    // [추가] 주문 목록 화면의 clearForm([삭제하기] 버튼)이 보낸 요청을 받는다
    //        폼은 POST 로 보내지만 hidden _method=delete 때문에 DELETE 요청으로 바뀌어 여기로 온다
    //        DB에 저장된 주문을 모두 지우고, 다시 주문 목록 화면으로 보낸다
    @DeleteMapping("/deleteAll")
    public String deleteAllOrders(){
        orderProService.deleteAll();
        return "redirect:/order/list";
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewOrder(@PathVariable(name="id")Long id){
        Order order = orderProService.get(id);
        List<Book> listOfBooks = new ArrayList<Book>();
        for (OrderItem item : order.getOrderItems().values()){
            String bookId = item.getBookId();
            Book book = bookService.getBookById(bookId);
            listOfBooks.add(book);
        }
        ModelAndView modelAndView = new ModelAndView("orderView");
        modelAndView.addObject("order", order);
        modelAndView.addObject("bookList", listOfBooks);

        return modelAndView;
    }
}