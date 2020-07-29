package com.yuntai.sync.client.utils;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Description: Http工具类
 * @Title: HttpUtil
 * @Package com.yuntai.sync.client.utils
 * @Copyright 版权归 Hundsun 所有
 * @author jiabj@hsyuntai.com
 * @date 2019/9/17 8:52
 */
public class HttpUtil {

    private static final Executor EXECUTOR = Executor.newInstance();

    /**
     * http post方法，实现webservice的调用
     * @param url          webserviceUrl
     * @param requestXml   请求的XML
     * @param timeout      超时时间
     * @return
     * @throws IOException
     */
    public static String post(String url, String requestXml, Integer timeout) throws IOException {
        return EXECUTOR.execute(Request.Post(url)
                .connectTimeout(timeout * 1000)
                .socketTimeout(timeout * 1000)
                .version(HttpVersion.HTTP_1_1)
                .bodyString(requestXml, ContentType.TEXT_XML.withCharset("UTF-8")))
                .returnContent()
                .asString(Charset.forName("UTF-8"));
    }
}
