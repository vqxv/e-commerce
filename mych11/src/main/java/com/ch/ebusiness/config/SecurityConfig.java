package com.ch.ebusiness.config;

import com.ch.ebusiness.entity.User;
import com.ch.ebusiness.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                    .build();
        };
    }

    @Bean
    public AuthenticationSuccessHandler adminAuthSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                response.sendRedirect("/admin/dashboard");
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler userAuthSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String role = authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .filter(a -> a.startsWith("ROLE_"))
                    .map(a -> a.substring(5))
                    .findFirst().orElse("USER");
                if ("ADMIN".equals(role)) {
                    response.sendRedirect("/admin/dashboard");
                } else {
                    response.sendRedirect("/");
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http, @Qualifier("adminAuthSuccessHandler") AuthenticationSuccessHandler adminSuccessHandler) throws Exception {
        http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/toLogin", "/admin/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
            )
            .formLogin(form -> form
                .loginPage("/admin/toLogin")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("email")
                .successHandler(adminSuccessHandler)
                .failureUrl("/admin/toLogin?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/toLogin")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public SecurityFilterChain userFilterChain(HttpSecurity http, @Qualifier("userAuthSuccessHandler") AuthenticationSuccessHandler userSuccessHandler) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/validateCode").permitAll()
                .requestMatchers("/", "/index", "/goodsDetail", "/search").permitAll()
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/cart/add", "/cart/focus", "/cart/delete/**", "/cart/clear", "/cart/submitOrder", "/cart/pay/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/cart/view", "/cart/myFocus", "/cart/myOrders", "/cart/orderDetail/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/user/toLogin")
                .loginProcessingUrl("/user/login")
                .usernameParameter("email")
                .successHandler(userSuccessHandler)
                .failureUrl("/user/toLogin?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}