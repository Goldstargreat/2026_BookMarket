package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;

public interface CartRepository
{
    Cart create(Cart cart);
    Cart read(String cartId);
}
