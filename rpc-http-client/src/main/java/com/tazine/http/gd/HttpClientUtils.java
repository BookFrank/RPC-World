package com.tazine.http.gd;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * HttpClientUtils
 *
 * @author frank
 * @since 1.0.0
 */
public class HttpClientUtils {

    private static PoolingHttpClientConnectionManager connectionManager = null;

    private static HttpClientBuilder httpClientBuilder = null;

    private static RequestConfig requestConfig = null;

    private static final int MAX_CONNECTION_NUM = 10;

    private static final int DEFAULT_MAX_CONNECTION = 5;

    private static final int MAX_RETRY_TIMES = 3;

    private static HttpRequestRetryHandler gdRetryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException e, int executionOnCount, HttpContext httpContext) {
            System.out.println("进行一次重试");
            if (executionOnCount >= MAX_RETRY_TIMES) {
                // Do not retry if over the max retry count
                System.out.println("Over the max_retry_times");
                return false;
            }
            if (e instanceof InterruptedIOException) {
                // Timeout
                System.out.println("Timeout");
                return false;
            }
            if (e instanceof UnknownHostException) {
                // Unknown host
                System.out.println("Unknown host exception");
                return false;
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

    static {
        /**
         * 设置 HTTP 的请求参数
         */
        requestConfig = RequestConfig.custom()
                // 连接超时，指的是连接一个 URL 的等待时间
                .setConnectTimeout(5000)
                // 读取数据超时，指的是连接上一个URL，获取 response 的返回等待时间
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        /**
         * 设置连接池的相关参数
         */
        connectionManager = new PoolingHttpClientConnectionManager();
        // 客户端总并行连接最大数
        connectionManager.setMaxTotal(MAX_CONNECTION_NUM);
        // 每个主机的最大并行连接数
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTION);

        httpClientBuilder = HttpClients.custom()
                .setRetryHandler(gdRetryHandler)
                .setConnectionManager(connectionManager);
    }

    /**
     * 从连接池中获取连接
     *
     * @return ClosableHttpClient
     */
    private static CloseableHttpClient getConnection() {
        return httpClientBuilder.build();
    }

    public static void get(String url){
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getConnection();

            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                System.out.println(EntityUtils.toString(entity));
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
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭连接，释放资源
            if (null != httpClient){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void post(String url){
        CloseableHttpClient httpClient = null;

        // 创建 HttpClient 实例
        httpClient = getConnection();
        // 创建 HttpPost
        HttpPost httpPost = new HttpPost(url);
        // 创建参数队列
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("type", "house"));
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            CloseableHttpResponse response = null;
            try {
               response = httpClient.execute(httpPost);
               HttpEntity entity = response.getEntity();
               if (null != entity){
                   System.out.println(EntityUtils.toString(entity));
               }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != response){
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (null != httpClient){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        //HttpClientUtils.get("http://www.tazine2.com");
        HttpClientUtils.post("http://124.251.25.20:91/mana/hmset");
    }
}
