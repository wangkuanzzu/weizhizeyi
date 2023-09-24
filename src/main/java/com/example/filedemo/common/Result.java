package com.example.filedemo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public Result() {

    }

    public static Result success(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result success() {
        return success(200, null, null);
    }

    public static Result success(String msg) {
        return success(200, msg, null);
    }

    public static Result success(String msg, Object data) {
        return success(200, msg, data);
    }

    public static Result error(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result error(int code) {
        return error(code, null, null);
    }

    public static Result error(int code, String msg) {
        return error(code, msg, null);
    }

    public static Result error(String msg) {
        return error(500, msg, null);
    }

    public static Result error(String msg, Object data) {
        return error(500, msg, data);
    }

}
