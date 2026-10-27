package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Order;

public interface OrderService
{
    void confirmOrder(String bookId, long quantity); // 도서의 재고수량 확인
    Long saveOrder(Order order);
}
