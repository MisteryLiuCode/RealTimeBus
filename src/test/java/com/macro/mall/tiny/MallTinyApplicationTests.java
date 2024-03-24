package com.macro.mall.tiny;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@SpringBootTest
@Slf4j
public class MallTinyApplicationTests {

    @Value("${bjbus.token}")
    private String token;

    @Test
    public void contextLoads() {
    }


    @Test
    public void test() {
        String timeUrl = "http://www.bjbus.com/api/api_etartime.php?conditionstr=000000058250560-110100013292004&token=" + token;
        log.info("请求路径:{}", timeUrl);
        String result = HttpUtil.createGet(timeUrl).contentType("application/json").execute().body();
        log.info("响应数据:{}", result);

    }

}
