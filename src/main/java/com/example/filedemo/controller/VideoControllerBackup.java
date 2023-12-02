package com.example.filedemo.controller;

import com.example.filedemo.common.utils.AesNewUtils;
import com.example.filedemo.entity.VideoRequest;
import com.example.filedemo.entity.VideoResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
@Log
public class VideoControllerBackup {

    private final static String secretKey = "1234567.$12345qw";
    private final static String key = "pLPMNVOGqiSC4CGXBfmS6XIUFP";

    private final static Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(1024)
            .maximumSize(102400)
            .expireAfterWrite(300, TimeUnit.SECONDS)
            .build();

    @PostMapping("/api/video")
    @ResponseBody
    public VideoResponse jieXi(@RequestBody VideoRequest paramTouTiao) {
        String dataString = paramTouTiao.getDataString();
        if (StringUtils.isEmpty(dataString)) {
            return VideoResponse.error("参数不能为空！");
        }
        /*
                加密信息生成：
                1.生成随机字符串 a
                2.获取当前时间戳 b
                3. 原始字符串 c = a + "|" + b
                4. 加密信息 d = aes(c, "秘钥")

                解密验证：
                1. 获取到加密信息 d
                2. 解密拿到原始字符串 c
                3. 使用|分割拿到时间戳 b
                4. 验证时间戳是否在最近n秒内。比如最近60s。在n秒内继续下一步。否则失败处理
                5. 验证该加密信息d是否已经（需要一个cache 存请求的加密信息，超过一定时间的比如n秒可以删除。
                6. 已经使用过该秘钥请求过直接失败处理，否则把该秘钥放入cache，后端请求接口
         */

        try {
            //{
            // "dataString":"xpz1XY0mI4+MqNIL+D+tMQ=="
            //}
            String decrypt = AesNewUtils.decrypt(dataString, secretKey);
            //videoUrl=https://video.weishi.qq.com/lHXJOqng;api=https://www.52api.cn/api/weishi;timestamp=2435432456543
            String[] decryptArray = decrypt.split(";");
            Map<String, String> stringStringHashMap = new HashMap<>();
            for (String s : decryptArray) {
                //timestamp=2435432456543
                String[] split = s.split("=");
                stringStringHashMap.put(split[0], split[1]);
            }
            //实现60000毫秒内允许请求
            String timestamp = stringStringHashMap.get("timestamp");
            long nowTimestamp = System.currentTimeMillis();
            if(nowTimestamp-Long.valueOf(timestamp) > 60000){
                return VideoResponse.error("请求不合法！");
            }
            StringBuilder urlBuilder = new StringBuilder();
            String urlStr = urlBuilder
                    .append(stringStringHashMap.get("api"))
                    .append("?key=").append(key)
                    .append("&url=").append(stringStringHashMap.get("videoUrl")).toString();
            String cacheIfPresent = cache.getIfPresent(dataString);
            if (Objects.nonNull(cache.getIfPresent(dataString))) {
                return VideoResponse.success("缓存获取成功", cacheIfPresent);
            }

            StringBuilder result = new StringBuilder();
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方式
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    // 读取到的内容给line变量
                    result.append(line);
                }
                reader.close();
                String encrypt = AesNewUtils.encrypt(result.toString(), secretKey);
                cache.put(dataString, encrypt);
                return VideoResponse.success("三方接口调用成功！", encrypt);
            }

            return VideoResponse.error("三方接口调用失败！");
        } catch (Exception e) {
            return VideoResponse.error("请求异常！");
        }
    }
}
