package com.ch.ebusiness.controller;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.GoodsRepository;
import com.ch.ebusiness.repository.GoodsTypeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        List<Goods> lastedGoods = goodsRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))).getContent();
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
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Goods> goodsList = goodsRepository.findByNameContaining(keyword);
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("keyword", keyword);
        return "user/searchResult";
    }
}
