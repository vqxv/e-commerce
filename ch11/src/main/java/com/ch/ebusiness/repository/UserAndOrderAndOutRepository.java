package com.ch.ebusiness.repository;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAndOrderAndOutRepository {
    int selectAllOrder();

    List<Map<String, Object>> selectOrderByPage(int startIndex, int perPageSize);

    int selectAllUser();

    List<Map<String, Object>> selectUserByPage(int startIndex, int perPageSize);
}
