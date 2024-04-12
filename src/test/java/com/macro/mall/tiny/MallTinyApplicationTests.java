package com.macro.mall.tiny;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.timeBus.dto.LineStationDTO;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusLineMapper;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TBusLineService busLineService;

    @Autowired
    private RedisService redisService;

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
        String busData = busLineService.getBusData();
        // 结束执行时间
        long end = System.currentTimeMillis();
        log.info("查询数据结果为:{}", busData);
        log.info("执行时间:{}", end - start);
    }

    /**
     * 获取一条线路
     */
    @Test
    public void getLineStationByLineName(){
        //开始执行时间
        long start = System.currentTimeMillis();
        String busData = busLineService.getBusDataByLineName("专201");
        // 结束执行时间
        long end = System.currentTimeMillis();
        log.info("查询数据结果为:{}", JSONObject.toJSONString(CommonResult.success(busData)));
        log.info("执行时间:{}", end - start);
    }

    @Test
    public void testRedis(){
        redisService.set("testRedisKey", "1");
        Object o = redisService.get("testRedisKey");
        log.info("redis获取结果为:{}", o.toString());
    }
}
