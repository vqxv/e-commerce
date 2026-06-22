package com.ch.ebusiness.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
public class ValidateCodeController {

    @GetMapping("/validateCode")
    public void getCode(HttpSession session, HttpServletResponse response) throws IOException {
        // 生成四位随机数字
        String code = String.format("%04d", new Random().nextInt(10000));
        session.setAttribute("rand", code);

        int width = 80, height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 干扰线
        Random rand = new Random();
        g.setColor(new Color(200, 200, 200));
        for (int i = 0; i < 4; i++) {
            int x1 = rand.nextInt(width), y1 = rand.nextInt(height);
            int x2 = rand.nextInt(width), y2 = rand.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制数字
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.setColor(new Color(20, 90, 160));
        g.drawString(code, 10, 22);
        g.dispose();

        response.setContentType("image/jpeg");
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }
}
