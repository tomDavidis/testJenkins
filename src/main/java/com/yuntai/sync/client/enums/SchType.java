package com.yuntai.sync.client.enums;

/**
 * @author jiabj@hsyuntai.com
 * @Description: 排班类型枚举
 * @Title: SchType
 * @Package com.yuntai.sync.client.enums
 * @Copyright 版权归 Hundsun 所有
 * @date 2019/9/18 21:03
 */
public enum SchType {

    NORMAL("1", "普通门诊"),
    ZHUZHI("2", "主治医师"),
    FUZHUREN("3", "副主任医师"),
    ZHUREN("4", "主任医师"),
    ZHIMINGZJ("5", "知名专家教授"),
    TEXU("6", "特需门诊");

    private String schCode;
    private String title;

    private SchType(String schCode, String title) {
        this.schCode = schCode;
        this.title = title;
    }

    public static String getSchTypeFromCode(String schCode) {
        for (SchType schType : SchType.values()) {
            if (schCode.equals(schType.getSchCode())) {
                return schType.getTitle();
            }
        }
        return null;
    }

    public String getSchCode() {
        return schCode;
    }

    public String getTitle() {
        return title;
    }
}
