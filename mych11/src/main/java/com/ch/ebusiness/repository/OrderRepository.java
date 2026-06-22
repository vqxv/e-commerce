package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Order;
import com.ch.ebusiness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByCreateTimeDesc(User user);
}
