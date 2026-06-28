package com.ch.ebusiness.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carttable")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "busertable_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goodstable_id")
    private Goods goods;

    @Column(name = "shoppingnum", nullable = false)
    private Integer quantity = 1;
}