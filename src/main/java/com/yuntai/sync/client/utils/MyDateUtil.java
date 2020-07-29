package com.yuntai.sync.client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
* @Description: 日期工具类
* @Title: MyDateUtil
* @Package com.yuntai.sync.client.utils
* @Copyright 版权归 Hundsun 所有
* @author jiabj@hsyuntai.com
* @date 2019/9/17 11:20
*/
public class MyDateUtil {

    public static final String FORMAT_DAY_PATTERN = "yyyy-MM-dd";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取指定日期
     *
     * @param specifiedDay
     * @param num
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay, int num) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        String dayAfter = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            c.setTime(date);
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day + num);

            dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayAfter;
    }

    /**
     *  字符串转日期
     * @param stringDate
     * @return
     */
    public static Date stringChangeDate(String stringDate){

        Date date = new Date();
        try {
            date = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date ;
    }
}
