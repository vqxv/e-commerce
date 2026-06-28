package com.ch.ebusiness.controller;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.GoodsRepository;
import com.ch.ebusiness.repository.GoodsTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GoodsController {

    private final GoodsRepository goodsRepository;
    private final GoodsTypeRepository goodsTypeRepository;

    public GoodsController(GoodsRepository goodsRepository,
                           GoodsTypeRepository goodsTypeRepository) {
        this.goodsRepository = goodsRepository;
        this.goodsTypeRepository = goodsTypeRepository;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<Goods> advertisementGoods = goodsRepository.findByIsAdvertisement(1);
        List<Goods> recommendGoods = goodsRepository.findByIsRecommend(1);
        List<Goods> lastedGoods = goodsRepository.findAllByOrderByIdDesc();
        List<GoodsType> goodsTypeList = goodsTypeRepository.findAll();

        model.addAttribute("advertisementGoods", advertisementGoods);
        model.addAttribute("recommendGoods", recommendGoods);
        model.addAttribute("lastedGoods", lastedGoods);
        model.addAttribute("goodsTypeList", goodsTypeList);
        return "user/index";
    }

    @GetMapping("/goodsDetail")
    public String goodsDetail(@RequestParam("id") Integer id, Model model) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        model.addAttribute("goods", goods);
        return "user/goodsDetail";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword,
                         @RequestParam(name = "typeId", required = false) Integer typeId,
                         Model model) {
        // 查询所有商品分类（供搜索页分类标签使用）
        List<GoodsType> goodsTypeList = goodsTypeRepository.findAll();
        model.addAttribute("goodsTypeList", goodsTypeList);

        // 查询广告商品（供轮播图使用）
        List<Goods> advertisementGoods = goodsRepository.findByIsAdvertisement(1);
        model.addAttribute("advertisementGoods", advertisementGoods);

        List<Goods> goodsList;

        if (typeId != null) {
            // 按分类ID查询
            goodsList = goodsRepository.findByGoodsTypeId(typeId);
            GoodsType currentType = goodsTypeRepository.findById(typeId).orElse(null);
            model.addAttribute("currentType", currentType);
            model.addAttribute("selectedTypeId", typeId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // 按关键词搜索
            goodsList = goodsRepository.findByNameContaining(keyword.trim());
            model.addAttribute("keyword", keyword.trim());
        } else {
            // 无过滤条件，显示所有商品
            goodsList = goodsRepository.findAllByOrderByIdDesc();
        }

        model.addAttribute("goodsList", goodsList);
        return "user/searchResult";
    }
}