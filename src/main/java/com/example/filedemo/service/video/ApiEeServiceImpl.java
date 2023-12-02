package com.example.filedemo.service.video;

import com.alibaba.fastjson2.JSONObject;
import com.example.filedemo.common.utils.HttpUtils;
import com.example.filedemo.entity.MusicDetail;
import com.example.filedemo.entity.VideoDetail;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log
@Service
public class ApiEeServiceImpl implements ApiService{

    private final static String EE_API = "http://web.eeapi.cn/api/video/32B436849149DAACF6FDC6C880B7F0D02A1E4698A4FFA757CB/4233/?url=";

    @Override
    public String generateVideoDetail(Map dataMap) {
        StringBuilder urlBuilder = new StringBuilder();
        String urlStr = urlBuilder
                .append(EE_API)
                .append(dataMap.get("videoUrl")).toString();
        String httpResult = HttpUtils.httpGet(urlStr);
        JSONObject jsonObject = JSONObject.parseObject(httpResult);

        if (jsonObject.getInteger("code") != 200 || jsonObject.getInteger("status") != 101) {
            return null;
        }
        JSONObject data = jsonObject.getJSONObject("data");
        VideoDetail videoDetail = new VideoDetail();
        videoDetail.setAuthorName(data.getString("Name"));
        videoDetail.setAuthorCover(data.getString("avatar"));

        videoDetail.setVideoTitle(data.getString("title"));
        videoDetail.setVideoCover(data.getString("cover"));
        videoDetail.setVideoDynamicCover(data.getString("cover"));
        videoDetail.setVideoUrl(data.getString("url"));

        MusicDetail musicDetail = new MusicDetail();
        if (data.getJSONObject("music") != null) {
            musicDetail.setAuthor(data.getJSONObject("music").getString("author"));
            musicDetail.setAvatar(data.getJSONObject("music").getString("avatar"));
            musicDetail.setUrl(data.getJSONObject("music").getString("url"));
        }
        videoDetail.setMusic(musicDetail);
        return JSONObject.toJSONString(videoDetail);
    }

