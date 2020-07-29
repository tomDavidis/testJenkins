package com.yuntai.sync.client.his.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public class YuntaiDateUtils {

    private static final Logger logger = LoggerFactory.getLogger(YuntaiDateUtils.class);
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    //该部分为线程不安全的处理代码,此处需要避免这种异常的情况,因此屏蔽该时间转换的工具类
    public static final String FORMAT_DAY_PATTERN = "yyyy-MM-dd";
//
//	public final static SimpleDateFormat FORMAT_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//	public final static SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//	public final static SimpleDateFormat FORMAT_HOUR = new SimpleDateFormat("yyyy-MM-dd HH");
//
//	public static final SimpleDateFormat FORMAT_DAY = new SimpleDateFormat(FORMAT_DAY_PATTERN);
//
//	public final static SimpleDateFormat FORMAT_DIR = new SimpleDateFormat("yyyy/MM/dd/");
//
//	public final static SimpleDateFormat FORMAT_OPTMONTH = new SimpleDateFormat("yyyyMM");



    /**
     * 将时间转换为想要的时间格式String
     * @param date
     * @param format
     * @return
     */
    public static String dateFormatToString(Date date, String format) {
        if( StringUtils.isBlank(format) ){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormatToString(date,dateFormat);
    }

    /**
     * 将时间转换为想要的时间格式String
     * @param date
     * @param format
     * @return
     */
    public static String dateFormatToString(Date date, SimpleDateFormat format) {
        if (date == null || format==null ) {
            return null;
        }
        return format.format(date);
    }

    /**
     * 将时间String转换为想要的时间
     * @param date
     * @param format
     * @return
     */
    public static Date stringParaseToDate(String date, String format) {
        if (date == null || date.equals("")) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return stringParaseToDate(date,dateFormat);
    }

    /**
     * 将时间String转换为想要的时间
     * @param date
     * @param format
     * @return
     */
    public static Date stringParaseToDate(String date, SimpleDateFormat format) {
        if ( StringUtils.isBlank(date) || format == null ) {
            return null;
        }
        try {
            Date result = format.parse(date);
            return result;
        } catch (Exception e) {
            logger.error("日期字符串转换出错!", e);
        }
        return null;
    }

    /**
     * 通过yyyy-MM-dd 样式的字符串得到一个星期中的读几天
     * 2,3,4,5,6,7,1
     * 1,2,3,4,5,6,7
     */
    public static Integer changeDateToDayType(String str) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatDay = new SimpleDateFormat(FORMAT_DAY_PATTERN);
        Date date = stringParaseToDate(str, formatDay);
        c.setTime(date);
        Integer in = c.get(Calendar.DAY_OF_WEEK);
        switch (in) {
            case 1:
                in = 7;
                break;
            case 2:
                in = 1;
                break;
            case 3:
                in = 2;
                break;
            case 4:
                in = 3;
                break;
            case 5:
                in = 4;
                break;
            case 6:
                in = 5;
                break;
            case 7:
                in = 6;
                break;
        }
        return in;
    }

    /**
     * 获取星期值
     * @param str
     * @return
     */
    public static Integer convantDateToDayType(String str){
        Calendar c=Calendar.getInstance();
        SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=stringParaseToDate(str, formatTimestamp);
        c.setTime(date);
        Integer in =c.get(Calendar.DAY_OF_WEEK);
        switch (in) {
            case 1:
                in = 7;
                break;
            case 2:
                in = 1;
                break;
            case 3:
                in = 2;
                break;
            case 4:
                in = 3;
                break;
            case 5:
                in = 4;
                break;
            case 6:
                in = 5;
                break;
            case 7:
                in = 6;
                break;
        }
        return in;
    }

    /**
     * 时间String比较
     * @param d1   日期1
     * @param d2   日期2
     * @return 比较结果  （1表示 日期1>日期2）、（0表示日期1=日期2 ）、（-1表示 日期1<日期2）
     */
    public static int compare2Dates(String d1,String d2) throws ParseException{
        return compare2DatesByPattern(d1,d2,FORMAT_DAY_PATTERN);
    }

    /**
     * 判断是不是传入的时间是不是当日的时间
     *@param date 日期
     *@return 比较结果 如果相等就放回true 的
     */
    public static boolean compare(String date) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String now = df.format(new Date()); // 获取当前的时间
        if (YuntaiDateUtils.compare2Dates(now, date) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 按照指定时间格式比较两个时间
     * @param d1   日期1
     * @param d2   日期2
     * @param pattern 时间模式
     * @return 比较结果  （1表示 日期1>日期2）、（0表示日期1=日期2 ）、（-1表示 日期1<日期2）
     */
    public static int compare2DatesByPattern(String d1,String d2,String pattern) {
        try {
            DateFormat df=new SimpleDateFormat(pattern); // HH:mm:ss
            Calendar c1=Calendar.getInstance();
            Calendar c2=Calendar.getInstance();
            c1.setTime(df.parse(d1));
            c2.setTime(df.parse(d2));
            int result=c1.compareTo(c2);
            return result;
        } catch (ParseException e) {
            logger.error("日期比较失败", e);
        }
        return 0;
    }

    /**
     * 根据pattern判断字符串是否为合法日期
     * @param dateStr
     * @param pattern
     * @return
     */
    public static boolean isValidDate(String dateStr, String pattern) {
        boolean isValid = false;
//		String patterns = "yyyy-MM-dd,MM/dd/yyyy";

        if (pattern == null || pattern.length() < 1) {
            pattern = "yyyy-MM-dd";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            // sdf.setLenient(false);
            String date = sdf.format(sdf.parse(dateStr));
            if (date.equalsIgnoreCase(dateStr)) {
                isValid = true;
            }
        } catch (Exception e) {
            isValid = false;
        }
        // 如果目标格式不正确，判断是否是其它格式的日期
        if (!isValid) {
            isValid = isValidDatePatterns(dateStr, "");
        }
        return isValid;
    }

    /**
     * 判断时间string格式数据是否满足时间格式 yyyy-MM-dd;dd/MM/yyyy;yyyy/MM/dd;yyyy/M/d h:mm
     * @param dateStr
     * @param patterns
     * @return
     */
    public static boolean isValidDatePatterns(String dateStr, String patterns) {
        if (patterns == null || patterns.length() < 1) {
            patterns = "yyyy-MM-dd;dd/MM/yyyy;yyyy/MM/dd;yyyy/M/d h:mm";
        }
        boolean isValid = false;
        String[] patternArr = patterns.split(";");
        for (int i = 0; i < patternArr.length; i++) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(patternArr[i]);
                // sdf.setLenient(false);
                String date = sdf.format(sdf.parse(dateStr));
                if (date.equalsIgnoreCase(dateStr)) {
                    isValid = true;
                    break;
                }
            } catch (Exception e) {
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * 将时间string 形式转换为Date形式
     * @param date
     * @param format SimpleDateFormat
     * @return
     */
    public static String getFormatDate(Date date, SimpleDateFormat format) {
        if (format == null) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        try {
            String strDate = format.format(date);
            return strDate;
        } catch (Exception e) {
            logger.error("日期格转换失败::::::::", e);
        }
        return null;
    }

    /**
     * 将时间string 形式转换为Date形式
     * @param strDate
     * @param pattern String
     * @return
     */
    public static Date parseDate(String strDate, String pattern) {
        pattern = StringUtils.isNotBlank(pattern) ? pattern : DEFAULT_PATTERN;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date nowDate = sdf.parse(strDate);
            return nowDate;
        } catch (Exception e) {
            logger.error(strDate + "日期解析失败！", e);
        }
        return null;
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     * @param pattern
     * @return
     */
    public static Date getNowDate(String pattern) {
        pattern = StringUtils.isNotBlank(pattern) ? pattern : DEFAULT_PATTERN;
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String strDate = sdf.format(date);
            Date nowDate = sdf.parse(strDate);
            return nowDate;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取当前时间失败", e);
        }
        return null;
    }

    /**
     * 获取月份
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        SimpleDateFormat formatOptmonth = new SimpleDateFormat("yyyyMM");
        if( date!=null ){
            String changedayStr = formatOptmonth.format(date);
            if( StringUtils.isNotBlank(changedayStr)){
                return Integer.parseInt(changedayStr);
            }
        }
        return null;
    }

    /**
     * 获取星期
     * @param date
     * @return
     */
    public static Integer getWeek(Date date) {
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        String dateString = dateFm.format(date);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("星期一", 1);
        map.put("星期二", 2);
        map.put("星期三", 3);
        map.put("星期四", 4);
        map.put("星期五", 5);
        map.put("星期六", 6);
        map.put("星期日", 7);
        return map.containsKey(dateString)?map.get(dateString):null;
    }

    /**
     * 获取指定日期
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

    public static String getSpecifiedDayAfter(Date specifiedDay,int num ,int type,String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        c.add(type, num);

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(c.getTime());
    }

    /**
     * 判断传入的时间是否距离当前系统时间五分钟
     * 超时为真,未超时为假
     * @param specifiedDay
     * @param num
     * @return
     */
    public static boolean getSpecifiedMinuteAfter(Date createTime, int num) {
        if( createTime!=null ){
            Date currentTime = new Date();
            if( currentTime.getTime()>createTime.getTime() ){
                if( (currentTime.getTime()-createTime.getTime())/1000.0/60 >=num ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将传入日期转换成制定格式的string
     * @param dateStr
     * @param pattern
     * @return
     */
    public static String getFormatDate(String dateStr, String pattern) {
        pattern = StringUtils.isNotBlank(pattern) ? pattern : DEFAULT_PATTERN;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String date = format.format(sdf.parse(dateStr));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将传入日期转换成制定格式的string
     * @param date
     * @param pattern
     * @return
     */
    public static String getFormatDate(Date date, String pattern) {
        pattern = StringUtils.isNotBlank(pattern) ? pattern : DEFAULT_PATTERN;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String strDate = sdf.format(date);
        return strDate;
    }

    public static void main(String[] args) {
//		for (int i = 0; i < 7; i++) {
        System.out.println(getSpecifiedDayAfter("2015-11-3",15));
//		}
        try {
            int compareTo = compare2Dates("2015-11-1 11:22:33", "2015-1-10 11:32:33");
            System.out.println(compareTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
