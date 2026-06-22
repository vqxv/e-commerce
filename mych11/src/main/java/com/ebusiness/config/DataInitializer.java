package com.ebusiness.config;

import com.ebusiness.entity.*;
import com.ebusiness.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      GoodsTypeRepository goodsTypeRepository,
                                      GoodsRepository goodsRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // 管理员
            if (userRepository.findByEmail("admin@shop.com") == null) {
                User admin = new User();
                admin.setEmail("admin@shop.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }
            // 普通用户
            if (userRepository.findByEmail("user@shop.com") == null) {
                User user = new User();
                user.setEmail("user@shop.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("USER");
                userRepository.save(user);
            }

            // 商品和分类仅当分类表为空时创建
            if (goodsTypeRepository.count() == 0) {
                GoodsType type1 = new GoodsType();
                type1.setTypename("电子数码");
                goodsTypeRepository.save(type1);

                GoodsType type2 = new GoodsType();
                type2.setTypename("服装鞋帽");
                goodsTypeRepository.save(type2);

                GoodsType type3 = new GoodsType();
                type3.setTypename("图书音像");
                goodsTypeRepository.save(type3);

                Goods g1 = new Goods();
                g1.setName("智能手机");
                g1.setOriginalPrice(3999.0);
                g1.setCurrentPrice(2999.0);
                g1.setStock(100);
                g1.setPicture("phone.jpg");
                g1.setIsRecommend(1);
                g1.setIsAdvertisement(1);
                g1.setGoodsType(type1);
                goodsRepository.save(g1);

                Goods g2 = new Goods();
                g2.setName("蓝牙耳机");
                g2.setOriginalPrice(599.0);
                g2.setCurrentPrice(399.0);
                g2.setStock(200);
                g2.setPicture("headphone.jpg");
                g2.setIsRecommend(1);
                g2.setIsAdvertisement(1);
                g2.setGoodsType(type1);
                goodsRepository.save(g2);

                Goods g3 = new Goods();
                g3.setName("运动跑鞋");
                g3.setOriginalPrice(899.0);
                g3.setCurrentPrice(599.0);
                g3.setStock(150);
                g3.setPicture("shoes.jpg");
                g3.setIsRecommend(0);
                g3.setIsAdvertisement(0);
                g3.setGoodsType(type2);
                goodsRepository.save(g3);

                Goods g4 = new Goods();
                g4.setName("Java编程思想");
                g4.setOriginalPrice(108.0);
                g4.setCurrentPrice(79.0);
                g4.setStock(50);
                g4.setPicture("book.jpg");
                g4.setIsRecommend(0);
                g4.setIsAdvertisement(0);
                g4.setGoodsType(type3);
                goodsRepository.save(g4);

                Goods g5 = new Goods();
                g5.setName("平板电脑");
                g5.setOriginalPrice(3299.0);
                g5.setCurrentPrice(2799.0);
                g5.setStock(80);
                g5.setPicture("tablet.jpg");
                g5.setIsRecommend(0);
                g5.setIsAdvertisement(0);
                g5.setGoodsType(type1);
                goodsRepository.save(g5);

                Goods g6 = new Goods();
                g6.setName("休闲T恤");
                g6.setOriginalPrice(199.0);
                g6.setCurrentPrice(129.0);
                g6.setStock(300);
                g6.setPicture("tshirt.jpg");
                g6.setIsRecommend(0);
                g6.setIsAdvertisement(0);
                g6.setGoodsType(type2);
                goodsRepository.save(g6);

                Goods g7 = new Goods();
                g7.setName("算法导论");
                g7.setOriginalPrice(128.0);
                g7.setCurrentPrice(89.0);
                g7.setStock(60);
                g7.setPicture("algorithm.jpg");
                g7.setIsRecommend(0);
                g7.setIsAdvertisement(0);
                g7.setGoodsType(type3);
                goodsRepository.save(g7);

                Goods g8 = new Goods();
                g8.setName("智能手表");
                g8.setOriginalPrice(1599.0);
                g8.setCurrentPrice(1299.0);
                g8.setStock(120);
                g8.setPicture("watch.jpg");
                g8.setIsRecommend(0);
                g8.setIsAdvertisement(0);
                g8.setGoodsType(type1);
                goodsRepository.save(g8);
            }
        };
    }
}
