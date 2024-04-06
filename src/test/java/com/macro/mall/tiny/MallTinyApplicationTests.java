package com.macro.mall.tiny;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.macro.mall.tiny.modules.timeBus.dto.LineStationDTO;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusLineMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest
@Slf4j
public class MallTinyApplicationTests {

    @Value("${bjbus.token}")
    private String token;

    @Resource
    private TBusLineMapper lineMapper;

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

    private String getCityUrl = "https://restapi.amap.com/v3/geocode/regeo?parameters";

    @Test
    public void getCity() {
        getCityUrl += "&location=" + "116.481488,39.990464"+"&key="+"0fb664b477f15256035f259fd406defc";
        String result = HttpUtil.createGet(getCityUrl).contentType("application/json").execute().body();
        System.out.println(result);
    }

    /**
     * 获取所有路线和车站
     */
    @Test
    public void getLineStation(){
        //开始执行时间
        long start = System.currentTimeMillis();
        List<LineStationDTO> lineStationDTO = lineMapper.selectLineStation();
        // 结束执行时间
        long end = System.currentTimeMillis();
        log.info("lineStationDTO:{}", JSON.toJSON(lineStationDTO));
        log.info("执行时间:{}", end - start);
    }
}
