package com.ebusiness.controller;

import com.ebusiness.entity.User;
import com.ebusiness.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        String password = params.get("password");
        String confirmPassword = params.get("confirmPassword");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "邮箱不能为空"));
        }
        if (password == null || password.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "密码至少6位"));
        }
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of("error", "两次密码不一致"));
        }
        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "该邮箱已注册"));
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/user/current")
    public ResponseEntity<?> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(Map.of("error", "未登录"));
        }
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(Map.of("email", user.getEmail(), "role", user.getRole()));
    }

    @PutMapping("/user/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> params) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(Map.of("error", "未登录"));
        }
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        if (oldPwd == null || newPwd == null || newPwd.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "参数不合法"));
        }
        User user = (User) auth.getPrincipal();
        if (!passwordEncoder.matches(oldPwd, user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "原密码错误"));
        }
        user.setPassword(passwordEncoder.encode(newPwd));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
