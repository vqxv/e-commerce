package com.ch.ebusiness.service.impl;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.Order;
import com.ch.ebusiness.repository.CartRepository;
import com.ch.ebusiness.repository.IndexRepository;
import com.ch.ebusiness.service.CartService;
import com.ch.ebusiness.util.MD5Util;
import com.ch.ebusiness.util.MyUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private IndexRepository indexRepository;

    @Override
    public String focus(Model model, HttpSession session, Integer gid) {
        Integer uid = MyUtil.getUser(session).getId();
        List<Map<String, Object>> list = cartRepository.isFocus(uid, gid);
        if (list.size() > 0) {
            return "no";
        } else {
            cartRepository.focus(uid, gid);
            return "ok";
        }
    }

    @Override
    public String putCart(Goods goods, Model model, HttpSession session) {
        Integer uid = MyUtil.getUser(session).getId();
        if (cartRepository.isPutCart(uid, goods.getId()).size() > 0) {
            cartRepository.updateCart(uid, goods.getId(), goods.getBuyNumber());
        } else {
            cartRepository.putCart(uid, goods.getId(), goods.getBuyNumber());
        }
        return "forward:/cart/selectCart";
    }

    @Override
    public String selectCart(Model model, HttpSession session, String act) {
        List<Map<String, Object>> list = cartRepository.selectCart(MyUtil.getUser(session).getId());
        double sum = 0;
        for (Map<String, Object> map : list) {
            sum = sum + (Double) map.get("smallsum");
        }
        model.addAttribute("total", sum);
        model.addAttribute("cartlist", list);
        model.addAttribute("advertisementGoods", indexRepository.selectAdvertisementGoods());
        model.addAttribute("goodsType", indexRepository.selectGoodsType());
        if ("toCount".equals(act)) {
            return "user/count";
        }
        return "user/cart";
    }

    @Override
    public String deleteCart(HttpSession session, Integer gid) {
        Integer uid = MyUtil.getUser(session).getId();
        cartRepository.deleteAgoods(uid, gid);
        return "forward:/cart/selectCart";
    }

    @Override
    public String clearCart(HttpSession session) {
        cartRepository.clear(MyUtil.getUser(session).getId());
        return "forward:/cart/selectCart";
    }

    @Override
    @Transactional
    public String submitOrder(Order order, Model model, HttpSession session) {
        order.setBuserable_id(MyUtil.getUser(session).getId());
        cartRepository.addOrder(order);
        cartRepository.addOrderDetail(order.getId(), MyUtil.getUser(session).getId());
        List<Map<String, Object>> listGoods = cartRepository.selectGoodsShop(MyUtil.getUser(session).getId());
        for (Map<String, Object> map : listGoods) {
            cartRepository.updateStore(map);
        }
        cartRepository.clear(MyUtil.getUser(session).getId());
        model.addAttribute("order", order);
        return "user/pay";
    }

    @Override
    public String pay(Order order) {
        cartRepository.pay(order.getId());
        return "ok";
    }

    @Override
    public String myFocus(Model model, HttpSession session) {
        model.addAttribute("advertisementGoods", indexRepository.selectAdvertisementGoods());
        model.addAttribute("goodsType", indexRepository.selectGoodsType());
        model.addAttribute("myFocus", cartRepository.myFocus(MyUtil.getUser(session).getId()));
        return "user/myFocus";
    }

    @Override
    public String myOrder(Model model, HttpSession session) {
        model.addAttribute("advertisementGoods", indexRepository.selectAdvertisementGoods());
        model.addAttribute("goodsType", indexRepository.selectGoodsType());
        model.addAttribute("myOrder", cartRepository.myOrder(MyUtil.getUser(session).getId()));
        return "user/myOrder";
    }

    @Override
    public String orderDetail(Model model, Integer id) {
        model.addAttribute("orderDetail", cartRepository.orderDetail(id));
        return "user/orderDetail";
    }

    @Override
    public String updateUpwd(HttpSession session, String bpwd) {
        Integer uid = MyUtil.getUser(session).getId();
        cartRepository.updateUpwd(uid, MD5Util.MD5(bpwd));
        return "forward:/user/toLogin";
    }
}
