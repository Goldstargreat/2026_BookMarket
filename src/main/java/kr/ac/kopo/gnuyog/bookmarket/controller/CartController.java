package kr.ac.kopo.gnuyog.bookmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import kr.ac.kopo.gnuyog.bookmarket.domain.CartItem;
import kr.ac.kopo.gnuyog.bookmarket.exception.BookIdException;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import kr.ac.kopo.gnuyog.bookmarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    @GetMapping
    public String requestCartId(HttpServletRequest request){
        String sessionId = request.getSession().getId();
        return "redirect:/cart/" + sessionId;
    }

    @PostMapping
    public @ResponseBody Cart create(@RequestBody Cart cart)
    {
        return cartService.create(cart);
    }

    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value = "cartId") String cartId, Model model)
    {
        Cart cart = cartService.read(cartId);
        model.addAttribute("cart", cart);
        model.addAttribute("cartId", cartId);
        return "cart";
    }

    @GetMapping("/{cartId}/json")
    public @ResponseBody Cart read(@PathVariable(value = "cartId") String cartId)
    {
        return cartService.read(cartId);
    }

    @PutMapping("/book/{bookId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addCartByNewItem(@PathVariable("bookId") String bookId, HttpServletRequest request)
    {
        String sessionId = request.getSession(true).getId();

        Cart cart = cartService.read(sessionId);

        if (cart == null)
            cart = cartService.create(new Cart(sessionId));

        // [수정] 없는 책이면 getBookById가 BookIdException을 던진다.
        //        → 아래 handleBookIdException이 404로 응답하므로 null 검사는 지웠다.
        Book book = bookService.getBookById(bookId);

        cart.addCartItem(new CartItem(book));

        cartService.update(sessionId, cart);
    }

    @DeleteMapping("/book/{bookId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCartByItem(@PathVariable("bookId") String bookId, HttpServletRequest request)
    {
        String sessionId = request.getSession(true).getId();

        Cart cart = cartService.read(sessionId);

        if (cart == null)
            cart = cartService.create(new Cart(sessionId));

        // [수정] 위와 같은 이유로 null 검사 제거
        Book book = bookService.getBookById(bookId);

        cart.removeCartItem(new CartItem(book));

        cartService.update(sessionId, cart);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCartList(@PathVariable("cartId") String cartId)
    {
        cartService.delete(cartId);
    }

    // [수정] 추가: 이 컨트롤러에서 BookIdException이 나면 화면 대신 404 상태 코드만 보낸다.
    //        controller.js의 response.ok가 false가 되어 "추가에 실패했습니다 (404)"가 뜬다.
    @ExceptionHandler(BookIdException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleBookIdException()
    {
    }
}