package com.ch.ebusiness.service.impl;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.TypeRepository;
import com.ch.ebusiness.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Override
    public String addType(GoodsType goodsType) {
        typeRepository.addType(goodsType);
        return "redirect:/type/selectAllTypeByPage?currentPage=1";
    }

    @Override
    public String selectAllTypeByPage(Model model, int currentPage) {
        int totalCount = typeRepository.selectAll();
        int pageSize = 2;
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        List<GoodsType> typeByPage = typeRepository.selectAllTypeByPage((currentPage - 1) * pageSize, pageSize);
        model.addAttribute("allTypes", typeByPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        return "admin/selectGoodsType";
    }

    @Override
    public String delete(int id) {
        List<Goods> list = typeRepository.selectGoods(id);
        if (list.size() > 0) {
            return "no";
        } else {
            typeRepository.deleteType(id);
            return "/type/selectAllTypeByPage?currentPage=1";
        }
    }
}
