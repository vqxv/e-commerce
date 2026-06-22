package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.CartItem;
import com.ch.ebusiness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUser(User user);
    CartItem findByUserAndGoodsId(User user, Integer goodsId);
    @Transactional
    void deleteByUser(User user);
}
