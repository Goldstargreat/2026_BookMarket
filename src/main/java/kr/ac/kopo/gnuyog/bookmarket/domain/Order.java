package kr.ac.kopo.gnuyog.bookmarket.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
// [수정] 사용하지 않는 import 2줄 삭제 (org.aspectj.weaver.ast.Or, java.lang.annotation.Target)

@Entity
@Data
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue
    private Long orderId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_order_id")
    @MapKey(name = "bookId") // [수정] Map의 키 = OrderItem의 bookId 필드
    private Map<String, OrderItem> orderItems = new HashMap<String, OrderItem>();

    private BigDecimal grandTotal;
}