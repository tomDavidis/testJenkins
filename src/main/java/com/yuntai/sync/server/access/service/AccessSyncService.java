package com.yuntai.sync.server.access.service;

import com.yuntai.sync.api.access.model.AccessData;
import com.yuntai.sync.api.access.model.med.*;
import com.yuntai.sync.api.enums.med.*;

/**
 * @Description: 对接服务，供同步定时器调用，不做泛型，加强约束
 * @ClassName: AccessSyncService
 * @Package: com.yuntai.sync.client.access.service
 * @Author: qiufeng@hsyuntai.com
 * @Date: 2018/7/3 18:59
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public interface AccessSyncService {

    /**
     * @Description: 从对接获取大科室集合
     * @Param:
     * @Return:
     * @Author: qiufeng@hsyuntai.com
     * @Date: 2018/7/3 19:24
     */
    AccessData<AccessSect, UpdateSectField> getSectFromAccess(Long hosId);

    /**
     * @Description: 从对接获取门诊集合
     * @Param:
     * @Return:
     * @Author: qiufeng@hsyuntai.com
     * @Date: 2018/7/3 19:29
     */
    AccessData<AccessDept, UpdateDeptField> getDeptFromAccess(Long hosId);

    /**
     * @Description: 从对接获取医生集合
     * @Param:
     * @Return:
     * @Author: qiufeng@hsyuntai.com
     * @Date: 2018/7/3 19:41
     */
    AccessData<AccessDoc, UpdateDocField> getDocFromAccess(Long hosId);

    /**
     * @Description: 从对接获取门诊医生关系集合
     * @Param:
     * @Return:
     * @Author: qiufeng@hsyuntai.com
     * @Date: 2018/7/3 19:43
     */
    AccessData<AccessDeptRDoc, Enum> getDeptRDocFromAccess(Long hosId);

    /**
     * @Description: 从对接获取排班集合
     * @Param:
     * @Return:
     * @Author: qiufeng@hsyuntai.com
     * @Date: 2018/7/3 19:57
     */
    AccessData<AccessSch, UpdateSchField> getSchFromAccess(Long hosId);

    /**
     * @Description: 从对接获取在线诊疗排班集合
     * @Param:
     * @Return:
     * @Author: qiufeng@hsyuntai.com
     * @Date: 2018/7/3 19:57
     */
    AccessData<AccessOltSch, UpdateOltSchField> getOltSchFromAccess(Long hosId);

    /**
     * @description 从对接获取病种集合
     * @param
     * @return
     * @author lijh@hsyuntai.com
     * @date 2019/11/11 18:42
     */
    AccessData<AccessDisease, UpdateDiseaseField> getDiseaseFromAccess(Long hosId);

    /**
     * @description 从对接获取医生病种关系集合
     * @param
     * @return
     * @author lijh@hsyuntai.com
     * @date 2019/11/11 18:41
     */
    AccessData<AccessDocRDisease, Enum> getDocRDiseaseFromAccess(Long hosId);

}
