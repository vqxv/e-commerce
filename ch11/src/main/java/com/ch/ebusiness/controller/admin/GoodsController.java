package com.ch.ebusiness.controller.admin;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.service.GoodsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/goods")
public class GoodsController extends AdminBaseController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/toAddGoods")
    public String toAddGoods(@ModelAttribute("goods") Goods goods, Model model) {
        goods.setIsAdvertisement(0);
        goods.setIsRecommend(1);
        return goodsService.toAddGoods(goods, model);
    }

    @RequestMapping("/addGoods")
    public String addGoods(@ModelAttribute("goods") Goods goods, HttpServletRequest request, String act) throws IllegalArgumentException, IOException {
        return goodsService.addGoods(goods, request, act);
    }

    @RequestMapping("/selectAllGoodsByPage")
    public String selectAllGoodsByPage(Model model, int currentPage) {
        return goodsService.selectAllGoodsByPage(model, currentPage);
    }

    @RequestMapping("/detail")
    public String detail(Model model, Integer id, String act) {
        return goodsService.detail(model, id, act);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Integer id) {
        return goodsService.delete(id);
    }
}
