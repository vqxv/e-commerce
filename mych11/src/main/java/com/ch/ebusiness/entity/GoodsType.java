package com.ch.ebusiness.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "goodstype")
public class GoodsType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "typename", nullable = false, length = 50)
    private String typename;
}