    public static void main(String[] args) {
        String httpResult = "{\n" +
                "\t\"status\": 101,\n" +
                "\t\"code\": 200,\n" +
                "\t\"msg\": \"获取成功\",\n" +
                "\t\"解析时间\": \"2023-12-02 10:02:00\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"Name\": \"凛1917\",\n" +
                "\t\t\"avatar\": \"https://p3-pc.douyinpic.com/aweme/1080x1080/aweme-avatar/tos-cn-i-0813_e4a66df576ce4f7bac067f5c0bbc1b7e.jpeg?from=116350172\",\n" +
                "\t\t\"title\": \"#美女 #泳装\uD83D\uDC59 #完美身材 #这谁顶得住啊 大多数女的还是不敢穿这种泳装吧！\",\n" +
                "\t\t\"cover\":          \"https://p3-pc-sign.douyinpic.com/image-cut-tos-priv/82da0165e7a807046706a03db33aff43~tplv-dy-resize-origshort-autoq-75:330.jpeg?x-expires=2016842400&x-signature=STT5fJ3egDvY3ysj1CP01%2BveqEI%3D&from=3213915784&s=PackSourceEnum_AWEME_DETAIL&se=false&sc=cover&biz_tag=pcweb_cover&l=20231202100200ED86DA2BBC3C3146A6C1\",\n" +
                "\t\t\"download_image\": \"https://p3-pc-sign.douyinpic.com/image-cut-tos-priv/82da0165e7a807046706a03db33aff43~tplv-dy-resize-origshort-autoq-75:330.jpeg?x-expires=2016842400&x-signature=STT5fJ3egDvY3ysj1CP01%2BveqEI%3D&from=3213915784&s=PackSourceEnum_AWEME_DETAIL&se=false&sc=cover&biz_tag=pcweb_cover&l=20231202100200ED86DA2BBC3C3146A6C1\",\n" +
                "\t\t\"url\": \"http://v3-default.365yg.com/d5bd13f437238e7b2a8bdec3b409a304/656a9e32/video/tos/cn/tos-cn-ve-15c001-alinc2/a9ab534b3a6b4af5a402795f2c3886e4/?a=0&ch=0&cr=0&dr=0&lr=unwatermarked&net=5&cd=0%7C0%7C0%7C0&cv=1&br=2538&bt=2538&cs=0&ds=4&ft=rkG4k0071jevGHhWH6xRfCSl4YBO5nt74Gdz7tG&mime_type=video_mp4&qs=0&rc=NTQ7O2lpZ2c8Nzs2PDk3OkBpanJxaDM6Zm5qZTMzNGkzM0BfMzBhYzM2Xi8xMzYtYy82YSNpMDVvcjRfa2tgLS1kLTBzcw%3D%3D&btag=e00008000&dy_q=1701482520&l=20231202100200179B7DCA2C006ED94DFD\",\n" +
                "\t\t\"video\": \"http://v3-default.365yg.com/d5bd13f437238e7b2a8bdec3b409a304/656a9e32/video/tos/cn/tos-cn-ve-15c001-alinc2/a9ab534b3a6b4af5a402795f2c3886e4/?a=0&ch=0&cr=0&dr=0&lr=unwatermarked&net=5&cd=0%7C0%7C0%7C0&cv=1&br=2538&bt=2538&cs=0&ds=4&ft=rkG4k0071jevGHhWH6xRfCSl4YBO5nt74Gdz7tG&mime_type=video_mp4&qs=0&rc=NTQ7O2lpZ2c8Nzs2PDk3OkBpanJxaDM6Zm5qZTMzNGkzM0BfMzBhYzM2Xi8xMzYtYy82YSNpMDVvcjRfa2tgLS1kLTBzcw%3D%3D&btag=e00008000&dy_q=1701482520&l=20231202100200179B7DCA2C006ED94DFD\",\n" +
                "\t\t\"down\": \"https://v3-default.365yg.com/d5bd13f437238e7b2a8bdec3b409a304/656a9e32/video/tos/cn/tos-cn-ve-15c001-alinc2/a9ab534b3a6b4af5a402795f2c3886e4/?a=0&ch=0&cr=0&dr=0&lr=unwatermarked&net=5&cd=0%7C0%7C0%7C0&cv=1&br=2538&bt=2538&cs=0&ds=4&ft=rkG4k0071jevGHhWH6xRfCSl4YBO5nt74Gdz7tG&mime_type=video_mp4&qs=0&rc=NTQ7O2lpZ2c8Nzs2PDk3OkBpanJxaDM6Zm5qZTMzNGkzM0BfMzBhYzM2Xi8xMzYtYy82YSNpMDVvcjRfa2tgLS1kLTBzcw%3D%3D&btag=e00008000&dy_q=1701482520&l=20231202100200179B7DCA2C006ED94DFD\",\n" +
                "\t\t\"music\": {\n" +
                "\t\t\t\"author\": \"DJ京仔Remix\",\n" +
                "\t\t\t\"avatar\": \"https://p3-pc.douyinpic.com/aweme/1080x1080/aweme-avatar/mosaic-legacy_2d00b000496cb89a788aa.jpeg?from=116350172\",\n" +
                "\t\t\t\"url\": \"https://sf3-cdn-tos.douyinstatic.com/obj/ies-music/7104592753861856030.mp3\"\n" +
                "\t\t},\n" +
                "\t\t\"duration\": 10380,\n" +
                "\t\t\"bigFile\": false\n" +
                "\t},\n" +
                "\t\"type\": \"mp4\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(httpResult);
        System.out.println( jsonObject.getJSONObject("data").getString("title"));
        System.out.println( jsonObject.getJSONObject("data").getJSONObject("music").getString("author"));

    }
}
