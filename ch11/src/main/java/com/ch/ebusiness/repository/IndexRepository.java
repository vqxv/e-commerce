package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.Goods;
import com.ch.ebusiness.entity.GoodsType;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IndexRepository {
    List<Goods> selectAdvertisementGoods();

    List<GoodsType> selectGoodsType();

    List<Goods> search(String mykey);

    List<Goods> selectRecommendGoods(Integer tid);

    List<Goods> selectLastedGoods(Integer tid);

    Goods selectAGoods(Integer id);
}
