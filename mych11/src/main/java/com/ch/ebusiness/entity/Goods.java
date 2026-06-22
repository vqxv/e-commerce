package com.ch.ebusiness.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gname", nullable = false, length = 200)
    private String name;

    @Column(name = "goprice", nullable = false)
    private Double originalPrice;

    @Column(name = "grprice", nullable = false)
    private Double currentPrice;

    @Column(name = "gstore", nullable = false)
    private Integer stock;

    @Column(name = "gpicture", length = 255)
    private String picture;

    @Column(name = "is_recommend")
    private Integer isRecommend = 0;

    @Column(name = "is_advertisement")
    private Integer isAdvertisement = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goodstype_id")
    private GoodsType goodsType;
}
