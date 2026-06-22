package com.ch.ebusiness.controller;

import com.ch.ebusiness.entity.*;
import com.ch.ebusiness.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final GoodsRepository goodsRepository;
    private final GoodsTypeRepository goodsTypeRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir:${user.dir}/src/main/resources/static/images/}")
    private String uploadDir;

    public AdminController(GoodsRepository goodsRepository,
                           GoodsTypeRepository goodsTypeRepository,
                           OrderRepository orderRepository,
                           UserRepository userRepository) {
        this.goodsRepository = goodsRepository;
        this.goodsTypeRepository = goodsTypeRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/toLogin")
    @PreAuthorize("permitAll()")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/salesData")
    @ResponseBody
    @Transactional(readOnly = true)
    public Map<String, Object> salesData() {
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        TreeMap<String, Double> monthlyMap = new TreeMap<>();

        List<Order> paidOrders = orderRepository.findAll(Sort.unsorted()).stream()
                .filter(o -> o.getStatus() != null && o.getStatus() == 1)
                .toList();

        if (paidOrders.isEmpty()) {
            monthlyMap.put("2025-06", 12000.0);
            monthlyMap.put("2025-07", 15000.0);
            monthlyMap.put("2025-08", 18000.0);
        } else {
            for (Order order : paidOrders) {
                String month = order.getCreateTime().format(monthFormat);
                monthlyMap.merge(month, order.getTotalAmount(), Double::sum);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("categories", new ArrayList<>(monthlyMap.keySet()));
        result.put("values", new ArrayList<>(monthlyMap.values()));
        return result;
    }

    @GetMapping("/goods")
    public String goodsPage() {
        return "admin/goods";
    }

    @GetMapping("/goods/list")
    @ResponseBody
    public Page<Goods> goodsList(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size) {
        return goodsRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    @PostMapping("/goods/add")
    @ResponseBody
    public ResponseEntity<?> addGoods(@ModelAttribute Goods goods,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            goods.setPicture(saveFile(file));
        }
        goodsRepository.save(goods);
        return ResponseEntity.ok(Map.of("message", "添加成功"));
    }

    @PostMapping("/goods/update")
    @ResponseBody
    public ResponseEntity<?> updateGoods(@ModelAttribute Goods updated,
                                         @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Goods goods = goodsRepository.findById(updated.getId()).orElse(null);
        if (goods == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "商品不存在"));
        }
        goods.setName(updated.getName());
        goods.setOriginalPrice(updated.getOriginalPrice());
        goods.setCurrentPrice(updated.getCurrentPrice());
        goods.setStock(updated.getStock());
        goods.setIsRecommend(updated.getIsRecommend());
        goods.setIsAdvertisement(updated.getIsAdvertisement());
        goods.setGoodsType(updated.getGoodsType());
        if (file != null && !file.isEmpty()) {
            goods.setPicture(saveFile(file));
        }
        goodsRepository.save(goods);
        return ResponseEntity.ok(Map.of("message", "修改成功"));
    }

    @PostMapping("/goods/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteGoods(@PathVariable Integer id) {
        try {
            goodsRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("result", "no"));
        }
        return ResponseEntity.ok(Map.of("result", "ok"));
    }

    @GetMapping("/types")
    public String typesPage() {
        return "admin/types";
    }

    @GetMapping("/types/list")
    @ResponseBody
    public List<GoodsType> typesList() {
        return goodsTypeRepository.findAll();
    }

    @PostMapping("/types/add")
    @ResponseBody
    public ResponseEntity<?> addType(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "类型名称不能为空"));
        }
        GoodsType type = new GoodsType();
        type.setTypename(name.trim());
        goodsTypeRepository.save(type);
        return ResponseEntity.ok(Map.of("message", "添加成功"));
    }

    @PostMapping("/types/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteType(@PathVariable Integer id) {
        try {
            goodsTypeRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("result", "no"));
        }
        return ResponseEntity.ok(Map.of("result", "ok"));
    }

    @GetMapping("/orders")
    public String ordersPage() {
        return "admin/ordersUsers";
    }

    @GetMapping("/orders/list")
    @ResponseBody
    public List<Order> ordersList() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    @GetMapping("/users")
    public String usersPage() {
        return "admin/ordersUsers";
    }

    @GetMapping("/users/list")
    @ResponseBody
    public List<User> usersList() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    private String saveFile(MultipartFile file) throws IOException {
        String original = file.getOriginalFilename();
        String ext = original != null ? original.substring(original.lastIndexOf(".")) : ".jpg";
        String filename = UUID.randomUUID() + ext;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(new File(dir, filename));
        return filename;
    }
}
