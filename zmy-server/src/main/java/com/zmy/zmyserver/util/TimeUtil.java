package com.zmy.zmyserver.util;

import java.util.Calendar;

public class TimeUtil {


    public static long today() {


        Calendar calendar = Calendar.getInstance();

        // 将时、分、秒和毫秒设置为最小值
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取当前日期的第一毫秒
        return calendar.getTimeInMillis();
    }

    public static void main(String[] args) {
        System.out.println(today());
    }
}
