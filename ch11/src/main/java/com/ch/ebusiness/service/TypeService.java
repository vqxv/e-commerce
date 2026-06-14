package com.ch.ebusiness.service;

import com.ch.ebusiness.entity.GoodsType;
import org.springframework.ui.Model;

public interface TypeService {
    String addType(GoodsType goodsType);

    String selectAllTypeByPage(Model model, int currentPage);

    String delete(int id);
}
