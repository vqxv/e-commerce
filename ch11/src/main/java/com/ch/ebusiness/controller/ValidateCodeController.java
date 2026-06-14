package com.ch.ebusiness.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
public class ValidateCodeController {

    @RequestMapping("/validateCode")
    public void validateCode(HttpSession session, HttpServletResponse response) throws IOException {
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawString(rand, 16 * i + 6, 22);
        }
        session.setAttribute("rand", sRand);
        g.dispose();
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }
}
