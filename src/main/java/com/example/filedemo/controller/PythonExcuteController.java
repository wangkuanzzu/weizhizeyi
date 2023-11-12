package com.example.filedemo.controller;

import com.example.filedemo.common.Result;
import com.example.filedemo.entity.RequestParamTouTiao;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Controller
@Log
public class PythonExcuteController {

    private final static String secretKey = "1234567.$12345qw";


    @PostMapping("/api/python")
    @ResponseBody
    public Result jieXi(@RequestBody RequestParamTouTiao paramTouTiao) {





        return Result.success("成功", null);
    }


    public static void main(String[] args) throws IOException {
            System.out.println("======START - - RESULT=======" + System.currentTimeMillis());
            List<String> cmd = new ArrayList<>();
            cmd.add("python");
            cmd.add("D:/IdeaProjects/file-demo/target/classes/static/python/demo.py");
            cmd.add("24");
            cmd.add("0.013");

            ProcessBuilder pb = new ProcessBuilder(cmd);
            // 合并 错误流和标准流
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                if (process.waitFor() != 0) {
                    System.out.println("commond exec error");
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("====" + line);
                }

                // 可以使用 org.apache.commons.io 包下的 IoUtils 读流转换为String
                // IoUtils.tostring(reader);
                // 等待命令执行完成
                int code = process.waitFor();

                if (code == 0) {
                    //通常情况0 表示命令或者脚本正常退出，但是如果脚本自己有返回状态这里需要根据自己状态判断
                    System.out.println("success");
                } else {
                    System.out.println("fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("======START - - RESULT=======" + System.currentTimeMillis());
        }

}
