package com.ebusiness.controller;

import com.ebusiness.entity.*;
import com.ebusiness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('USER')")
public class FocusOrderController {

    @Autowired
    private FocusRepository focusRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private UserRepository userRepository;

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email);
    }

    /**
     * 添加收藏
     */
    @PostMapping("/focus/{goodsId}")
    public ResponseEntity<Map<String, Object>> addFocus(@PathVariable Integer goodsId) {
        User user = currentUser();
        if (focusRepository.existsByUserAndGoodsId(user, goodsId)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "已收藏"));
        }
        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        if (goods == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "商品不存在"));
        }
        Focus focus = new Focus();
        focus.setUser(user);
        focus.setGoods(goods);
        focusRepository.save(focus);
        return ResponseEntity.ok(Map.of("success", true));
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/focus/{goodsId}")
    public ResponseEntity<Map<String, Object>> removeFocus(@PathVariable Integer goodsId) {
        User user = currentUser();
        if (!focusRepository.existsByUserAndGoodsId(user, goodsId)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "未收藏"));
        }
        focusRepository.deleteByUserAndGoodsId(user, goodsId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    /**
     * 收藏列表
     */
    @GetMapping("/focus")
    public List<Map<String, Object>> listFocus() {
        User user = currentUser();
        List<Focus> focuses = focusRepository.findByUser(user);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Focus f : focuses) {
            Map<String, Object> item = new HashMap<>();
            Goods g = f.getGoods();
            item.put("goodsId", g.getId());
            item.put("name", g.getName());
            item.put("picture", g.getPicture());
            item.put("currentPrice", g.getCurrentPrice());
            item.put("originalPrice", g.getOriginalPrice());
            result.add(item);
        }
        return result;
    }

    /**
     * 我的订单列表
     */
    @GetMapping("/orders")
    public List<Map<String, Object>> listOrders() {
        User user = currentUser();
        List<Order> orders = orderRepository.findByUserOrderByCreateTimeDesc(user);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Order o : orders) {
            Map<String, Object> item = new HashMap<>();
            item.put("orderId", o.getId());
            item.put("totalAmount", o.getTotalAmount());
            item.put("status", o.getStatus()); // 0待支付, 1已支付
            item.put("createTime", o.getCreateTime().toString());
            result.add(item);
        }
        return result;
    }

    /**
     * 支付订单
     */
    @PutMapping("/orders/{orderId}/pay")
    @Transactional
    public ResponseEntity<Map<String, Object>> payOrder(@PathVariable Integer orderId) {
        User user = currentUser();
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body(Map.of("error", "订单不存在"));
        }
        if (order.getStatus() != 0) {
            return ResponseEntity.ok(Map.of("success", true, "message", "已支付"));
        }
        order.setStatus(1);
        orderRepository.save(order);
        return ResponseEntity.ok(Map.of("success", true));
    }

    /**
     * 订单详情（商品明细）
     */
    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<?> orderItems(@PathVariable Integer orderId) {
        User user = currentUser();
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body(Map.of("error", "订单不存在"));
        }
        List<Map<String, Object>> items = new ArrayList<>();
        for (OrderItem oi : order.getItems()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", oi.getGoods().getName());
            item.put("picture", oi.getGoods().getPicture());
            item.put("price", oi.getPrice());
            item.put("quantity", oi.getQuantity());
            item.put("subtotal", oi.getPrice() * oi.getQuantity());
            items.add(item);
        }
        return ResponseEntity.ok(items);
    }
}

