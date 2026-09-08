package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;

public interface CartService
{
    Cart create(Cart cart);
    Cart read(String cartId);
}
