package com.ch.ebusiness.service;

import com.ch.ebusiness.entity.BUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface UserService {
    String isUse(BUser bUser);

    String register(BUser bUser);

    String login(BUser bUser, HttpSession session, Model model);
}
