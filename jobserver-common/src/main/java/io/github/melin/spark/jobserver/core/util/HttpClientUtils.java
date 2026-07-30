package io.github.melin.spark.jobserver.core.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpVersion;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;

/**
 * 基于 Apache HttpClient 5.x 优化的 HTTP 工具类
 */
public class HttpClientUtils {

    // 1. 初始化 HttpClient 5 连接池与客户端
    private static final CloseableHttpClient HTTP_CLIENT;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);

        // HttpClient 5 使用 Timeout 类来优雅地表达时间单位
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(5))
                .setConnectionRequestTimeout(Timeout.ofSeconds(2))
                .setResponseTimeout(Timeout.ofSeconds(10)) // 替代了旧版的 SocketTimeout
                .build();

        HTTP_CLIENT = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static void postRequest(String url, String key, String value) throws IOException {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(key, value));
        // 5.x 中，CloseableHttpResponse 继承了 AutoCloseable，支持 try-with-resources
        try (CloseableHttpResponse response = postRequest(url, params)) {
            // 自动释放连接
        }
    }

    public static void postRequest(String url, String key1, String value1, String key2, String value2)
            throws IOException {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(key1, value1));
        params.add(new BasicNameValuePair(key2, value2));
        try (CloseableHttpResponse response = postRequest(url, params)) {
            // 自动释放连接
        }
    }

    /**
     * 发送 POST 请求。
     * 注意：调用此方法获取 CloseableHttpResponse 后，外部必须使用 try-with-resources 或手动 close 释放响应！
     */
    public static CloseableHttpResponse postRequest(String url, List<NameValuePair> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        // 设置协议版本（HttpClient 5 依然支持显式指定 HTTP/1.1 或 1.0）
        httpPost.setVersion(HttpVersion.HTTP_1_1);

        httpPost.addHeader("Accept-Encoding", "gzip, deflate");

        // 5.x 的 UrlEncodedFormEntity 默认就是 UTF-8，但显式指定更为安全
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        return HTTP_CLIENT.execute(httpPost);
    }

    /**
     * 💡 推荐的 HttpClient 5 最佳实践：直接返回 String，内部安全关闭所有资源
     */
    public static String postRequestForString(String url, List<NameValuePair> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        // 使用 HttpClientResponseHandler 是 5.x 最推荐的做法，它能保证无论请求成功与否，底层连接都会安全释放
        return HTTP_CLIENT.execute(httpPost, response -> {
            int status = response.getCode();
            if (status >= 200 && status < 300) {
                return response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;
            } else {
                throw new IOException("Unexpected response status: " + status);
            }
        });
    }
}
