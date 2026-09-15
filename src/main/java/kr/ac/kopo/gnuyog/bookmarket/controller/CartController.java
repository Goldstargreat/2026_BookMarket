package kr.ac.kopo.gnuyog.bookmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import kr.ac.kopo.gnuyog.bookmarket.domain.CartItem;
import kr.ac.kopo.gnuyog.bookmarket.exception.BookIdException;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import kr.ac.kopo.gnuyog.bookmarket.service.CartService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/cart")
public class CartController
{
    @Autowired
    private CartService cartService;
    @Autowired
    private BookService bookService;

    @GetMapping
    public String requestCartId(HttpServletRequest request)
    {
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
        return "cart"; // cart.html 을 렌더링
    }

    @PutMapping("/{cartId}")
    public @ResponseBody Cart read(@PathVariable(value = "cartId") String cartId)
    {
        return cartService.read(cartId);
    }

    @PutMapping("/book/{bookId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addCartByNewItem(@PathVariable("bookId") String bookId,
                                 HttpServletRequest request)

    // 메서드 이름: addCartByNewItem (새 아이템을 장바구니에 추가)
    // @PathVariable("bookId") String bookId → 주소에 있던 {bookId} 값을 bookId라는 변수에 담아줘요.
    // HttpServletRequest request → 현재 이 요청을 보낸 사용자에 대한 정보(세션 등)를 담고 있는 객체예요.
    {
        String sessionId = request.getSession(true).getId();
        // **세션(session)**이란: 어떤 사용자가 사이트에 접속해 있는 동안 그 사람을 구별하기 위한 "출입증" 같은 거예요.
        // request.getSession(true) → 이 사용자의 세션을 가져오는데, 만약 세션이 없으면 새로 만들어라(true)는 뜻이에요.
        // .getId() → 그 세션의 고유 번호(문자열)를 가져와요. 이걸로 "이 장바구니가 누구 것인지" 구별해요.
        Cart cart = cartService.read(sessionId);
        if (cart == null)
        {
            cart = cartService.create(new Cart(sessionId));
        }

        Book book = bookService.getBookById(bookId);
        // 아까 주소에서 받은 bookId를 이용해서, 실제 책 정보(제목, 가격 등)를 데이터베이스에서 찾아와요.
        if (book == null)
        {
            throw new IllegalArgumentException(new BookIdException(bookId));
        }
        // 만약 그 bookId에 해당하는 책이 존재하지 않으면(잘못된 요청이면), 에러를 발생시켜서 멈춰요.
        // "이런 책 없어요!"라고 알려주는 거예요.

        cart.addCartItem(new CartItem(book));
        // 찾아온 책 정보로 CartItem(장바구니에 담을 아이템)을 새로 만들고,
        // 그걸 장바구니(cart)에 추가해요.

        // 방금 새로 만든 장바구니라면 create()로 저장소에 처음 등록하고,
        // 이미 있던 장바구니라면 update()로 기존 내용을 갱신합니다.
        // (CartRepositoryImpl은 create=신규만 허용, update=기존 것만 허용하는 구조라서
        //  상황에 맞는 메서드를 골라 호출해야 합니다.)

        // 변경된 장바구니 내용을 저장해요. 안 하면 방금 추가한 책이 사라져요.
        cartService.update(sessionId, cart);
    }
    @DeleteMapping("/{cartId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCartByItem(@PathVariable("bookId") String bookId,
                                 HttpServletRequest request) {
        String sessionId = request.getSession(true).getId();
        Cart cart = cartService.read(sessionId);
        if(cart == null)
        {
            cart = cartService.create(new Cart(sessionId));
        }
        Book book = bookService.getBookById(bookId);
        if(book == null)
        {
            throw new IllegalArgumentException(new BookIdException(bookId));
        }
        cart.removeCartitem(new CartItem(book));
        cartService.update(sessionId, cart);
    }
}