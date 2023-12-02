package com.example.filedemo.entity;

import lombok.Data;

import java.util.List;

@Data
public class VideoDetail {


    public String type;//类型

    public String authorName;//作者名称
    public String authorCover;//作者封面头像

    public String title;//标题
    public String cover;//封面
    public String content;//文本内容
    public String videoUrl;//视频地址
    public String videoDynamicCover;//视频动态封面

    public List<String> imageUrls;//图集

    public MusicDetail music;//音乐
}
