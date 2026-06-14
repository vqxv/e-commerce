package com.ch.ebusiness.repository;

import com.ch.ebusiness.entity.AUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminRepository {
    List<AUser> login(AUser aUser);
}
