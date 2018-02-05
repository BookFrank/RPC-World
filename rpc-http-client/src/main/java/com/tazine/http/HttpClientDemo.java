package com.tazine.http;

import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpClientDemo
 *
 * @author frank
 * @since 1.0.0
 */
public class HttpClientDemo {

    private static final int MAX_RETRY_TIMES = 10;

    private CloseableHttpClient httpClient;

    public HttpClientDemo() {
        this.httpClient = HttpClients.createDefault();
    }

    public String getBody(String url) {

        HttpGet httpGet = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        httpGet.setConfig(requestConfig);

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                String body = EntityUtils.toString(response.getEntity());
                System.out.println(body);
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBodyWithRetry(String url, int retryTimes) {

        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(30000)
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .build();
        httpGet.setConfig(requestConfig);

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                String body = EntityUtils.toString(response.getEntity());
                System.out.println(body);
            } else {
                System.out.println("请求错误 " + statusLine.getStatusCode() + " , retryTimes : " + retryTimes);
                if (retryTimes < MAX_RETRY_TIMES) {
                    return getBodyWithRetry(url, retryTimes + 1);
                }
                httpGet.abort();
            }
            response.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("请求异常 " + " , retryTimes : " + retryTimes);
            if (retryTimes < MAX_RETRY_TIMES) {
                return getBodyWithRetry(url, retryTimes + 1);
            }
            httpGet.abort();

        }
        return null;
    }

    public static void main(String[] args) {

        HttpClientDemo client = new HttpClientDemo();
        client.getBodyWithRetry("http://www.tine2112211.com", 0);
    }
}
