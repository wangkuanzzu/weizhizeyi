package com.example.filedemo.entity;

import lombok.Data;

@Data
public class MusicDetail {

    /**
     * 作者
     */
    public String author; //音乐作者
    /**
     * 封面
     */
    public String avatar;//音乐封面
    /**
     * 地址
     */
    public String url;//音乐地址
}
