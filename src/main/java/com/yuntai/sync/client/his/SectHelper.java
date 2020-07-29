//package com.yuntai.sync.client.his;
//
//import com.yuntai.sync.api.access.model.AccessDept;
//import com.yuntai.sync.client.his.util.HisResultBean;
//import com.yuntai.sync.client.his.util.HisWebserviceUtil;
//import com.yuntai.sync.client.his.util.YuntaiDateUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.dom4j.Element;
//
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * @author jiabj@hsyuntai.com
// * @Description: 获取HIS的门诊科室方法类
// * @Title: SectHelper
// * @Package com.yuntai.sync.client.his
// * @Copyright 版权归 Hundsun 所有
// * @date 2019/9/16 19:33
// */
//public class SectHelper {
//
//    private static final String HOS_CODE = "T110481";
//
//    public static final String DEPT_URL = "/depts";
//
//    public static List<AccessDept> getSectListFromHos(String hisWebserviceUrl) {
//        String requestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><request>" +
//                "<time_stamp>{0}</time_stamp><input><hos_code>{1}</hos_code></input></request>";
//        requestXML = MessageFormat.format(requestXML, YuntaiDateUtils.getFormatDate(new Date(), "yyyyMMddHHmmssSSS"),
//                HOS_CODE);
//        HisResultBean hisResultBean = HisWebserviceUtil.getResponse(hisWebserviceUrl + DEPT_URL, requestXML, 30);
//        if (!hisResultBean.isSuccess()) {
//            return null;
//        }
//        Element outPut = hisResultBean.getResultEle();
//        if (outPut == null) {
//            return null;
//        }
//        Iterator<Element> it = outPut.element("departlist").elementIterator("depart");
//        List<AccessDept> deptList = new ArrayList<>();
//        AccessDept dept = null;
//        while (it.hasNext()) {
//            Element ele = it.next();
//            dept = new AccessDept();
//            String code = ele.elementTextTrim("code");
//            String name = ele.elementTextTrim("name");
//            String pcode = ele.elementTextTrim("pcode");
//            String remark = ele.elementTextTrim("remark");
//            String desc = ele.elementTextTrim("desc");
//            String address = ele.elementTextTrim("address");
//            String index = ele.elementTextTrim("index");
//
//            // 过滤掉一级门诊，一级为不挂号的科室
//            if ("0".equals(pcode)) {
//                continue;
//            }
//            dept = new AccessDept();
//            dept.setAccessDeptId(pcode + "-" + code);
//            dept.setDeptName(name);
//            dept.setRemark(remark);
//            dept.setResume(desc);
//            dept.setAddr(address);
//            if (StringUtils.isNotBlank(index)) {
//                dept.setSort(Long.parseLong(index));
//            }
//            deptList.add(dept);
//        }
//        return deptList;
//    }
//}
