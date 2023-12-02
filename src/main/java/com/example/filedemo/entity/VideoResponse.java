package com.example.filedemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoResponse {

    private Integer code;
    private String msg;
    private Object data;

    public VideoResponse() {

    }

    public static VideoResponse success(int code, String msg, Object data) {
        VideoResponse r = new VideoResponse();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static VideoResponse success() {
        return success(200, null, null);
    }

    public static VideoResponse success(String msg) {
        return success(200, msg, null);
    }

    public static VideoResponse success(String msg, Object data) {
        return success(200, msg, data);
    }

    public static VideoResponse error(int code, String msg, Object data) {
        VideoResponse r = new VideoResponse();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static VideoResponse error(int code) {
        return error(code, null, null);
    }

    public static VideoResponse error(int code, String msg) {
        return error(code, msg, null);
    }

    public static VideoResponse error(String msg) {
        return error(500, msg, null);
    }

    public static VideoResponse error(String msg, Object data) {
        return error(500, msg, data);
    }

}
