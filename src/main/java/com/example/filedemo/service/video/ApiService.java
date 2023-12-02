package com.example.filedemo.service.video;

import java.util.Map;

public interface ApiService {

    /**
     * 生成请求URL
     * @param dataMap
     * @return
     */
    String generateVideoDetail(Map dataMap);
}
