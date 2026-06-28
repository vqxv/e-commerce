package com.ch.ebusiness.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "focustable")
public class Focus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goodstable_id")
    private Goods goods;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "busertable_id")
    private User user;
}