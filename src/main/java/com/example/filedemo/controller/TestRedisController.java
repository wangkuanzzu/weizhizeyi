package com.example.filedemo.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Controller
public class TestRedisController {

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * http://localhost:8080/redis/all
     * http://localhost:8080/redis/delete/all
     *
     * @return
     */
    @GetMapping("/all")
    @ResponseBody
    public String getAllKey() {
        return JSON.toJSONString(redisTemplate.keys("*"));
    }

    /**
     * http://localhost:8080/redis/all/regex?regex=*bound*
     */
    @GetMapping("/regexKey")
    @ResponseBody
    public String getRegexKey(@RequestParam String regex) {
        return JSON.toJSONString(redisTemplate.keys(regex));
    }

    @GetMapping("/delete")
    @ResponseBody
    public Boolean delete(@RequestParam String key) {
        return redisTemplate.delete(key);
    }

    @GetMapping("/delete/all")
    @ResponseBody
    public Long deleteAll(@RequestParam String key) {
        Set keys = redisTemplate.keys("*");
        return redisTemplate.delete(keys);
    }

    /**
     * 设置过期时间，单位秒
     */
    @GetMapping("/expire")
    @ResponseBody
    public Boolean expireSeconds(@RequestParam String key, @RequestParam long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 添加带过期时间的key
     */
    @GetMapping("/setExpireValue")
    @ResponseBody
    public String setValue(@RequestParam String key, @RequestParam String value, @RequestParam long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        return "success";
    }

    /**
     * 获取过期时间，单位秒
     */
    @GetMapping("/getExpire")
    @ResponseBody
    public Long getExpire(@RequestParam String key, @RequestParam long timeout) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 移除过期时间
     */
    @GetMapping("/persist")
    @ResponseBody
    public Boolean persist(@RequestParam String key) {
        return redisTemplate.persist(key);
    }




    /**
     * http://localhost:8088/redis/setValue?key=owner&value=Tinyspot
     * http://localhost:8088/redis/getValue?key=owner
     */
    @GetMapping("/api/setValue")
    @ResponseBody
    public String setValue(@RequestParam String key,@RequestParam String value){
        redisTemplate.opsForValue().set(key, value);
        return "success";
    }

    @GetMapping("/api/getValue")
    @ResponseBody
    public String getValue(@RequestParam String key) {
        return redisTemplate.opsForValue().get(key).toString();
    }

    /**
     * void put(H key, HK hashKey, HV value);
     * hashOperations.putAll(key, Map<K, V>);
     *
     * http://localhost:8088/redis/opsForHash?key=books
     * Literature
     *   |- Poetry
     *   |- Prose
     * Art
     *   |- Music
     *   |- Photography
     */
    @RequestMapping("/api/opsForHash")
    @ResponseBody
    public String opsForHash(String key, String... hashKey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, "Literature", "Poetry");
        hashOperations.put(key, "Literature", "Prose");
        hashOperations.put(key, "Art", "Music");
        hashOperations.put(key, "Art", "Photography");

        Set keys = hashOperations.keys(key);
        return JSON.toJSONString(keys);
    }

    @RequestMapping("/opsForHashDelete")
    @ResponseBody
    public Long opsForHashDelete(String key, String hashKey) {
        // Long delete(H key, Object... hashKeys)
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    @RequestMapping("/api/opsForList")
    @ResponseBody
    public String opsForList(String key) {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush(key, "111");
        listOperations.leftPush(key, "222");

        List range = listOperations.range(key, 0, listOperations.size(key));
        return JSON.toJSONString(range);
    }

    @RequestMapping("/api/opsForListAll")
    @ResponseBody
    public String opsForListAll(String key) {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll(key, "111", "222", "333");

        List range = listOperations.range(key, 0, listOperations.size(key));
        return JSON.toJSONString(range);
    }

    @RequestMapping("/api/opsForSet")
    @ResponseBody
    public String opsForSet(String key) {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(key, "aaa");
        setOperations.add(key, "bbb");

        Set members = setOperations.members(key);
        return JSON.toJSONString(members);
    }

    @RequestMapping("/api/opsForZSet")
    @ResponseBody
    public String opsForZSet(String key) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(key, "aaa", 1);
        zSetOperations.add(key, "bbb", 2);
        zSetOperations.add(key, "ccc", 3);

        Set range = zSetOperations.range(key, 0, zSetOperations.size(key));
        return JSON.toJSONString(range);
    }
}
