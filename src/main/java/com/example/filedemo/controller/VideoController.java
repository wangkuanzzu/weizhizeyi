package com.example.filedemo.controller;

import com.example.filedemo.common.utils.AesNewUtils;
import com.example.filedemo.common.enumm.PlatformEnum;
import com.example.filedemo.common.SecretConstant;
import com.example.filedemo.entity.VideoResponse;
import com.example.filedemo.entity.VideoRequest;
import com.example.filedemo.service.video.Api52ServiceImpl;
import com.example.filedemo.service.video.ApiEeServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
@Log
public class VideoController {

    private final static Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(1024)
            .maximumSize(102400)
            .expireAfterWrite(300, TimeUnit.SECONDS)
            .build();

    @Resource
    private Api52ServiceImpl api52ServiceImpl;

    @Resource
    private ApiEeServiceImpl apiEeServiceImpl;

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
    @PostMapping("/api/video/v2")
    @ResponseBody
    public VideoResponse jieXi(@RequestBody VideoRequest videoRequest) {
        String dataString = videoRequest.getDataString();
        String platform = videoRequest.getPlatform();
        if (StringUtils.isEmpty(dataString) || StringUtils.isEmpty(platform)) {
            return VideoResponse.error("参数不能为空！");
        }

        PlatformEnum platformEnum = PlatformEnum.fromCode(platform);
        if (platformEnum == null) {
            return VideoResponse.error("不合法的平台编码！");
        }
        try {
            //dataString=xpz1XY0mI4+MqNIL+D+tMQ
            String decrypt = AesNewUtils.decrypt(dataString, SecretConstant.SECRET_KEY);
            //videoUrl=https://video.weishi.qq.com/lHXJOqng;api=https://www.52api.cn/api/weishi;timestamp=2435432456543
            String[] decryptArray = decrypt.split(";");
            Map<String, String> dataMap = new HashMap<>();
            for (String s : decryptArray) {
                //timestamp=2435432456543
                String[] split = s.split("=");
                dataMap.put(split[0], split[1]);
            }
            //实现60000毫秒内允许请求
            String timestamp = dataMap.get("timestamp");
            long nowTimestamp = System.currentTimeMillis();
            if(nowTimestamp-Long.valueOf(timestamp) > 60000){
                return VideoResponse.error("请求不合法！");
            }
            //缓存中获取节省接口调用成本
            String cacheIfPresent = cache.getIfPresent(dataString);
            if (Objects.nonNull(cache.getIfPresent(dataString))) {
                return VideoResponse.success("缓存获取成功", cacheIfPresent);
            }
            //不同对接平台
            String httpResult = null;
            if (PlatformEnum.API_52.equals(platformEnum)) {
                httpResult = api52ServiceImpl.generateVideoDetail(dataMap);
            }
            if (PlatformEnum.API_EE.equals(platformEnum)) {
                httpResult = apiEeServiceImpl.generateVideoDetail(dataMap);
            }
            if (httpResult == null) {
                return VideoResponse.error("三方接口调用失败！");
            }
            //成功后放入缓存，返回结果
            String encrypt = AesNewUtils.encrypt(httpResult, SecretConstant.SECRET_KEY);
            cache.put(dataString, encrypt);
            return VideoResponse.success("三方接口调用成功！", encrypt);
        } catch (Exception e) {
            return VideoResponse.error("请求异常！");
        }
    }
}
