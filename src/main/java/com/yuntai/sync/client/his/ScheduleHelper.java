package com.yuntai.sync.client.his;

import com.yuntai.sync.api.access.model.jyt.AccessDeptJyt;
import com.yuntai.sync.api.access.model.jyt.AccessSchJyt;
import com.yuntai.sync.client.his.util.HisResultBean;
import com.yuntai.sync.client.his.util.HisWebserviceUtil;
import com.yuntai.sync.client.his.util.YuntaiDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author jiabj@hsyuntai.com
 * @Description: 排班接口工具类
 * @Title: ScheduleHelper
 * @Package com.yuntai.sync.client.his
 * @Copyright 版权归 Hundsun 所有
 * @date 2019/11/13 11:06
 */
public class ScheduleHelper {
    private static final String HOS_CODE = "T107871";

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleHelper.class);

    private static final String SCHEDULE_URL = "/reg/list";

    public static List<AccessSchJyt> getScheduleList(List<AccessDeptJyt> deptList, Integer schDays, String hisWebserviceUrl) {
        List<AccessSchJyt> schList = new ArrayList<>();
        String from = YuntaiDateUtils.getFormatDate(new Date(), "yyyyMMdd");
        String to = YuntaiDateUtils.getSpecifiedDayAfter(new Date(), schDays, Calendar.DAY_OF_MONTH, "yyyyMMdd");
        for (AccessDeptJyt accessDept : deptList) {
            String[] deptIdArgs = accessDept.getAccessDeptId().split("\\|");
            String dept_code1 = deptIdArgs[0];
            String dept_code2 = deptIdArgs[1];
            StringBuilder sb = new StringBuilder();
            sb.append("<hos_code>" + HOS_CODE + "</hos_code>");
            sb.append("<dept_code1>" + dept_code1 + "</dept_code1>");
            sb.append("<dept_code2>" + dept_code2 + "</dept_code2>");
            sb.append("<doctor_code></doctor_code>");
            // 1普通挂号 2 转诊
            sb.append("<category>1</category>");
            sb.append("<begin_date>" + from + "</begin_date>");
            sb.append("<end_date>" + to + "</end_date>");
            HisResultBean hisResultBean = HisWebserviceUtil.getResponse(hisWebserviceUrl + SCHEDULE_URL,
                    sb.toString(), 60);
            if (!hisResultBean.isSuccess()) {
                LOGGER.error("科室[{}]没有获取到出诊信息", accessDept.getAccessDeptId());
                continue;
            }
            Element output = hisResultBean.getResultEle();
            if (output == null) {
                LOGGER.error("科室[{}]没有获取到出诊信息", accessDept.getAccessDeptId());
                continue;
            }
            Element day_views = output.element("day_views");
            if (day_views == null) {
                LOGGER.error("科室[{}]没有获取到出诊信息", accessDept.getAccessDeptId());
                continue;
            }
            List<Element> day_view_list = day_views.elements("day_view");
            for (Element dv : day_view_list) {
                String day = dv.elementTextTrim("day");
                Element registry_list = dv.element("registry_list");
                Iterator<Element> it = registry_list.elementIterator("registry");
                AccessSchJyt schedule = null;
                while (it.hasNext()) {
                    Element registry = it.next();
                    // 医院定制扩展字段，将会在锁号的时候传给 HIS 系统，可以 HIS 系统携带一些额外字段
                    String ext_param = registry.elementTextTrim("ext_param");
                    if (StringUtils.isBlank(ext_param)) {
                        continue;
                    }
                    String doctorCode = registry.elementTextTrim("doctor_code");
                    String titleType = registry.elementTextTrim("title_type");
                    String count = registry.elementTextTrim("count");
                    String title = registry.elementTextTrim("title");
                    String fee = registry.elementTextTrim("fee");
                    String reg_half = registry.elementTextTrim("reg_half");
                    String doctorName = registry.elementTextTrim("doctor");
                    String specialty = registry.elementTextTrim("specialty");
                    String remark = registry.elementTextTrim("remark");

                    //从his号源信息获取排班数据
                    schedule = new AccessSchJyt();
                    schedule.setSchDate(YuntaiDateUtils.parseDate(day, "yyyyMMdd"));
                    schedule.setAccessSchId(ext_param);
                    schedule.setAccessDeptId(accessDept.getAccessDeptId());
                    if (StringUtils.isNotBlank(doctorCode) && !"null".equals(doctorCode)) {
                        schedule.setAccessDocId(doctorCode);
                    } else {
                        schedule.setAccessDocId(accessDept.getAccessDeptId());
                        doctorCode = "NULL";
                    }
                    // 1 上午2 下午3 晚间
                    if ("1".equals(reg_half)) {
                        schedule.setDayType("1");
                        schedule.setStartTime(YuntaiDateUtils.parseDate("08:00:00", "HH:mm:ss"));
                        schedule.setEndTime(YuntaiDateUtils.parseDate("12:00:00", "HH:mm:ss"));
                    } else if ("2".equals(reg_half)) {
                        schedule.setDayType("2");
                        schedule.setStartTime(YuntaiDateUtils.parseDate("13:00:00", "HH:mm:ss"));
                        schedule.setEndTime(YuntaiDateUtils.parseDate("17:30:00", "HH:mm:ss"));
                    } else if ("3".equals(reg_half)) {
                        schedule.setDayType("4");
                        schedule.setStartTime(YuntaiDateUtils.parseDate("18:00:00", "HH:mm:ss"));
                        schedule.setEndTime(YuntaiDateUtils.parseDate("23:59:59", "HH:mm:ss"));
                    }
                    schedule.setSchLevel(title);
                    if (StringUtils.isNoneBlank(count)) {
                        schedule.setRemainNo(Integer.parseInt(count));
                        schedule.setResNo(Integer.parseInt(count));
                    } else {
                        schedule.setRemainNo(0);
                        schedule.setResNo(0);
                    }
                    if (StringUtils.isNotBlank(fee)) {
                        BigDecimal bd = new BigDecimal(fee);
                        schedule.setRegFee(bd);
                    } else {
                        schedule.setRegFee(new BigDecimal("0.00"));
                    }
                    schedule.setServiceFee(new BigDecimal("0.0"));
                    // 排班号源类型 0无需号源/不支持号源 1余号 2时间段 3余号+时间段
                    schedule.setNumSrcType(0);
                    // 是否专家号 0 不是 1 是
                    schedule.setIsExpert(1);
                    String fb1 =
                            day + "|" + reg_half + "|" + titleType + "|" + dept_code1 + "|" + dept_code2 + "|" + doctorCode;
                    schedule.setFb1(accessDept.getAccessDeptId());
                    schList.add(schedule);
                }
            }
        }
        return schList;
    }
}
