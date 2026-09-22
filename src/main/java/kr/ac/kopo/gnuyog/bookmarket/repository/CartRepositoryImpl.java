package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CartRepositoryImpl implements CartRepository{
    private Map<String, Cart> listOfCarts;

    public CartRepositoryImpl() {
        listOfCarts = new HashMap<String, Cart>();
    }

    @Override
    public Cart create(Cart cart) {
        if (listOfCarts.keySet().contains(cart.getCartId())){
            throw new IllegalArgumentException(String.format("장바구니를 새로 생성할 수 없습니다. 장바구니 %s id가 이미 존재합니다.", cart.getCartId()));
        }

        listOfCarts.put(cart.getCartId(), cart);

        return cart;
    }

    @Override
    public Cart read(String cartId) {
        return listOfCarts.get(cartId);
    }

    @Override
    public void update(String cartId, Cart cart) {
        if (!listOfCarts.keySet().contains(cart.getCartId())){
            throw new IllegalArgumentException(String.format("장바구니 목록을 갱신할 수 없습니다. 장바구니 %s id가 존재하지 않습니다.", cartId));
        }

        listOfCarts.put(cartId, cart);
    }

    @Override
    public void delete(String cartId) {
        if (!listOfCarts.keySet().contains(cartId)){
            throw  new IllegalArgumentException(String.format("장바구니 목록 삭제 불가능. 이유: 해당 장바구니 id(%s)가 존재하지 않음", cartId));
        }

        listOfCarts.remove(cartId);
    }
}