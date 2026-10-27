package kr.ac.kopo.gnuyog.bookmarket.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;
    private String name;
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")

    private Address address;

}
