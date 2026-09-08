package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CartRepositoryImpl implements CartRepository
{
    private Map<String, Cart> listofCarts;

    public CartRepositoryImpl(){
        listofCarts = new HashMap<String, Cart>();
    }

    @Override
    public Cart create(Cart cart)
    {
        if(listofCarts.keySet().contains(cart.getCartId()))
        {
            throw new IllegalArgumentException(String.format("장바구니를 새로 생성할 수 없습니다. 장바구니 %s id가 존재합니다.", cart.getCartId()));
        }
        listofCarts.put(cart.getCartId(), cart);

        return cart;
    }
    @Override
    public Cart read(String cartId)
    {
        return listofCarts.get(cartId);
    }

}
