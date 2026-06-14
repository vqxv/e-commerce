package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Order;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartRepository {
    List<Map<String, Object>> isFocus(Integer uid, Integer gid);

    void focus(Integer uid, Integer gid);

    List<Map<String, Object>> isPutCart(Integer uid, Integer gid);

    void putCart(Integer uid, Integer gid, Integer bnum);

    void updateCart(Integer uid, Integer gid, Integer bnum);

    List<Map<String, Object>> selectCart(Integer uid);

    void deleteAgoods(Integer uid, Integer gid);

    void clear(Integer uid);

    void addOrder(Order order);

    void addOrderDetail(Integer ordersn, Integer uid);

    List<Map<String, Object>> selectGoodsShop(Integer uid);

    void updateStore(Map<String, Object> map);

    void pay(Integer ordersn);

    List<Map<String, Object>> myFocus(Integer uid);

    List<Map<String, Object>> myOrder(Integer uid);

    List<Map<String, Object>> orderDetail(Integer id);

    void updateUpwd(Integer uid, String bpwd);
}
