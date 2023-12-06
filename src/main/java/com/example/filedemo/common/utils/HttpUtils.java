package com.example.filedemo.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpUtils {

    public static String httpGet(String urlStr) {
        if (StringUtils.isEmpty(urlStr)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.error("三方接口HTTP请求失败");
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // 读取到的内容给line变量
                result.append(line);
            }
            reader.close();
            log.info("三方接口调用成功");
            return result.toString();
        } catch (Exception e) {
            log.error("三方接口调用异常", e);
            throw new RuntimeException("三方接口调用异常", e);
        }
    }
}
