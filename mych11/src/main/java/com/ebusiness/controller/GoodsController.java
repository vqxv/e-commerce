package com.ebusiness.controller;

import com.ebusiness.entity.Goods;
import com.ebusiness.entity.GoodsType;
import com.ebusiness.repository.GoodsRepository;
import com.ebusiness.repository.GoodsTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品展示 REST API
 */
@RestController
@RequestMapping("/api")
public class GoodsController {

    private final GoodsRepository goodsRepository;
    private final GoodsTypeRepository goodsTypeRepository;

    public GoodsController(GoodsRepository goodsRepository,
                           GoodsTypeRepository goodsTypeRepository) {
        this.goodsRepository = goodsRepository;
        this.goodsTypeRepository = goodsTypeRepository;
    }

    /**
     * 商品列表（支持按类型筛选、关键字搜索）
     */
    @GetMapping("/goods")
    public List<GoodsVO> getGoods(@RequestParam(required = false) Integer typeId,
                                  @RequestParam(required = false) String keyword) {
        List<Goods> goodsList;
        if (keyword != null && !keyword.isBlank()) {
            goodsList = goodsRepository.findByNameContaining(keyword.trim());
        } else if (typeId != null) {
            goodsList = goodsRepository.findByGoodsTypeId(typeId);
        } else {
            goodsList = goodsRepository.findAll();
        }
        return goodsList.stream().map(GoodsVO::new).collect(Collectors.toList());
    }

    /**
     * 单个商品详情
     */
    @GetMapping("/goods/{id}")
    public GoodsVO getGoodsDetail(@PathVariable Integer id) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        return new GoodsVO(goods);
    }

    /**
     * 所有商品分类
     */
    @GetMapping("/goodstypes")
    public List<GoodsTypeVO> getGoodsTypes() {
        return goodsTypeRepository.findAll().stream()
                .map(t -> new GoodsTypeVO(t.getId().intValue(), t.getTypename()))
                .collect(Collectors.toList());
    }

    // ---- 内部 VO 类，避免序列化循环 ----
    static class GoodsVO {
        public Integer id;
        public String name;
        public String picture;
        public Double currentPrice;
        public Double originalPrice;
        public Integer stock;
        public Integer isRecommend;
        public Integer isAdvertisement;
        public Integer typeId;
        public String typeName;

        public GoodsVO(Goods goods) {
            this.id = goods.getId().intValue();
            this.name = goods.getName();
            this.picture = goods.getPicture();
            this.currentPrice = goods.getCurrentPrice();
            this.originalPrice = goods.getOriginalPrice();
            this.stock = goods.getStock();
            this.isRecommend = goods.getIsRecommend();
            this.isAdvertisement = goods.getIsAdvertisement();
            if (goods.getGoodsType() != null) {
                this.typeId = goods.getGoodsType().getId().intValue();
                this.typeName = goods.getGoodsType().getTypename();
            }
        }
    }

    static class GoodsTypeVO {
        public Integer id;
        public String name;

        public GoodsTypeVO(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
