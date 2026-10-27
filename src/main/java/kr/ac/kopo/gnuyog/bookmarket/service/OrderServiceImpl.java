package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.domain.Order;
import kr.ac.kopo.gnuyog.bookmarket.repository.BookRepository;
import kr.ac.kopo.gnuyog.bookmarket.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl implements OrderService
{
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void confirmOrder(String bookId, long quantity)
    {
        Book bookById = bookRepository.getBookById(bookId);
        if(bookById.getUnitsInStock() < quantity)
            throw new IllegalArgumentException("도서 수량이 부족합니다. 구입 가능한 수량: " + bookById.getUnitsInStock());

        bookById.setUnitsInStock(bookById.getUnitsInStock() - quantity);
    }

    @Override
    public Long saveOrder(Order order)
    {
        Long orderId = orderRepository.saveOrder(order);
        return orderId;
    }
}
