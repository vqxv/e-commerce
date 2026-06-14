package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsRepository {
    int selectAllGoods();

    List<Goods> selectAllGoodsByPage(int startIndex, int perPageSize);

    List<GoodsType> selectAllGoodsType();

    int addGoods(Goods goods);

    Goods selectAGoods(Integer id);

    int updateGoods(Goods goods);

    List<Map<String, Object>> selectFocusGoods(Integer id);

    List<Map<String, Object>> selectCartGoods(Integer id);

    List<Map<String, Object>> selectOrderGoods(Integer id);

    void deleteAGoods(Integer id);
}
