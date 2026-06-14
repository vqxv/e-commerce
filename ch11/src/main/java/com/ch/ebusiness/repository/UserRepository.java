package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.BUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
    List<BUser> isUse(BUser bUser);

    int register(BUser bUser);

    List<BUser> login(BUser bUser);
}
