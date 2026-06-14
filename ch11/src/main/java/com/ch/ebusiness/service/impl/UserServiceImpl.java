package com.ch.ebusiness.service.impl;

import com.ch.ebusiness.entity.BUser;
import com.ch.ebusiness.repository.UserRepository;
import com.ch.ebusiness.service.UserService;
import com.ch.ebusiness.util.MD5Util;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String isUse(BUser bUser) {
        if (userRepository.isUse(bUser).size() > 0) {
            return "no";
        }
        return "ok";
    }

    @Override
    public String register(BUser bUser) {
        bUser.setBpwd(MD5Util.MD5(bUser.getBpwd()));
        if (userRepository.register(bUser) > 0) {
            return "user/login";
        }
        return "user/register";
    }

    @Override
    public String login(BUser bUser, HttpSession session, Model model) {
        bUser.setBpwd(MD5Util.MD5(bUser.getBpwd()));
        String rand = (String) session.getAttribute("rand");
        if (!rand.equalsIgnoreCase(bUser.getCode())) {
            model.addAttribute("errorMessage", "验证码错误!");
            return "user/login";
        }
        List<BUser> list = userRepository.login(bUser);
        if (list.size() > 0) {
            session.setAttribute("bUser", list.get(0));
            return "forward:/";
        } else {
            model.addAttribute("errorMessage", "用户名或密码错误!");
            return "user/login";
        }
    }
}
