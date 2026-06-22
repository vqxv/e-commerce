package com.ebusiness.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orderdetail")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderbasetable_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goodstable_id", nullable = false)
    private Goods goods;

    @Column(name = "shoppingnum", nullable = false)
    private Integer quantity;

    @Column(name = "buy_price", nullable = false)
    private Double price;
}
