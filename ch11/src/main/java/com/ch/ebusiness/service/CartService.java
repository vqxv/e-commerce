package com.ch.ebusiness.service;

import com.ch.ebusiness.entity.Order;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface CartService {
    String focus(Model model, HttpSession session, Integer gid);

    String putCart(com.ch.ebusiness.entity.Goods goods, Model model, HttpSession session);

    String selectCart(Model model, HttpSession session, String act);

    String deleteCart(HttpSession session, Integer gid);

    String clearCart(HttpSession session);

    String submitOrder(Order order, Model model, HttpSession session);

    String pay(Order order);

    String myFocus(Model model, HttpSession session);

    String myOrder(Model model, HttpSession session);

    String orderDetail(Model model, Integer id);

    String updateUpwd(HttpSession session, String bpwd);
}
