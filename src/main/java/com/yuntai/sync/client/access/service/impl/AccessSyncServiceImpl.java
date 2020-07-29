package com.yuntai.sync.client.access.service.impl;

import com.yuntai.sync.api.access.model.AccessData;
import com.yuntai.sync.api.access.model.jyt.*;
import com.yuntai.sync.api.enums.DataModeType;
import com.yuntai.sync.api.enums.jyt.*;
import com.yuntai.sync.client.access.service.jyt.AccessJytSyncService;
import com.yuntai.sync.client.his.DeptHelper;
import com.yuntai.sync.client.his.DoctorHelper;
import com.yuntai.sync.client.his.OltScheduleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 定时同步客户端接口实现类
 * @ClassName: AccessSyncServiceImpl
 * @Package: com.yuntai.sync.client.access.service.impl
 * @Author: qiufeng@hsyuntai.com
 * @Date: 2018/7/3 20:01
 * @Copyright: 版权归 HSYUNTAI 所有
 */
@Service("AccessncServiceImpl")
public class AccessSyncServiceImpl implements AccessJytSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessSyncServiceImpl.class);

    @Value("${his.webservice.url}")
    private String hisWebserviceUrl;

    @Value("${sync.sch.days}")
    private Integer syncSchDays;

    // 科室缓存
    private List<AccessDeptJyt> tempDeptList;

    @Override
    public AccessData<AccessDeptJyt, UpdateDeptJytField> getDeptJytFromAccess(Long hosId) {
//        tempDeptList = DeptHelper.getDeptListFromHos(hisWebserviceUrl);
        tempDeptList = DeptHelper.mock();
        AccessData<AccessDeptJyt, UpdateDeptJytField> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
        accessData.setTList(tempDeptList);
        accessData.setUpdateFieldList(Arrays.asList(new UpdateDeptJytField[]{UpdateDeptJytField.deptName,
                UpdateDeptJytField.sort, UpdateDeptJytField.addr}));
        return accessData;
    }

    @Override
    public AccessData<AccessDocJyt, UpdateDocJytField> getDocJytFromAccess(Long hosId) {
        AccessData<AccessDocJyt, UpdateDocJytField> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
//        accessData.setTList(DoctorHelper.getDocList(tempDeptList, hisWebserviceUrl));
        accessData.setTList(DoctorHelper.mock());
        accessData.setUpdateFieldList(Arrays.asList(new UpdateDocJytField[]{UpdateDocJytField.docVirtualName,
                UpdateDocJytField.docVirtualName, UpdateDocJytField.mediLevel, UpdateDocJytField.goodAt,
                UpdateDocJytField.titleShown}));
        return accessData;
    }

    @Override
    public AccessData<AccessDeptRDocJyt, Enum> getDeptRDocJytFromAccess(Long hosId) {
//        if (DoctorHelper.deptRDocMap.isEmpty()) {
//            return null;
//        }
        List<AccessDeptRDocJyt> accessDeptRDocList = new ArrayList<>();
        AccessDeptRDocJyt deptRDoc = null;
//        for (String doctorId : DoctorHelper.deptRDocMap.keySet()) {
//            for (String deptId : DoctorHelper.deptRDocMap.get(doctorId)) {
//                deptRDoc = new AccessDeptRDocJyt();
//                deptRDoc.setAccessDeptId(deptId);
//                deptRDoc.setAccessDocId(doctorId);
//                accessDeptRDocaccessDocId = "200"List.add(deptRDoc);
//            }
//        }
        deptRDoc = new AccessDeptRDocJyt();
        deptRDoc.setAccessDeptId("100");
        deptRDoc.setAccessDocId("200");
        accessDeptRDocList.add(deptRDoc);
        AccessData<AccessDeptRDocJyt, Enum> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
        accessData.setTList(accessDeptRDocList);
        return accessData;
    }

    @Override
    public AccessData<AccessSchJyt, UpdateSchJytField> getSchJytFromAccess(Long hosId) {
        AccessData<AccessSchJyt, UpdateSchJytField> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
//        accessData.setTList(ScheduleHelper.getScheduleList(this.tempDeptList, this.syncSchDays, hisWebserviceUrl));
        accessData.setTList(null);
        accessData.setUpdateFieldList(Arrays.asList(UpdateSchJytField.values()));
        return accessData;
    }

    @Override
    public AccessData<AccessDviJyt, UpdateDviJytField> getDviJytFromAccess(Long hosId) {
        AccessData<AccessDviJyt, UpdateDviJytField> accessData = new AccessData<>();
        try {
            accessData.setDataModeType(DataModeType.ALL);
            accessData.setTList(null);
//   accessData.settList(DviHelper.getDviList(tempDeptList, syncSchDays, hisWebserviceUrl));
            accessData.setUpdateFieldList(Arrays.asList(UpdateDviJytField.values()));
        } finally {
            tempDeptList = null;
        }
        return accessData;
    }

    @Override
    public AccessData<AccessOltSchJyt, UpdateOltSchJytField> getOltSchJytFromAccess(Long aLong) {
        AccessData<AccessOltSchJyt, UpdateOltSchJytField> accessData = new AccessData<>();
        try {
            accessData.setDataModeType(DataModeType.ALL);
            accessData.setTList(OltScheduleHelper.mock());
            accessData.setUpdateFieldList(Arrays.asList(UpdateOltSchJytField.values()));
        } finally {
            tempDeptList = null;
        }
        return accessData;
    }
}
