package com.ch.ebusiness;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandleController {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
        String message = "";
        if (e instanceof SQLException) {
            message = "数据库异常: " + e.getMessage();
        } else if (e instanceof NoLoginException) {
            message = "未登录异常";
        } else {
            message = "未知异常: " + e.getClass().getName() + " - " + e.getMessage();
        }
        // 打印堆栈跟踪到控制台
        e.printStackTrace();
        
        // 如果是AJAX请求，返回错误信息
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        if ((requestedWith != null && requestedWith.contains("XMLHttpRequest"))
                || (accept != null && accept.contains("application/json"))) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"error\":\"" + message + "\"}");
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("mymessage", message);
        mav.addObject("stackTrace", e.getStackTrace());
        mav.setViewName("myError");
        return mav;
    }
}
