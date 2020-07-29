package com.yuntai.sync.client.his;

import com.yuntai.sync.api.access.model.jyt.AccessDeptJyt;
import com.yuntai.sync.api.access.model.jyt.AccessDviJyt;
import com.yuntai.sync.client.his.util.HisResultBean;
import com.yuntai.sync.client.his.util.HisWebserviceUtil;
import com.yuntai.sync.client.his.util.YuntaiDateUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author jiabj@hsyuntai.com
 * @Description: 排班接口工具类
 * @Title: ScheduleHelper
 * @Package com.yuntai.sync.client.his
 * @Copyright 版权归 Hundsun 所有
 * @date 2019/11/13 11:06
 */
public class DviHelper {
    private static final String HOS_CODE = "T107871";

    private static final Logger logger = LoggerFactory.getLogger(DoctorHelper.class);

    private static final String DVI_URL = "/doctors/schedule";

    public static List<AccessDviJyt> getDviList(List<AccessDeptJyt> deptList, Integer schDays, String hisWebserviceUrl) {
        List<AccessDviJyt> dviList = new ArrayList<>();
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
            sb.append("<begin_date>" + from + "</begin_date>");
            sb.append("<end_date>" + to + "</end_date>");
            HisResultBean hisResultBean = HisWebserviceUtil.getResponse(hisWebserviceUrl + DVI_URL,
                    sb.toString(), 60);
            if (!hisResultBean.isSuccess()) {
                logger.error("科室[{}]没有获取到出诊信息", accessDept.getAccessDeptId());
                continue;
            }
            Element output = hisResultBean.getResultEle();
            if (output == null) {
                logger.error("科室[{}]没有获取到出诊信息", accessDept.getAccessDeptId());
                continue;
            }
            Element doctor_list = output.element("doctor_list");
            if (doctor_list == null) {
                logger.error("科室[{}]没有获取到出诊信息", accessDept.getAccessDeptId());
                continue;
            }
            List<Element> doctors = doctor_list.elements("doctor");
            for (Element doctor : doctors) {
                String doctorCode = doctor.elementTextTrim("code");
                String doctorName = doctor.elementTextTrim("name");
                String title = doctor.elementTextTrim("title");
                Element date_list = doctor.element("date_list");
                if (date_list == null) {
                    continue;
                }
                Iterator<Element> it = date_list.elementIterator("date");
                AccessDviJyt accessDvi = null;
                while (it.hasNext()) {
                    Element element = it.next();
                    String reg_date = element.elementTextTrim("date");
                    String reg_half = element.elementTextTrim("type");

                    //从his号源信息获取出诊信息
                    accessDvi = new AccessDviJyt();
                    accessDvi.setDocName(doctorName);
                    accessDvi.setAccessDocId(doctorCode);
                    accessDvi.setAccessDeptId(accessDept.getAccessDeptId());
                    // 1 上午2 下午3 晚间
                    if ("1".equals(reg_half)) {
                        accessDvi.setDayType("1");
                    } else if ("2".equals(reg_half)) {
                        accessDvi.setDayType("2");
                    } else if ("3".equals(reg_half)) {
                        accessDvi.setDayType("4");
                    }
                    String fb1 =
                            reg_date + "|" + reg_half + "|"  + dept_code1 + "|" + dept_code2 + "|" + doctorCode ;
                    accessDvi.setAccessVisitId(fb1);
                    accessDvi.setVisitDate(YuntaiDateUtils.parseDate(reg_date, "yyyyMMdd"));
                    accessDvi.setMediLevel(getMidiType(title));
                    dviList.add(accessDvi);
                }
            }
        }
        return dviList;
    }

    private static Integer getMidiType(String hisType) {
        //枚举:6	特需门诊 5知名专家教授 4主任医师, 3副主任医生, 2主治医师, 1普通门诊
        if ("主治医师".equals(hisType)) {
            return 2;
        } else if ("副主任医师".equals(hisType)) {
            return 3;
        } else if ("知名专家,教授".equals(hisType)) {
            return 5;
        } else if ("主任医师".equals(hisType)) {
            return 4;
        } else if ("住院医师".equals(hisType)) {
            return 2;
        } else if ("医师".equals(hisType)) {
            return 1;
        } else if ("特需门诊".equals(hisType)) {
           return 6;
        }
        return null;
    }

}
