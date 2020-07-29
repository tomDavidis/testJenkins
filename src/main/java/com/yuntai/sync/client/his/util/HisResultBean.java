package com.yuntai.sync.client.his.util;

import org.dom4j.Element;

/**
 * @author jiabj@hsyuntai.com
 * @Description: HIS-webservice返回结果
 * @Title: HisResultBean
 * @Package com.yuntai.sync.client.his
 * @Copyright 版权归 Hundsun 所有
 * @date 2019/9/16 16:27
 */
public class HisResultBean {
    public static final String CODE_SUCCESS = "0";
    public static final String COED_FAILED = "1";

    private String code;
    private boolean success;
    private String message;
    private Long timestamp = System.currentTimeMillis();
    private Element resultEle;

    /**
     * 构造器
     * @param success    是否成功 true false
     * @param code       his返回的状态码
     * @param message    提示信息
     * @param resultEle  his返回的业务数据xml节点
     */
    public HisResultBean(boolean success, String code, String message, Element resultEle) {
        this.success = success;
        this.code = success ? CODE_SUCCESS : code;
        this.message = message;
        this.resultEle = resultEle;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Element getResultEle() {
        return resultEle;
    }

    public void setResultEle(Element resultEle) {
        this.resultEle = resultEle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        final StringBuilder sb =
                new StringBuilder("HisResultBean").append('[').append("code=").append(code).append("," +
                        "success=").append(success).append(",message=").append(message).append(",timestamp=").append(timestamp).append(",resultEle=").append(resultEle == null ? "" : resultEle.asXML()).append(']');
        return sb.toString();
    }
}
