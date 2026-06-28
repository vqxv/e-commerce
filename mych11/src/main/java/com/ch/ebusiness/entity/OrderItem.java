package com.ch.ebusiness.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orderdetail")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderbasetable_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goodstable_id")
    private Goods goods;

    @Column(name = "shoppingnum", nullable = false)
    private Integer quantity = 1;

    @Column(name = "price", nullable = false)
    private Double price;
}