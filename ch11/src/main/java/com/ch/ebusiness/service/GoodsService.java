package com.ch.ebusiness.service;

import com.ch.ebusiness.entity.Goods;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import java.io.IOException;

public interface GoodsService {
    String toAddGoods(Goods goods, Model model);

    String addGoods(Goods goods, HttpServletRequest request, String act) throws IllegalArgumentException, IOException;

    String selectAllGoodsByPage(Model model, int currentPage);

    String detail(Model model, Integer id, String act);

    String delete(Integer id);
}
