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
    public @ResponseBody Cart create(@RequestBody Cart cart){
        return cartService.create(cart);
    }

    @GetMapping("/{cartId}")
    public String requestCartList(@PathVariable(value = "cartId")String cartId, Model model){
        Cart cart = cartService.read(cartId);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PutMapping("/{cartId}")
    public @ResponseBody Cart read(@PathVariable(value = "cartId") String cartId){
        return cartService.read(cartId);
    }

    @PutMapping("/book/{bookId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addCartByNewItem(@PathVariable("bookId") String bookId, HttpServletRequest request){
        String sessionId = request.getSession(true).getId();

        Cart cart = cartService.read(sessionId);

        if (cart == null)
            cart = cartService.create(new Cart(sessionId));

        Book book = bookService.getBookById(bookId);

        if (book == null)
            throw new IllegalArgumentException(new BookIdException(bookId));

        cart.addCartItem(new CartItem(book));

        cartService.update(sessionId, cart);
    }

    @DeleteMapping("/book/{bookId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)//생략(기본):정상적 실행에 대한 응답(200), 정상+반환값없다: 204
    public void removeCartByItem(@PathVariable("bookId") String bookId, HttpServletRequest request){
        String sessionId = request.getSession(true).getId();

        Cart cart = cartService.read(sessionId);

        if (cart == null)
            cart = cartService.create(new Cart(sessionId));

        Book book = bookService.getBookById(bookId);

        if (book == null)
            throw new IllegalArgumentException(new BookIdException(bookId));

        cart.removeCartItem(new CartItem(book));

        cartService.update(sessionId, cart);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCartList(@PathVariable("cartId") String cartId){
        cartService.delete(cartId);
    }
}