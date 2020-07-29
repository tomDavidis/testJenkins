package com.yuntai.sync.client.access.service.impl;

import com.yuntai.sync.api.access.model.AccessData;
import com.yuntai.sync.api.access.model.jyt.*;
import com.yuntai.sync.api.enums.DataModeType;
import com.yuntai.sync.api.enums.jyt.*;
import com.yuntai.sync.client.access.service.jyt.AccessJytPullService;
import com.yuntai.sync.client.his.DeptHelper;
import com.yuntai.sync.client.his.DoctorHelper;
import com.yuntai.sync.client.his.ScheduleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 云端主动拉取数据，客户端接口服务，客户端需要云端不同的数据类型，是实现不同的数据返回
 * @ClassName: AccessPullServiceImpl
 * @Package: com.yuntai.sync.client.access.service.impl
 * @Author: qiufeng@hsyuntai.com
 * @Date: 2018/7/15 16:48
 * @Copyright: 版权归 HSYUNTAI 所有
 */
@Service
public class AccessPullServiceImpl implements AccessJytPullService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessSyncServiceImpl.class);

    @Value("${his.webservice.url}")
    private String hisWebserviceUrl;

    @Value("${sync.sch.days}")
    private Integer syncSchDays;

    // 科室缓存
    private List<AccessDeptJyt> tempDeptList;


    @Override
    public AccessData<AccessDeptJyt, UpdateDeptJytField> getDeptJytFromAccess(Long aLong, DataModeType dataModeType) {
        tempDeptList = DeptHelper.getDeptListFromHos(hisWebserviceUrl);
        AccessData<AccessDeptJyt, UpdateDeptJytField> accessData = new AccessData<>();
        accessData.setDataModeType(dataModeType);
        accessData.setTList(tempDeptList);
        accessData.setUpdateFieldList(Arrays.asList(new UpdateDeptJytField[]{UpdateDeptJytField.deptName,
				UpdateDeptJytField.sort, UpdateDeptJytField.addr}));
        return accessData;
    }

    @Override
    public AccessData<AccessDocJyt, UpdateDocJytField> getDocJytFromAccess(Long aLong, DataModeType dataModeType) {
        AccessData<AccessDocJyt, UpdateDocJytField> accessData = new AccessData<>();
        accessData.setDataModeType(dataModeType);
        accessData.setTList(DoctorHelper.getDocList(tempDeptList, hisWebserviceUrl));
        accessData.setUpdateFieldList(Arrays.asList(new UpdateDocJytField[]{UpdateDocJytField.docVirtualName,
				UpdateDocJytField.docVirtualName, UpdateDocJytField.mediLevel, UpdateDocJytField.goodAt,
				UpdateDocJytField.titleShown}));
        return accessData;
    }

    @Override
    public AccessData<AccessDeptRDocJyt, Enum> getDeptRDocJytFromAccess(Long aLong, DataModeType dataModeType) {
        if (DoctorHelper.deptRDocMap.isEmpty()) {
            return null;
        }
        List<AccessDeptRDocJyt> accessDeptRDocList = new ArrayList<>();
        AccessDeptRDocJyt deptRDoc = null;
        for (String doctorId : DoctorHelper.deptRDocMap.keySet()) {
            for (String deptId : DoctorHelper.deptRDocMap.get(doctorId)) {
                deptRDoc = new AccessDeptRDocJyt();
                deptRDoc.setAccessDeptId(deptId);
                deptRDoc.setAccessDocId(doctorId);
                accessDeptRDocList.add(deptRDoc);
            }
        }
        AccessData<AccessDeptRDocJyt, Enum> accessData = new AccessData<>();
        accessData.setDataModeType(dataModeType);
        accessData.setTList(accessDeptRDocList);
        return accessData;
    }

    @Override
    public AccessData<AccessSchJyt, UpdateSchJytField> getSchJytFromAccess(Long aLong, DataModeType dataModeType) {
        AccessData<AccessSchJyt, UpdateSchJytField> accessData = new AccessData<>();
        accessData.setDataModeType(dataModeType);
        accessData.setTList(ScheduleHelper.getScheduleList(this.tempDeptList, this.syncSchDays, hisWebserviceUrl));
        accessData.setUpdateFieldList(Arrays.asList(UpdateSchJytField.values()));
        return accessData;
    }

    @Override
    public AccessData<AccessDviJyt, UpdateDviJytField> getDviJytFromAccess(Long aLong, DataModeType dataModeType) {
        AccessData<AccessDviJyt, UpdateDviJytField> accessData = new AccessData<>();
        try {
            accessData.setDataModeType(dataModeType);
            accessData.setTList(null);
//   accessData.settList(DviHelper.getDviList(tempDeptList, syncSchDays, hisWebserviceUrl));
            accessData.setUpdateFieldList(Arrays.asList(UpdateDviJytField.values()));
        } finally {
            tempDeptList = null;
        }
        return accessData;
    }

    @Override
    public AccessData<AccessOltSchJyt, UpdateOltSchJytField> getOltSchJytFromAccess(Long aLong, DataModeType dataModeType) {
        return null;
    }
}
