package com.yuntai.sync.client.his;

import com.yuntai.sync.api.access.model.med.AccessDept;
import com.yuntai.sync.api.access.model.med.AccessDoc;
import com.yuntai.sync.api.enums.YesOrNo;
import com.yuntai.sync.client.his.util.HisResultBean;
import com.yuntai.sync.client.his.util.HisWebserviceUtil;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author jiabj@hsyuntai.com
 * @Description: 从HIS中获取医生列表
 * @Title: DoctorHelper
 * @Package com.yuntai.sync.client.his
 * @Copyright 版权归 Hundsun 所有
 * @date 2019/11/12 8:59
 */
public class DoctorHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorHelper.class);

    private static final String DOCTOR_URL = "/doctors";

    private static final String HOS_CODE = "T110481";

    public static Map<String, Set<String>> deptRDocMap = new HashMap<>();

    public static List<AccessDoc> getDocList(List<AccessDept> deptList, String hisWebserviceUrl) {
        List<AccessDoc> docList = new ArrayList<>();
        AccessDoc virtrualDoctor = null;
        for (AccessDept dept : deptList) {
            String[] deptIdArgs = dept.getAccessDeptId().split("\\|");
            String dept_code1 = deptIdArgs[0];
            String dept_code2 = deptIdArgs[1];
            // 将门诊虚拟成医生
            virtrualDoctor = new AccessDoc();
            virtrualDoctor.setAccessDocId(dept.getAccessDeptId());
            virtrualDoctor.setDocVirtualName(dept.getDeptName());
            virtrualDoctor.setDocName(dept.getDeptName());
            virtrualDoctor.setVirtualFlag(YesOrNo.YES.getCode());
            docList.add(virtrualDoctor);
            addToDeptRDocCache(dept.getAccessDeptId(), dept.getAccessDeptId());

            StringBuilder sb = new StringBuilder();
            sb.append("<hos_code>" + HOS_CODE + "</hos_code>");
            sb.append("<dept_code1>" + dept_code1 + "</dept_code1>");
            sb.append("<dept_code2>" + dept_code2 + "</dept_code2>");
            HisResultBean hisResultBean = HisWebserviceUtil.getResponse(hisWebserviceUrl + DOCTOR_URL, sb.toString(),
                    60);
            if (!hisResultBean.isSuccess()) {
                LOGGER.error("科室[{}]下没有获取到医生,HIS返回数据：{}", dept_code2, hisResultBean);
                continue;
            }
            Element outPut = hisResultBean.getResultEle();
            if (outPut == null) {
                LOGGER.error("科室[{}]下没有获取到医生", dept_code2);
                continue;
            }
            Iterator<Element> it = outPut.element("doctor_list").elementIterator("doctor");
            AccessDoc doctor = null;
            while (it.hasNext()) {
                Element ele = it.next();
                String title = ele.elementTextTrim("title");
                String doctorId = ele.elementTextTrim("code");
                String doctorName = ele.elementTextTrim("doctor_name");
                String sex = ele.elementTextTrim("sex");
                String speciality = ele.elementTextTrim("speciality");
                String price = ele.elementTextTrim("price");

                doctor = new AccessDoc();
                doctor.setAccessDocId(doctorId);
                doctor.setDocName(doctorName);
                doctor.setDocVirtualName(doctorName);
                // 0-女，1-男
                if ("1".equals(sex)) {
                    doctor.setSex(1);
                } else if ("2".equals(sex)) {
                    doctor.setSex(0);
                }
                doctor.setTitleShown(title);
                doctor.setGoodAt(speciality);
                doctor.setVirtualFlag(YesOrNo.NO.getCode());
                doctor.setMediLevel(getHisTitleToYuntai(title));
                docList.add(doctor);
                // 将医生与门诊科室的关系加入缓存
                addToDeptRDocCache(doctorId, dept.getAccessDeptId());
            }
        }
        return docList;
    }


    /**
     * 将医生和门诊的关系加入缓存
     *
     * @param docId
     * @param deptId
     */
    private static void addToDeptRDocCache(String docId, String deptId) {
        if (deptRDocMap.containsKey(docId)) {
            deptRDocMap.get(docId).add(deptId);
        } else {
            Set<String> deptIdSet = new HashSet<>();
            deptIdSet.add(deptId);
            deptRDocMap.put(docId, deptIdSet);
        }
    }

    private static Integer getHisTitleToYuntai(String hisTitleType) {
        //职称(枚举: 4主任医师, 3副主任医生, 2主治医师, 1住院医师 0医士)
        if ("主治医师".equals(hisTitleType)) {
            return 2;
        } else if ("副主任医师".equals(hisTitleType)) {
            return 3;
        } else if ("知名专家,教授".equals(hisTitleType)) {
            //doctor.setMediLevel(5);
        } else if ("主任医师".equals(hisTitleType)) {
            return 4;
        } else if ("住院医师".equals(hisTitleType)) {
            return 2;
        } else if ("医师".equals(hisTitleType)) {
            return 0;
        } else if ("特需门诊".equals(hisTitleType)) {
            //doctor.setMediLevel(6);
        }
        return null;
    }
}

