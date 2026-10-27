package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Order;

public interface OrderRepository
{
    Long saveOrder(Order order);
}
