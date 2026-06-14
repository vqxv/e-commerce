package com.ch.ebusiness.controller.admin;

import com.ch.ebusiness.NoLoginException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AdminBaseController {
    @ModelAttribute
    public void isLogin(HttpSession session) throws NoLoginException {
        if (session.getAttribute("auser") == null) {
            throw new NoLoginException("没有登录");
        }
    }
}
