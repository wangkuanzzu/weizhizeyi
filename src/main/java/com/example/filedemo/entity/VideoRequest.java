package com.example.filedemo.entity;

import lombok.Data;

@Data
public class VideoRequest {
    /**
     * 对接平台
     * 默认使用，兼容已经对接的接口
     *  1:表示 52api那个平台接口，
     *  2：表示eepapi接口，再有其他新接入的接口就是类型
     *  3：新接其他接口
     */
    public String platform;

    /**
     * 加密参数
     * 解析后格式为 key1=value1;key2=value2
     */
    public String dataString;
}
