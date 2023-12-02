package com.example.filedemo.entity;

import lombok.Data;

@Data
public class VideoDetail {


    public String videoTitle;//视频标题
    public String videoCover;//视频封面
    public String videoDynamicCover;//视频动态封面
    public String videoUrl;//视频地址
    public String authorName;//作者名称
    public String authorCover;//作者封面头像
    public MusicDetail music;//音乐

}
