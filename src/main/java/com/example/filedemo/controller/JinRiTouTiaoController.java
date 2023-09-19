package com.example.filedemo.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class JinRiTouTiaoController {

    private final static String key = "";

    @PostMapping("/api/toutiao")
    @ResponseBody
    public void jieXi(@RequestBody String videoUrl) {


        try {
            URL url = new URL("https://www.52api.cn/api/toutiao?key=" + key + "&url" + videoUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

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
                    System.out.println(line);
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/api/cache")
    @Cacheable(value = "videoUrls",key = "#videoUrl")
    @ResponseBody
    public String demo(@RequestParam String videoUrl) {
        System.out.println(videoUrl);
        return videoUrl.toUpperCase();
    }

}
