package kr.ac.kopo.gnuyog.bookmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import kr.ac.kopo.gnuyog.bookmarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/cart")
public class CartController
{
    @Autowired
    private CartService cartService;
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
    public @ResponseBody Cart read(@PathVariable(value = "cartId") String cartId){
        return cartService.read(cartId);
    }
}
