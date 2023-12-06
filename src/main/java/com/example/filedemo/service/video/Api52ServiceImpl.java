package com.example.filedemo.service.video;

import com.alibaba.fastjson2.JSONObject;
import com.example.filedemo.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
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
            log.error("三方接口返回失败", JSONObject.toJSONString(jsonObject));
            return null;
        }
        return httpResult;
    }

    public static void main(String[] args) {
        String httpResult = "{\n" +
                "\"code\": 200,\n" +
                "\"msg\": \"success\",\n" +
                "-\"data\": {\n" +
                "\"work_title\": \"定格的瞬间既是永恒\",\n" +
                "\"work_type\": \"video\",\n" +
                "\"work_cover\": \"https://p2.a.yximgs.com/upic/2023/07/17/10/BMjAyMzA3MTcxMDM5MTFfMTk2NzkwMTAzOF8xMDgxNjMzMTk1MjVfMV8z_B8b948fe2e7104a9392813aab11a0cdb4.jpg?clientCacheKey=3xhtqvyeidhxdek.jpg&di=dd0e93ab&bp=10000\",\n" +
                "\"work_releaseDate\": \"2023-07-17 10:39:31\",\n" +
                "\"work_viewCount\": 413599,\n" +
                "\"work_likeCount\": 6295,\n" +
                "\"work_commentCount\": 213,\n" +
                "\"work_shareCount\": null,\n" +
                "\"work_authorName\": \"ʚ张 婷 婷 ɞ.\",\n" +
                "\"work_avatar\": \"https://p4-pro.a.yximgs.com/uhead/AB/2023/09/16/22/BMjAyMzA5MTYyMjQxNTlfMTk2NzkwMTAzOF8xX2hkMzE3Xzg0NA==_s.jpg\",\n" +
                "\"work_authorKwaiId\": \"A59-Z1127L\",\n" +
                "\"work_url\": \"https://v1.kwaicdn.com/upic/2023/07/17/10/BMjAyMzA3MTcxMDM5MTFfMTk2NzkwMTAzOF8xMDgxNjMzMTk1MjVfMV8z_b_Bc4f1d70552b934531f227f22b62038e9.mp4?pkey=AAXmVBT0ojH2dVe4rI2vGyXThZROGaplxJmdPPNUzcfszX2_h_qHeJhjzah3exLr3SYSXZiqHWCyIy4NJX7c91HO8OxobPfE-SQceU03A7rTJovJPvum5htf41QOFFVyx68&clientCacheKey=3xhtqvyeidhxdek_b.mp4&tt=b&di=dd0e93ab&bp=10000\"\n" +
                "},\n" +
                "\"exec_time\": 1.261787,\n" +
                "\"ip\": \"106.37.112.107\"\n" +
                "}";
        System.out.println(httpResult);
        JSONObject jsonObject = JSONObject.parseObject(httpResult);
        System.out.println(jsonObject.getJSONObject("data").getString("video_title"));
        System.out.println(jsonObject.getJSONObject("data").getString("video_authorCover"));
    }


}
