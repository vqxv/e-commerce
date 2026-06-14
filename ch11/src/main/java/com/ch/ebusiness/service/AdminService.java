package com.ch.ebusiness.service;

import com.ch.ebusiness.entity.AUser;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

public interface AdminService {
    String login(AUser aUser, HttpSession session, Model model);
}
