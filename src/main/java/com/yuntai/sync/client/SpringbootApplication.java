package com.yuntai.sync.client;

import com.yuntai.sync.api.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description: spring boot 入口类
 * @ClassName: SpringbootApplication
 * @Package: com.yuntai.sync.client
 * @Author: qiufeng@hsyuntai.com
 * @Date: 2018/7/13 9:29
 * @Copyright: 版权归 HSYUNTAI 所有
 */
@SpringBootApplication
@EnableAsync
public class SpringbootApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringbootApplication.class);

    public static void main(String[] args) {
        LoggerUtils.setTraceId(null);
        try {
            LoggerUtils.info(LOGGER, "同步客户端程序开始启动");
            SpringApplication.run(SpringbootApplication.class);
            LoggerUtils.info(LOGGER, "同步客户端程序启动完成");
        } finally {
            LoggerUtils.removeTraceId();
        }
    }

}
