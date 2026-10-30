package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.domain.Order;
import kr.ac.kopo.gnuyog.bookmarket.domain.OrderItem;
import kr.ac.kopo.gnuyog.bookmarket.repository.BookRepository;
import kr.ac.kopo.gnuyog.bookmarket.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

        // [수정] 예전에는 bookById 객체의 값만 바꿔서 DB의 재고는 그대로였다.
        //        → DB의 재고를 직접 줄인다
        int count = bookRepository.decreaseUnitsInStock(bookId, quantity);

        // 위에서 확인한 뒤 그 사이에 다른 사람이 먼저 사 가서 재고가 모자라면 0이 나온다
        if (count == 0)
            throw new IllegalArgumentException("도서 수량이 부족합니다. 도서: " + bookById.getName());
    }

    // [수정] 주문에 담긴 책마다 재고를 줄인 다음 주문을 저장한다
    // @Transactional: 이 메서드 안의 DB 작업을 하나로 묶는다.
    //   예) 책 A는 재고를 줄였는데 책 B가 재고 부족이면 → 책 A의 재고도 원래대로 되돌리고, 주문도 저장하지 않는다
    @Override
    @Transactional
    public Long saveOrder(Order order)
    {
        for (OrderItem item : order.getOrderItems().values())
        {
            confirmOrder(item.getBookId(), item.getQuantity());
        }

        Long orderId = orderRepository.saveOrder(order);
        return orderId;
    }
}
