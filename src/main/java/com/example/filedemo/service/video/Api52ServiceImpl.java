package com.example.filedemo.service.video;

import com.alibaba.fastjson2.JSONObject;
import com.example.filedemo.common.utils.HttpUtils;
import com.example.filedemo.entity.VideoDetail;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log
@Service
public class Api52ServiceImpl implements ApiService{

    private static final String KEY = "pLPMNVOGqiSC4CGXBfmS6XIUFP";

    @Override
    public String generateVideoDetail(Map dataMap) {
        StringBuilder urlBuilder = new StringBuilder();
        String urlStr = urlBuilder
                .append(dataMap.get("api"))
                .append("?key=").append(KEY)
                .append("&url=").append(dataMap.get("videoUrl")).toString();
        String httpResult = HttpUtils.httpGet(urlStr);

        JSONObject jsonObject = JSONObject.parseObject(httpResult);
        if (jsonObject.getInteger("code") != 200) {
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.setAuthorName(data.getString("video_authorName"));
        videoDetail.setAuthorCover(data.getString("video_authorCover"));

        videoDetail.setVideoTitle(data.getString("video_title"));
        videoDetail.setVideoCover(data.getString("video_cover"));
        videoDetail.setVideoDynamicCover(data.getString("video_animatedCover"));
        videoDetail.setVideoUrl(data.getString("video_url"));

        return JSONObject.toJSONString(videoDetail);
    }

    public static void main(String[] args) {
        String httpResult = "{\n" +
                "\"code\": 200,\n" +
                "\"msg\": \"success\",\n" +
                "\"data\": {\n" +
                "\"video_title\": \"吴京心想：凯歌应该看到我抱他儿子了吧\",\n" +
                "\"video_cover\": \"http://weishi.gtimg.com/upload/20230708/782390229261.jpeg\",\n" +
                "\"video_dynamicCover\": \"\",\n" +
                "\"video_animatedCover\": \"http://shp.qpic.cn/wscover/0/gzc_2854_1047_0bc3fubbyaac4qaksonkqvsbkliedqwqehca_200_1/0?t=1688778247&q=90\",\n" +
                "\"video_animatedCover5f\": \"http://shp.qpic.cn/wscover/0/gzc_2854_1047_0bc3fubbyaac4qaksonkqvsbkliedqwqehca_105_1/0?t=1688778246&q=90\",\n" +
                "\"video_playNum\": 1800467,\n" +
                "\"video_dingNum\": 4455,\n" +
                "\"video_commentNum\": 492,\n" +
                "\"video_releaseDate\": \"2023-07-08 09:03:59\",\n" +
                "\"video_authorName\": \"明星大头条\",\n" +
                "\"video_authorCover\": \"http://avatar0.weishi.qq.com/uDNFBNJp7-2VvaU2MFwCs0w0G2J2lVoi7ilqU_100.jpg\",\n" +
                "\"video_authorCreatetime\": \"2021-04-22 00:29:22\",\n" +
                "\"video_authorAddress\": \"\",\n" +
                "\"video_url\": \"http://v.weishi.qq.com/v.weishi.qq.com/gzc_2854_1047_0bc3fubbyaac4qaksonkqvsbkliedqwqehca.f70.mp4?dis_k=97dc532a35c2239839ff5e559bbd457b&dis_t=1701513403&fromtag=0&pver=1.0.0\"\n" +
                "},\n" +
                "\"exec_time\": 1.230361,\n" +
                "\"ip\": \"106.37.113.223\"\n" +
                "}";
        System.out.println(httpResult);
        JSONObject jsonObject = JSONObject.parseObject(httpResult);
        System.out.println(jsonObject.getJSONObject("data").getString("video_title"));
        System.out.println(jsonObject.getJSONObject("data").getString("video_authorCover"));
    }
}
