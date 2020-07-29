package com.yuntai.sync.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: spring boot 测试 REST 接口
 * @ClassName: TestContronller
 * @Package: com.yuntai.sync.client.controller
 * @Author: qiufeng@hsyuntai.com
 * @Date: 2018/7/13 9:11
 * @Copyright: 版权归 HSYUNTAI 所有
 */
@RestController
public class TestContronller {

    @GetMapping("/")
    public String test() {
        return "hello, this is sync client";
    }

}
