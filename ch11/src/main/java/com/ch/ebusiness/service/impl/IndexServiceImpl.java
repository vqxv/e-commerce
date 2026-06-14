package com.ch.ebusiness.service.impl;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.IndexRepository;
import com.ch.ebusiness.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexRepository indexRepository;

    @Override
    public String index(Model model, Integer tid) {
        if (tid == null) tid = 0;
        model.addAttribute("advertisementGoods", indexRepository.selectAdvertisementGoods());
        model.addAttribute("goodsType", indexRepository.selectGoodsType());
        model.addAttribute("recommendGoods", indexRepository.selectRecommendGoods(tid));
        model.addAttribute("lastedGoods", indexRepository.selectLastedGoods(tid));
        return "user/index";
    }

    @Override
    public String search(Model model, String mykey) {
        model.addAttribute("advertisementGoods", indexRepository.selectAdvertisementGoods());
        model.addAttribute("goodsType", indexRepository.selectGoodsType());
        model.addAttribute("searchgoods", indexRepository.search(mykey));
        return "user/searchResult";
    }

    @Override
    public String goodsDetail(Model model, Integer id) {
        model.addAttribute("advertisementGoods", indexRepository.selectAdvertisementGoods());
        model.addAttribute("goodsType", indexRepository.selectGoodsType());
        model.addAttribute("goods", indexRepository.selectAGoods(id));
        return "user/goodsDetail";
    }
}
