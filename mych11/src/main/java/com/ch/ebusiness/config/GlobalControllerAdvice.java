package com.ch.ebusiness.config;

import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.GoodsTypeRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final GoodsTypeRepository goodsTypeRepository;

    public GlobalControllerAdvice(GoodsTypeRepository goodsTypeRepository) {
        this.goodsTypeRepository = goodsTypeRepository;
    }

    @ModelAttribute("goodsTypeList")
    public List<GoodsType> getGoodsTypeList() {
        return goodsTypeRepository.findAll();
    }
}