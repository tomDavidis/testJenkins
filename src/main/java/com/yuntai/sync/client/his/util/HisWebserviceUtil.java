package com.yuntai.sync.client.his.util;


import com.yuntai.sync.client.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * @author jiabj@hsyuntai.com
 * @Description: 访问HIS的webservice工具类
 * @Title: HisWebserviceUtil
 * @Package com.yuntai.sync.client.utils
 * @Copyright 版权归 Hundsun 所有
 * @date 2019/9/16 13:27
 */
public class HisWebserviceUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(HisWebserviceUtil.class);

    /**
     * 请求HIS的webservice工具类
     *
     * @param hisServiceUrl his的webservice地址
     * @param timeout       超时时间
     * @return
     */
    public static HisResultBean getResponse(String hisServiceUrl, String bizxml, Integer timeout) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        sb.append("<request>");
        sb.append("<time_stamp>" + YuntaiDateUtils.dateFormatToString(new Date(), "yyyyMMddHHmmssSSS") +
                "</time_stamp>");
        sb.append("<input>" + bizxml + "</input>");
        sb.append("</request>");
        String responseXml = "";
        try {
            responseXml = HttpUtil.post(hisServiceUrl, sb.toString(), timeout);
        } catch (IOException e) {
            LOGGER.error("调用HIS webservice接口出现异常", e);
            return new HisResultBean(false, HisResultBean.COED_FAILED, e.getMessage(), null);
        }
        if (StringUtils.isBlank(responseXml)) {
            return new HisResultBean(false, HisResultBean.COED_FAILED, "HIS返回结果XML为空!", null);
        }
        LOGGER.info("HIS返回xml:{}", responseXml);
        try {
            Element rootEle = DocumentHelper.parseText(responseXml).getRootElement();
            // 业务数据节点
            Element outputEle = rootEle.element("output");
            String code = rootEle.elementTextTrim("ret_code");
            if (!HisResultBean.CODE_SUCCESS.equals(code)) {
                return new HisResultBean(false, code, rootEle.elementTextTrim("ret_msg"), null);
            }
            return new HisResultBean(true, HisResultBean.CODE_SUCCESS, "处理成功", outputEle);
        } catch (DocumentException e) {
            LOGGER.error("调用HIS webservice接口出现异常", e);
            return new HisResultBean(false, HisResultBean.COED_FAILED, e.getMessage(), null);
        }
    }
}
