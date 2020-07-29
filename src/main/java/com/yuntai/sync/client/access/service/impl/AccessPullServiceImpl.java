package com.yuntai.sync.client.access.service.impl;

import com.yuntai.sync.api.access.model.*;
import com.yuntai.sync.api.enums.*;
import com.yuntai.sync.client.access.service.AccessPullService;
import com.yuntai.sync.client.his.*;
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
public class AccessPullServiceImpl implements AccessPullService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccessSyncServiceImpl.class);

	@Value("${his.webservice.url}")
	private String hisWebserviceUrl;

	@Value("${sync.sch.days}")
	private Integer syncSchDays;

	// 科室缓存
	private List<AccessDept> tempDeptList;
	
    @Override
    public AccessData<AccessDept, UpdateDeptField> getDeptFromAccess(Long hosId, DataModeType dataModeType) {
		tempDeptList = DeptHelper.getDeptListFromHos(hisWebserviceUrl);
		AccessData<AccessDept, UpdateDeptField> accessData = new AccessData<>();
		accessData.setDataModeType(dataModeType);
		accessData.settList(tempDeptList);
		accessData.setUpdateFieldList(Arrays.asList(new UpdateDeptField[]{UpdateDeptField.deptName,
				UpdateDeptField.sort, UpdateDeptField.addr}));
		return accessData;
    }

    @Override
    public AccessData<AccessDoc, UpdateDocField> getDocFromAccess(Long hosId, DataModeType dataModeType) {
		AccessData<AccessDoc, UpdateDocField> accessData = new AccessData<>();
		accessData.setDataModeType(dataModeType);
		accessData.settList(DoctorHelper.getDocList(tempDeptList, hisWebserviceUrl));
		accessData.setUpdateFieldList(Arrays.asList(new UpdateDocField[]{UpdateDocField.docName,
				UpdateDocField.docVirtualName, UpdateDocField.mediLevel, UpdateDocField.goodAt,
				UpdateDocField.titleShown}));
		return accessData;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public AccessData<AccessDeptRDoc, Enum> getDeptRDocFromAccess(Long hosId, DataModeType dataModeType) {
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
		accessData.setDataModeType(dataModeType);
		accessData.settList(accessDeptRDocList);
		return accessData;
    }

    @Override
    public AccessData<AccessSch, UpdateSchField> getSchFromAccess(Long hosId, DataModeType dataModeType) {
		AccessData<AccessSch, UpdateSchField> accessData = new AccessData<>();
		accessData.setDataModeType(dataModeType);
		accessData.settList(ScheduleHelper.getScheduleList(this.tempDeptList, this.syncSchDays, hisWebserviceUrl));
		accessData.setUpdateFieldList(Arrays.asList(UpdateSchField.values()));
		return accessData;
    }

	@Override
	public AccessData<AccessDvi, UpdateDviField> getDviFromAccess(Long hosId, DataModeType dataModeType) {
		AccessData<AccessDvi, UpdateDviField> accessData = new AccessData<>();
		try {
			accessData.setDataModeType(dataModeType);
			accessData.settList(null);
			//accessData.setUpdateFieldList(Arrays.asList(UpdateDviField.values()));
			accessData.settList(DviHelper.getDviList(tempDeptList, syncSchDays, hisWebserviceUrl));
		} finally {
			tempDeptList = null;
		}
		return accessData;
	}

}
