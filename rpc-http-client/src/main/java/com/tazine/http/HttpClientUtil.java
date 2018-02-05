package com.tazine.http;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * HttpClientUtil
 *
 * @author frank
 * @since 1.0.0
 */
public class HttpClientUtil {


    private static final int MAX_RETRY_TIMES = 5;

    CloseableHttpClient httpClient = HttpClients.custom()
            // 设置最大连接数
            .setMaxConnTotal(100)
            // 设置重试策略
            .setRetryHandler(myRetryHandler)
            .build();

    private static HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException e, int executionOnCount, HttpContext httpContext) {
            if (executionOnCount >= MAX_RETRY_TIMES) {
                // Do not retry if over max retry count
                System.out.println("Over the max retry times");
                return false;
            }
            if (e instanceof InterruptedIOException) {
                // Timeout
                System.out.println("Timeout");
                return false;
            }
            if (e instanceof UnknownHostException) {
                // Unknown host
                //System.out.println("Unknown host exception");
                //return false;
            }
            if (e instanceof ConnectTimeoutException) {
                // Connection refused
                System.out.println("Connection refused");
                return false;
            }
            if (e instanceof SSLException) {
                // SSL handshake exception
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        }
    };

    public String getBody(String url) {

        HttpGet httpGet = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();

        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                String body = EntityUtils.toString(response.getEntity());
                System.out.println(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {

//        HttpClientUtil client = new HttpClientUtil();
//        client.getBody("http://www.tine2112211.com");
        try {
            String s = Request.Get("http://www.tazine.com").execute().returnContent().asString();
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
