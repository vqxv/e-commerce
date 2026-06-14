package com.ch.ebusiness.service.impl;

import com.ch.ebusiness.repository.UserAndOrderAndOutRepository;
import com.ch.ebusiness.service.UserAndOrderAndOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Service
public class UserAndOrderAndOutServiceImpl implements UserAndOrderAndOutService {

    @Autowired
    private UserAndOrderAndOutRepository userAndOrderAndOutRepository;

    @Override
    public String selectOrder(Model model, int currentPage) {
        int totalCount = userAndOrderAndOutRepository.selectAllOrder();
        int pageSize = 5;
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        List<Map<String, Object>> orderByPage = userAndOrderAndOutRepository.selectOrderByPage((currentPage - 1) * pageSize, pageSize);
        model.addAttribute("allOrders", orderByPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        return "admin/allOrder";
    }

    @Override
    public String selectUser(Model model, int currentPage) {
        int totalCount = userAndOrderAndOutRepository.selectAllUser();
        int pageSize = 5;
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        List<Map<String, Object>> userByPage = userAndOrderAndOutRepository.selectUserByPage((currentPage - 1) * pageSize, pageSize);
        model.addAttribute("allUsers", userByPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        return "admin/allUser";
    }
}
