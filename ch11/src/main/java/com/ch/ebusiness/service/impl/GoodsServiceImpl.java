package com.ch.ebusiness.service.impl;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import com.ch.ebusiness.repository.GoodsRepository;
import com.ch.ebusiness.service.GoodsService;
import com.ch.ebusiness.util.MyUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public String toAddGoods(Goods goods, Model model) {
        model.addAttribute("goodsType", goodsRepository.selectAllGoodsType());
        return "admin/addGoods";
    }

    @Override
    public String addGoods(Goods goods, HttpServletRequest request, String act) throws IllegalArgumentException, IOException {
        MultipartFile myfile = goods.getFileName();
        if (!myfile.isEmpty()) {
            String path = request.getServletContext().getRealPath("/images/");
            String fileName = myfile.getOriginalFilename();
            String fileNewName = MyUtil.getNewFileName(fileName);
            File filePath = new File(path + File.separator + fileNewName);
            if (!filePath.getParentFile().exists()) {
                filePath.getParentFile().mkdirs();
            }
            myfile.transferTo(filePath);
            goods.setGpicture(fileNewName);
        }
        if ("add".equals(act)) {
            int n = goodsRepository.addGoods(goods);
            if (n > 0)
                return "redirect:/goods/selectAllGoodsByPage?currentPage=1&act=select";
            return "admin/addGoods";
        } else {
            int n = goodsRepository.updateGoods(goods);
            if (n > 0)
                return "redirect:/goods/selectAllGoodsByPage?currentPage=1&act=updateSelect";
            return "admin/UpdateAGoods";
        }
    }

    @Override
    public String selectAllGoodsByPage(Model model, int currentPage) {
        int totalCount = goodsRepository.selectAllGoods();
        int pageSize = 5;
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        List<Goods> typeByPage = goodsRepository.selectAllGoodsByPage((currentPage - 1) * pageSize, pageSize);
        model.addAttribute("allGoods", typeByPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        return "admin/adminGoods";
    }

    @Override
    public String detail(Model model, Integer id, String act) {
        model.addAttribute("goods", goodsRepository.selectAGoods(id));
        if ("detail".equals(act))
            return "admin/detail";
        else {
            model.addAttribute("goodsType", goodsRepository.selectAllGoodsType());
            return "admin/updateAGoods";
        }
    }

    @Override
    public String delete(Integer id) {
        if (goodsRepository.selectCartGoods(id).size() > 0
                || goodsRepository.selectFocusGoods(id).size() > 0
                || goodsRepository.selectOrderGoods(id).size() > 0) {
            return "no";
        } else {
            goodsRepository.deleteAGoods(id);
            return "/goods/selectAllGoodsByPage?currentPage=1";
        }
    }
}
