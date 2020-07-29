package com.yuntai.sync.client.access.service.impl;

import com.yuntai.sync.api.access.model.*;
import com.yuntai.sync.api.access.model.med.*;
import com.yuntai.sync.api.enums.*;
import com.yuntai.sync.api.enums.med.*;
import com.yuntai.sync.client.his.DeptHelper;
import com.yuntai.sync.client.his.DoctorHelper;
import com.yuntai.sync.client.his.ScheduleHelper;
import com.yuntai.sync.server.access.service.AccessSyncService;
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
@Service("AccessSyncServiceImpl")
public class AccessSyncServiceImpl implements AccessSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessSyncServiceImpl.class);

    @Value("${his.webservice.url}")
    private String hisWebserviceUrl;

    @Value("${sync.sch.days}")
    private Integer syncSchDays;

    // 科室缓存
    private List<AccessDept> tempDeptList;

    @Override
    public AccessData<AccessDept, UpdateDeptField> getDeptFromAccess(Long hosId) {
        tempDeptList = DeptHelper.getDeptListFromHos(hisWebserviceUrl);
        AccessData<AccessDept, UpdateDeptField> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
        accessData.setTList(tempDeptList);
        accessData.setUpdateFieldList(Arrays.asList(new UpdateDeptField[]{UpdateDeptField.deptName,
                UpdateDeptField.sort, UpdateDeptField.addr}));
        return accessData;
    }

    @Override
    public AccessData<AccessDoc, UpdateDocField> getDocFromAccess(Long hosId) {
        AccessData<AccessDoc, UpdateDocField> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
        accessData.setTList(DoctorHelper.getDocList(tempDeptList, hisWebserviceUrl));
        accessData.setUpdateFieldList(Arrays.asList(new UpdateDocField[]{UpdateDocField.dcode,
                UpdateDocField.v, UpdateDocField.mediLevel, UpdateDocField.goodAt,
                UpdateDocField.titleShown}));
        return accessData;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AccessData<AccessDeptRDoc, Enum> getDeptRDocFromAccess(Long hosId) {
        if (DoctorHelper.deptRDocMap.isEmpty()) {
            return null;
        }
        List<AccessDeptRDoc> accessDeptRDocList = new ArrayList<>();
        AccessDeptRDoc deptRDoc = null;
        for (String doctorId : DoctorHelper.deptRDocMap.keySet()) {
            for (String deptId : DoctorHelper.deptRDocMap.get(doctorId)) {
                deptRDoc = new AccessDeptRDoc();
                deptRDoc.setAccessDeptId(deptId);
                deptRDoc.setAccessDocId(doctorId);
                accessDeptRDocList.add(deptRDoc);
            }
        }
        AccessData<AccessDeptRDoc, Enum> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
        accessData.setTList(accessDeptRDocList);
        return accessData;
    }

    @Override
    public AccessData<AccessSch, UpdateSchField> getSchFromAccess(Long hosId) {
        AccessData<AccessSch, UpdateSchField> accessData = new AccessData<>();
        accessData.setDataModeType(DataModeType.ALL);
        accessData.setTList(ScheduleHelper.getScheduleList(this.tempDeptList, this.syncSchDays, hisWebserviceUrl));
        accessData.setUpdateFieldList(Arrays.asList(UpdateSchField.values()));
        return accessData;
    }

    @Override
    public AccessData<AccessDvi, UpdateDviField> getDviFromAccess(Long hosId) {
        AccessData<AccessDvi, UpdateDviField> accessData = new AccessData<>();
        try {
            accessData.setDataModeType(DataModeType.ALL);
            accessData.setTList(null);
//   accessData.settList(DviHelper.getDviList(tempDeptList, syncSchDays, hisWebserviceUrl));
            accessData.setUpdateFieldList(Arrays.asList(UpdateDviField.values()));
        } finally {
            tempDeptList = null;
        }
        return accessData;
    }


    @Override
    public AccessData<AccessSect, UpdateSectField> getSectFromAccess(Long hosId) {
        return null;
    }

    @Override
    public AccessData<AccessOltSch, UpdateOltSchField> getOltSchFromAccess(Long hosId) {
        return null;
    }

    @Override
    public AccessData<AccessDisease, UpdateDiseaseField> getDiseaseFromAccess(Long hosId) {
        return null;
    }

    @Override
    public AccessData<AccessDocRDisease, Enum> getDocRDiseaseFromAccess(Long hosId) {
        return null;
    }
}
