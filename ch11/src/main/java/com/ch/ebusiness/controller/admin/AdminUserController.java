package com.ch.ebusiness.controller.admin;

import com.ch.ebusiness.service.UserAndOrderAndOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminUser")
public class AdminUserController extends AdminBaseController {

    @Autowired
    private UserAndOrderAndOutService userAndOrderAndOutService;

    @RequestMapping("/selectUser")
    public String selectUser(Model model, int currentPage) {
        return userAndOrderAndOutService.selectUser(model, currentPage);
    }
}
