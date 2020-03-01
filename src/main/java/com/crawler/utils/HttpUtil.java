package com.crawler.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Component
public class HttpUtil {

    private PoolingHttpClientConnectionManager manager;
    public HttpUtil() {
        this.manager = new PoolingHttpClientConnectionManager();
        this.manager.setMaxTotal(100);
        this.manager.setDefaultMaxPerRoute(10);
    }

    /**
     * 解析html
     * @param url
     * @return
     */
    public String doGetHtml(String url) {
        //获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.manager).build();
        //创建httpget请求对象
        HttpGet httpGet = new HttpGet(url);


        httpGet.setConfig(this.getConfig());
        //解析响应，返回结果
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity() != null) {
                    String content = EntityUtils.toString(response.getEntity());
                    return content;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 下载图片
     * @param url
     * @return
     */
    public String doGetImg(String url) {
        //获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.manager).build();
        //创建httpget请求对象
        HttpGet httpGet = new HttpGet(url);


        httpGet.setConfig(this.getConfig());
        //解析响应，返回结果
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity() != null) {
                   //下载图片
                    //获取图片后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    //下载图片
                    String uuid = UUID.randomUUID().toString() + extName;
                    response.getEntity().writeTo(new FileOutputStream(new File("F:\\imgs\\" + uuid)));
                    //返回图片名称
                    return uuid;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(500).setSocketTimeout(10000).build();
        return config;
    }
}
