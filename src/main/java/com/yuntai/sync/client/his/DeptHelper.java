package com.yuntai.sync.client.his;

import com.yuntai.sync.api.access.model.jyt.AccessDeptJyt;
import com.yuntai.sync.client.his.util.HisResultBean;
import com.yuntai.sync.client.his.util.HisWebserviceUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jiabj@hsyuntai.com
 * @Description: 获取HIS的门诊科室方法类
 * @Title: SectHelper
 * @Package com.yuntai.sync.client.his
 * @Copyright 版权归 Hundsun 所有
 * @date 2019/9/16 19:33
 */
public class DeptHelper {

    public static final String DEPT_URL = "/depts";

    private static final String HOS_CODE = "T107871";

    public static List<AccessDeptJyt> getDeptListFromHos(String hisWebserviceUrl) {
        String requestXML = "<hos_code>" + HOS_CODE + "</hos_code>";
        HisResultBean hisResultBean = HisWebserviceUtil.getResponse(hisWebserviceUrl + DEPT_URL, requestXML, 30);
        if (!hisResultBean.isSuccess()) {
            return null;
        }
        Element outPut = hisResultBean.getResultEle();
        if (outPut == null) {
            return null;
        }
        Iterator<Element> it = outPut.element("departlist").elementIterator("depart");
        List<AccessDeptJyt> deptList = new ArrayList<>();
        AccessDeptJyt dept = null;
        while (it.hasNext()) {
            Element ele = it.next();
            dept = new AccessDeptJyt();
            String pcode = ele.elementTextTrim("pcode");
//            if ("0".equals(pcode)) {
//                continue;
//            }
            String code = ele.elementTextTrim("code");
            String name = ele.elementTextTrim("name");
            String remark = ele.elementTextTrim("remark");
            String desc = ele.elementTextTrim("desc");
            String address = ele.elementTextTrim("address");
            String index = ele.elementTextTrim("index");

            dept = new AccessDeptJyt();
            dept.setAccessDeptId(code);
            dept.setDeptName(name);
            dept.setRemark(remark);
            dept.setResume(desc);
            dept.setAddr(address);
            if (StringUtils.isNotBlank(index)) {
                dept.setSort(Long.parseLong(index));
            }
            // 过滤掉一级门诊，一级为不挂号的科室,取二级门诊
            deptList.add(dept);
        }
        return deptList;
    }

    public static List<AccessDeptJyt> mock(){
        AccessDeptJyt dept = new AccessDeptJyt();
        List<AccessDeptJyt> list = new ArrayList<>();
        dept.setAccessDeptId("100");
        dept.setDeptName("内科");
        list.add(dept);
        return list;
    }
}
