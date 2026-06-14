package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TypeRepository {
    int selectAll();

    List<GoodsType> selectAllTypeByPage(int startIndex, int perPageSize);

    void addType(GoodsType goodsType);

    List<Goods> selectGoods(int id);

    void deleteType(int id);
}
