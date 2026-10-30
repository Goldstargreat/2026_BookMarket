package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository
{
    // [수정] 예전에는 HashMap(메모리)에 저장해서 서버를 끄면 주문이 사라졌다.
    //        → 이미 만들어 둔 JPA 저장소(OrderProRepository)를 써서 DB에 저장한다.
    @Autowired
    private OrderProRepository orderProRepository;

    @Override
    public Long saveOrder(Order order)
    {
        // save()가 끝나면 DB가 만든 주문번호가 order.getOrderId()에 들어 있다
        orderProRepository.save(order);
        return order.getOrderId();
    }
}
