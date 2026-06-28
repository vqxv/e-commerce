package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Order;
import com.ch.ebusiness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByCreateTimeDesc(User user);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Order o WHERE o.user = :user")
    void deleteByUser(@Param("user") User user);
}
