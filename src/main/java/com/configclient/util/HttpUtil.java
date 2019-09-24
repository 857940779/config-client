package com.configclient.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @Author: luohanwen
 * @Date: 2019/9/18 16:02
 */
public class HttpUtil {


    public static String httpGetData(String url,Map param) throws Exception{
        HttpRequestBase httpRequest = getHttpRequest(buildRequestUrl(url,param), "GET", null);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        response = httpclient.execute(httpRequest);
        return parseReponse(response);
    }

    private static HttpRequestBase getHttpRequest(String url, String method, Header[] headers) {
        HttpRequestBase httpRequest = null;
        // 设置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(5000).build();
        if ("GET".equals(method.toUpperCase())) {
            httpRequest = new HttpGet(url);
        } else if ("POST".equals(method.toUpperCase())) {
            httpRequest = new HttpPost(url);
        }
        httpRequest.setConfig(requestConfig);
        if (headers!=null && headers.length >0) {
            httpRequest.setHeaders(headers);
        }
        return httpRequest;
    }

    //get请求把参数都封装在url中
    public  static String buildRequestUrl(String pathUrl, Map<String, Object> paramsMap) {
        String resultString = pathUrl + "?";
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            // 如果value是空值或者为空字符串，忽略
            if (null == entry.getValue() || String.valueOf(entry.getValue()).equals(""))
                continue;
            try {
                String value = URLEncoder.encode(entry.getValue().toString(), "utf-8");
                resultString += entry.getKey() + "=" + value + "&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return resultString.substring(0, resultString.length() - 1);
    }

    /**
     * 把返回数据转换
     */
    private static String parseReponse(CloseableHttpResponse response) throws Exception{
        String resultString = "";
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception("Response is Error. Error Code is " + response.getStatusLine().getStatusCode());
        }
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultString = EntityUtils.toString(entity,"UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
            }
        }
        return resultString;
    }
}
