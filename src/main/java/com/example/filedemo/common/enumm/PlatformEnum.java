package com.example.filedemo.common.enumm;

public enum PlatformEnum {
    API_52("1", "https://www.52api.cn/doc/50"),
    API_EE("2", "http://web.eeapi.cn/getapi.html");

    public String code;
    public String desc;

    PlatformEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PlatformEnum fromCode(String code){
        for (PlatformEnum value : PlatformEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

}
