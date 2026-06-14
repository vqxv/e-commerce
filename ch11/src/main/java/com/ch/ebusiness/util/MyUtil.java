package com.ch.ebusiness.util;

import com.ch.ebusiness.entity.BUser;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MyUtil {
    public static String getNewFileName(String oldFileName) {
        int lastIndex = oldFileName.lastIndexOf(".");
        String suffix = oldFileName.substring(lastIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStr = sdf.format(new Date());
        Random random = new Random();
        int randNum = random.nextInt(900) + 100;
        return timeStr + randNum + suffix;
    }

    public static BUser getUser(HttpSession session) {
        return (BUser) session.getAttribute("bUser");
    }
}
