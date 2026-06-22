package com.ch.ebusiness.controller;

import com.ch.ebusiness.entity.User;
import com.ch.ebusiness.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 构造函数注入
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 登录页面
    @GetMapping("/user/toLogin")
    public String toLogin() {
        return "user/login";
    }

    // 注册页面
    @GetMapping("/user/toRegister")
    public String toRegister() {
        return "user/register";
    }

    // 注册提交
    @PostMapping("/user/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam String code,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        // 1. 验证码检查
        String rand = (String) session.getAttribute("rand");
        if (rand == null || !rand.equalsIgnoreCase(code)) {
            redirectAttributes.addFlashAttribute("error", "验证码错误");
            return "redirect:/user/toRegister";
        }
        // 2. 密码一致性
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "两次密码不一致");
            return "redirect:/user/toRegister";
        }
        // 3. 邮箱唯一性
        if (userRepository.findByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("error", "该邮箱已被注册");
            return "redirect:/user/toRegister";
        }
        // 4. 创建用户
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);
        // 5. 清除验证码
        session.removeAttribute("rand");
        redirectAttributes.addFlashAttribute("success", "注册成功，请登录");
        return "redirect:/user/toLogin";
    }

    // 邮箱唯一性检查
    @PostMapping("/user/isUse")
    @ResponseBody
    public String isUse(@RequestParam String email) {
        return userRepository.findByEmail(email) == null ? "ok" : "no";
    }

    // 个人信息页
    @GetMapping("/user/userInfo")
    public String userInfo(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/user/toLogin";
        }
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("currentUser", user);
        return "user/userInfo";
    }

    // 修改密码
    @PostMapping("/user/updatePwd")
    @ResponseBody
    public String updatePwd(@RequestParam String oldPwd,
                            @RequestParam String newPwd,
                            Principal principal) {
        if (principal == null) {
            return "请先登录";
        }
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            return "用户不存在";
        }
        if (!passwordEncoder.matches(oldPwd, user.getPassword())) {
            return "原密码错误";
        }
        if (newPwd == null || newPwd.length() < 6) {
            return "新密码长度不能少于6位";
        }
        user.setPassword(passwordEncoder.encode(newPwd));
        userRepository.save(user);
        return "ok";
    }
}
