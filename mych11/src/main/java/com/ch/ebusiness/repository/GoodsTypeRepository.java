package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.GoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsTypeRepository extends JpaRepository<GoodsType, Integer> {
    GoodsType findByTypename(String typename);
}
