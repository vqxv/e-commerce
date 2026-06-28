package com.ch.ebusiness.controller;

import com.ch.ebusiness.entity.*;
import com.ch.ebusiness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FocusRepository focusRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email);
    }

    /**
     * 收藏 / 取消收藏
     */
    @PostMapping("/focus")
    @ResponseBody
    @Transactional
    public String focus(@RequestParam Integer goodsId) {
        User user = getCurrentUser();
        if (focusRepository.existsByUserAndGoodsId(user, goodsId)) {
            focusRepository.deleteByUserAndGoodsId(user, goodsId);
            return "no";
        }
        Focus focus = new Focus();
        focus.setUser(user);
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        focus.setGoods(goods);
        focusRepository.save(focus);
        return "ok";
    }

    /**
     * 加入购物车
     */
    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Integer goodsId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        User user = getCurrentUser();
        CartItem exist = cartItemRepository.findByUserAndGoodsId(user, goodsId);
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + quantity);
            cartItemRepository.save(exist);
        } else {
            Goods goods = goodsRepository.findById(goodsId)
                    .orElseThrow(() -> new RuntimeException("商品不存在"));
            CartItem item = new CartItem();
            item.setUser(user);
            item.setGoods(goods);
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return "ok";
    }

    /**
     * 购物车列表JSON（供侧边栏使用）
     */
    @GetMapping("/viewJson")
    @ResponseBody
    public List<Map<String, Object>> viewCartJson() {
        User user = getCurrentUser();
        List<CartItem> items = cartItemRepository.findByUser(user);
        List<Map<String, Object>> result = new ArrayList<>();
        for (CartItem item : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("quantity", item.getQuantity());
            map.put("price", item.getGoods().getCurrentPrice());
            Map<String, Object> goods = new HashMap<>();
            goods.put("id", item.getGoods().getId());
            goods.put("name", item.getGoods().getName());
            goods.put("picture", item.getGoods().getPicture());
            goods.put("currentPrice", item.getGoods().getCurrentPrice());
            map.put("goods", goods);
            result.add(map);
        }
        return result;
    }

    /**
     * 购物车列表
     */
    @GetMapping("/view")
    public String viewCart(Model model) {
        User user = getCurrentUser();
        List<CartItem> items = cartItemRepository.findByUser(user);
        double total = 0;
        for (CartItem item : items) {
            total += item.getGoods().getCurrentPrice() * item.getQuantity();
        }
        model.addAttribute("cartItems", items);
        model.addAttribute("total", total);
        return "user/cart";
    }

    /**
     * 删除购物车项
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteCartItem(@PathVariable Integer id) {
        cartItemRepository.deleteById(id);
        return "ok";
    }

    /**
     * 清空购物车
     */
    @PostMapping("/clear")
    @ResponseBody
    public String clearCart() {
        User user = getCurrentUser();
        cartItemRepository.deleteByUser(user);
        return "ok";
    }

    /**
     * 提交订单
     */
    @PostMapping("/submitOrder")
    @ResponseBody
    @Transactional
    public String submitOrder() {
        User user = getCurrentUser();
        List<CartItem> items = cartItemRepository.findByUser(user);
        if (items.isEmpty()) {
            return "empty";
        }
        for (CartItem ci : items) {
            if (ci.getGoods().getStock() < ci.getQuantity()) {
                return "stock";
            }
        }
        Order order = new Order();
        order.setUser(user);
        order.setCreateTime(LocalDateTime.now());
        order.setStatus(0);
        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : items) {
            Goods goods = ci.getGoods();
            goods.setStock(goods.getStock() - ci.getQuantity());
            goodsRepository.save(goods);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setGoods(goods);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(goods.getCurrentPrice());
            orderItems.add(oi);
            total += oi.getPrice() * oi.getQuantity();
        }
        order.setTotalAmount(total);
        order.setItems(orderItems);
        orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
        return "ok";
    }

    /**
     * 支付
     */
    @PostMapping("/pay/{orderId}")
    @ResponseBody
    @Transactional
    public String payOrder(@PathVariable Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        if (order.getStatus() != 0) {
            return "paid";
        }
        order.setStatus(1);
        orderRepository.save(order);
        return "ok";
    }

    /**
     * 我的收藏
     */
    @GetMapping("/focusList")
    public String focus(Model model) {
        User user = getCurrentUser();
        model.addAttribute("focuses", focusRepository.findByUser(user));
        return "user/myFocus";
    }

    /**
     * 我的订单
     */
    @GetMapping("/orders")
    public String orders(Model model) {
        User user = getCurrentUser();
        model.addAttribute("orders", orderRepository.findByUserOrderByCreateTimeDesc(user));
        return "user/myOrder";
    }

    /**
     * 订单详情
     */
    @GetMapping("/orderDetail/{id}")
    public String orderDetail(@PathVariable Integer id, Model model) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        model.addAttribute("order", order);
        return "user/orderDetail";
    }
}

