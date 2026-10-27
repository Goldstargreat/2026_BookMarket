package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderRepositoryImpl implements OrderRepository
{
    private Map<Long, Order> listofOrders;
    private long nextOrderId;

    public OrderRepositoryImpl()
    {
        listofOrders = new HashMap<Long, Order>();
        nextOrderId = 2000;
    }

    @Override
    public Long saveOrder(Order order)
    {
        order.setOrderId(getNextOrderId());
        listofOrders.put(order.getOrderId(), order);
        return order.getOrderId();
    }

    private synchronized long getNextOrderId()
    {
        return nextOrderId++;
    }
}