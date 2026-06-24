package com.ch.ebusiness.config;

import com.ch.ebusiness.entity.*;
import com.ch.ebusiness.repository.GoodsRepository;
import com.ch.ebusiness.repository.GoodsTypeRepository;
import com.ch.ebusiness.repository.OrderRepository;
import com.ch.ebusiness.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      GoodsTypeRepository goodsTypeRepository,
                                      GoodsRepository goodsRepository,
                                      OrderRepository orderRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) return;

            // 创建管理员
            User admin = new User();
            admin.setEmail("admin@shop.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);

            // 创建普通用户
            User user1 = new User();
            user1.setEmail("user1@shop.com");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.setRole("USER");
            userRepository.save(user1);

            User user2 = new User();
            user2.setEmail("user2@shop.com");
            user2.setPassword(passwordEncoder.encode("user123"));
            user2.setRole("USER");
            userRepository.save(user2);

            User user3 = new User();
            user3.setEmail("user3@shop.com");
            user3.setPassword(passwordEncoder.encode("user123"));
            user3.setRole("USER");
            userRepository.save(user3);

            // 创建分类
            GoodsType t1 = new GoodsType(); t1.setTypename("电子数码"); goodsTypeRepository.save(t1);
            GoodsType t2 = new GoodsType(); t2.setTypename("服装鞋帽"); goodsTypeRepository.save(t2);
            GoodsType t3 = new GoodsType(); t3.setTypename("家居用品"); goodsTypeRepository.save(t3);
            GoodsType t4 = new GoodsType(); t4.setTypename("图书音像"); goodsTypeRepository.save(t4);
            GoodsType t5 = new GoodsType(); t5.setTypename("运动户外"); goodsTypeRepository.save(t5);

            // 创建商品列表
            List<Object[]> goodsData = Arrays.asList(
                new Object[]{"iPhone 15 Pro","苹果手机，A17芯片",t1,"phone.jpg",7999.0,8999.0,1,1},
                new Object[]{"MacBook Air M3","轻薄笔记本，M3芯片",t1,"laptop.jpg",10999.0,12999.0,1,1},
                new Object[]{"Sony降噪耳机","行业领先降噪技术",t1,"earphone.jpg",2299.0,2999.0,1,0},
                new Object[]{"iPad Air","M2芯片，多功能平板",t1,"tablet.jpg",3999.0,4799.0,0,0},
                new Object[]{"Nike Air Max","经典气垫跑步鞋",t2,"shoes.jpg",899.0,1299.0,1,1},
                new Object[]{"优衣库羽绒服","轻薄保暖",t2,"jacket.jpg",599.0,999.0,0,0},
                new Object[]{"戴森吸尘器","智能除尘激光探测",t3,"lamp.jpg",3990.0,4990.0,1,0},
                new Object[]{"小米空气净化器","除甲醛除雾霾",t3,"air.jpg",1199.0,1599.0,0,0},
                new Object[]{"Java核心技术","Java开发者必读",t4,"book1.jpg",89.0,138.0,1,0},
                new Object[]{"算法导论","计算机经典教材",t4,"algorithm.jpg",89.0,128.0,0,0},
                new Object[]{"Apple Watch","健康监测运动追踪",t1,"watch.jpg",2499.0,2999.0,1,1},
                new Object[]{"全棉四件套","100%纯棉亲肤",t3,"bed.jpg",499.0,699.0,0,0},
                new Object[]{"瑜伽垫","加厚防滑居家健身",t5,"yoga.jpg",129.0,199.0,1,0},
                new Object[]{"登山包","50L大容量徒步",t5,"bag.jpg",349.0,499.0,0,0},
                new Object[]{"运动手环","心率监测消息提醒",t5,"band.jpg",299.0,399.0,0,0},
                new Object[]{"SK-II神仙水","护肤精华改善肤质",t3,"skincare.jpg",1390.0,1590.0,1,0},
                new Object[]{"坚果礼包","混合坚果送礼佳品",t3,"snack.jpg",128.0,198.0,0,0},
                new Object[]{"Levis牛仔裤","经典511版型",t2,"jeans.jpg",698.0,998.0,0,0}
            );

            List<Goods> goodsList = new ArrayList<>();
            for (Object[] gd : goodsData) {
                Goods g = new Goods();
                g.setName((String)gd[0]);
                g.setDescription((String)gd[1]);
                g.setGoodsType((GoodsType)gd[2]);
                g.setPicture((String)gd[3]);
                g.setCurrentPrice((Double)gd[4]);
                g.setOriginalPrice((Double)gd[5]);
                g.setStock(100);
                g.setIsRecommend((Integer)gd[6]);
                g.setIsAdvertisement((Integer)gd[7]);
                goodsList.add(g);
            }
            goodsRepository.saveAll(goodsList);

            // 创建订单
            for (int i = 0; i < 20; i++) {
                User u = (i % 3 == 0) ? user1 : (i % 3 == 1) ? user2 : user3;
                Order o = new Order();
                o.setUser(u);
                o.setStatus(i % 3);
                o.setTotalAmount(100 + Math.random() * 5000);
                o.setCreateTime(LocalDateTime.now().minusDays(i));
                orderRepository.save(o);
            }
        };
    }
}