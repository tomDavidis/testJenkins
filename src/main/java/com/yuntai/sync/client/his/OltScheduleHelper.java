package com.yuntai.sync.client.his;


import com.alibaba.fastjson.JSONObject;
import com.yuntai.sync.api.access.model.jyt.AccessOltSchJyt;
import com.yuntai.sync.api.enums.DayType;
import com.yuntai.sync.client.his.util.YuntaiDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OltScheduleHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OltScheduleHelper.class);

    public static List<AccessOltSchJyt> mock(){

        List<AccessOltSchJyt> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            AccessOltSchJyt accessOltSch = new AccessOltSchJyt();
            accessOltSch.setAccessDeptId("100");
            accessOltSch.setAccessDocId("200");
            accessOltSch.setAccessOltSchId("30"+i);
            accessOltSch.setSchDate(YuntaiDateUtils.parseDate(LocalDate.now().plusDays(i).toString(), "yyyy-MM-dd"));
            accessOltSch.setStartTime(YuntaiDateUtils.parseDate("05:00:00", "HH:mm:ss"));
            accessOltSch.setEndTime(YuntaiDateUtils.parseDate("23:00:00", "HH:mm:ss"));
            accessOltSch.setDayType(DayType.ALL.getCode());
            list.add(accessOltSch);
        }
        LOGGER.info("在线诊疗同步排班：{}",JSONObject.toJSONString(list));
        return list;
    }


}
