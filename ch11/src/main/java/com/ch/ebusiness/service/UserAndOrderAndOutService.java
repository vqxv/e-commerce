package com.ch.ebusiness.service;

import org.springframework.ui.Model;

public interface UserAndOrderAndOutService {
    String selectOrder(Model model, int currentPage);

    String selectUser(Model model, int currentPage);
}
