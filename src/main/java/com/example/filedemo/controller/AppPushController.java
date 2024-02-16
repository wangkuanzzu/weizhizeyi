package com.example.filedemo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.filedemo.common.utils.HttpUtils;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class AppPushController {

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 第一个接口是查重复，告诉我们用户设备的UUID，我们在我们的数据库里查询，如果包含了这个UUID告诉推广商已经有了
     * @param idfa
     * @param appid
     * @param source
     * @return
     */
    @GetMapping("/api/checkidfa")
    @ResponseBody
    public Map checkidfa(@RequestParam String idfa,@RequestParam String appid,@RequestParam String source){
        if (null == redisTemplate.opsForValue().get(idfa)) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put(idfa, "-1");
            return resultMap;
        }
        JSONObject jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(idfa).toString());
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(idfa, jsonObject.getString("active"));
        return resultMap;
    }

    /**
     * 第二个接口是，推广商，把我们的应用推广给用户，也就是从来没下载过的用户，用户在点击我们应用的时候，推广商会调用我们的接口，
     * 告诉我们这个用户的UUID或者IDFA的用户已经去下载我们应用了。
     * 我们把推广商发过来的UUID或IDFA存到数据库里，并且标识这个用户来自推广商的推广。并且标记还未激活，只是下载了应用。
     * @param idfa
     * @return
     */
    @GetMapping("/api/clickidfa")
    @ResponseBody
    public Map checkidfa(@RequestParam String idfa,@RequestParam String appid,@RequestParam String source,
                            @RequestParam String model, @RequestParam String callback,@RequestParam String ip,@RequestParam String ua) {
        if (null != redisTemplate.opsForValue().get(idfa)) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", 0);
            resultMap.put("error", "该设备idfa已存在。"+ idfa);
            return resultMap;
        }
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("idfa", idfa);
        dataMap.put("appid", appid);
        dataMap.put("source", source);
        dataMap.put("model", model);
        dataMap.put("callback", callback);
        dataMap.put("ip", ip);
        dataMap.put("ua", ua);
        dataMap.put("active", "0");
        try {
            redisTemplate.opsForValue().set(idfa, JSON.toJSONString(dataMap));
        }catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", 0);
            resultMap.put("error", e.getMessage());
            return resultMap;
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", 1);
        resultMap.put("error", "成功");
        return resultMap;
    }

    /**
     * 第三个接口，是我们App内调用的接口，app调用的时候会传递一个参数UUID或IDFA告诉我们服务器这个用户下载了我们的应用，并
     * 判断下这个用户也就是根据UUID或IDFA看是否是来源这个推广商推广下载的，并把它标记为激活状态，来源是这个推广商
     * @param idfa
     * @return
     */
    @GetMapping("/api/app/active")
    @ResponseBody
    public Map appActive(@RequestParam String idfa) {
        Object idfaData = redisTemplate.opsForValue().get(idfa);
        if (idfaData == null) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", 1);
            resultMap.put("message", "非推广商来源激活");
            return resultMap;
        }

        JSONObject jsonObject = JSON.parseObject(redisTemplate.opsForValue().get(idfa).toString());
        String callback = jsonObject.getString("callback");
        JSONObject callbackResult = JSONObject.parseObject(HttpUtils.httpGet(callback));
        Integer result = callbackResult.getInteger("result");
        if (result == 1){
            jsonObject.put("active", "1");
            redisTemplate.opsForValue().set(idfa, JSON.toJSONString(jsonObject));
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", 1);
            resultMap.put("message", "回调成功，激活成功");
            return resultMap;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", 0);
        resultMap.put("message", "回调失败，激活失败" + JSON.toJSONString(callbackResult));
        return resultMap;
    }


}
