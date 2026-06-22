package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {
    List<Goods> findByIsRecommend(Integer isRecommend);
    List<Goods> findByIsAdvertisement(Integer isAdvertisement);
    List<Goods> findByNameContaining(String keyword);
    List<Goods> findByGoodsTypeId(Integer typeId);
}
