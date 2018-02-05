package com.tazine.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * HttpClientUtil
 *
 * @author frank
 * @since 1.0.0
 */
public class HttpClientUtil {


    CloseableHttpClient httpClient = HttpClients.custom()
            // 设置最大连接数
            .setMaxConnTotal(100)
            //.setRetryHandler()
            .build();

}
