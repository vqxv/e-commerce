package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Focus;
import com.ch.ebusiness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FocusRepository extends JpaRepository<Focus, Integer> {
    boolean existsByUserAndGoodsId(User user, Integer goodsId);
    void deleteByUserAndGoodsId(User user, Integer goodsId);
    List<Focus> findByUser(User user);
}
