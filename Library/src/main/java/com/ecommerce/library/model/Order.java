package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "shipping_fee")
    private double shippingFee;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "tax")
    private double tax;

    @Column(name = "payment")
    private String payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderDetail> orderDetailList;

}
