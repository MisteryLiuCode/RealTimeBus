package com.macro.mall.tiny;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.timeBus.dto.*;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusLineMapper;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusStopMapper;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;


@SpringBootTest
@Slf4j
public class MallTinyApplicationTests {

    @Value("${bjbus.token}")
    private String token;

    @Resource
    private TBusLineService busLineService;

    @Autowired
    private RedisService redisService;


    @Resource
    private TBusStopMapper tBusStopMapper;

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
        getCityUrl += "&location=" + "116.481488,39.990464" + "&key=" + "0fb664b477f15256035f259fd406defc";
        String result = HttpUtil.createGet(getCityUrl).contentType("application/json").execute().body();
        System.out.println(result);
    }

    /**
     * 获取所有路线和车站
     */
    @Test
    public void getLineStation() {
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
    public void getLineStationByLineName() {
        //开始执行时间
        long start = System.currentTimeMillis();
        SearchParam searchParam = new SearchParam();
        searchParam.setSearch("1");
        String busData = busLineService.getBusDataByLineName(searchParam);
        // 结束执行时间
        long end = System.currentTimeMillis();
        log.info("查询数据结果为:{}", JSONObject.toJSONString(CommonResult.success(busData)));
        log.info("执行时间:{}", end - start);
    }

    @Test
    public void testRedis() {
        redisService.set("testRedisKey", "1");
        Object o = redisService.get("testRedisKey");
        log.info("redis获取结果为:{}", o.toString());
    }

    /**
     * 获取所有站的经纬度
     */
    @Test
    public void getStationByLineIds() {
        String baseUrl = "https://restapi.amap.com/v3/geocode/geo?";
        String apiKey = "7cc79318587d2f506147dc351024ca2f";
        List<String> stopNames = tBusStopMapper.selectDistinctStopName();
        log.info("开始填充经纬度，站点数量：{}", stopNames.size());

        try {
            for (String stopName : stopNames) {
                String url = baseUrl + "address=北京市" + stopName + "公交站" + "&output=json&key=" + apiKey;
                String resp = HttpUtil.createGet(url).execute().body();
                StopLocation location = JSONObject.parseObject(resp, StopLocation.class);

                if (!"1".equals(location.getStatus()) || "0".equals(location.getCount())) {
                    log.error("{}: 查询失败, 响应: {}", stopName, resp);
                    continue; // 使用continue替代break，保证其他站点可以继续处理
                }

                Optional<Geocodes> maybeGeocode = location.getGeocodes().stream()
                        .filter(geocode -> "公交地铁站点".equals(geocode.getLevel()))
                        .findFirst();

                if (maybeGeocode.isPresent()) {
                    Geocodes geocodes = maybeGeocode.get();
                    log.debug("{}: 查询成功, 经纬度: {}", stopName, geocodes.getLocation());
                    String[] split = geocodes.getLocation().split(",");
                    tBusStopMapper.updateByStopName(stopName, Double.parseDouble(split[0]), Double.parseDouble(split[1]));
                } else {
                    log.error("{}: 无有效经纬度信息, 响应: {}", stopName, resp);
                }
            }
        } catch (Exception e) {
            log.error("处理过程中发生异常", e);
        }
    }

    /**
     * 根据目的地获取公交线路
     * 请求示例
     * https://restapi.amap.com/v5/direction/transit/integrated?
     * origin=116.466485,39.995197&destination=116.46424,40.020642&key=<用户的key>&city1=010&city2=010
     */
    @Test
    public void getLineByDestination() {

        // 我的位置经纬度
        String myLocation = "116.543672,40.035133";

        // 终点经纬度
        String destination = "116.487105,40.0032";


        String baseUrl = "https://restapi.amap.com/v5/direction/transit/integrated?";

        String apiKey = "7cc79318587d2f506147dc351024ca2f";

        String url = baseUrl + "origin=" + myLocation + "&destination=" + destination + "&key=" + apiKey + "&city1=010&city2=010";

        String resp = HttpUtil.createGet(url).execute().body();

        LineDestination lineDestination = JSONObject.parseObject(resp, LineDestination.class);

        /**
         * - 结果集拿到root
         * - 拿到transits
         * - 拿到每一个segments
         * - 拿到每一个bus
         * - 筛选type为公交的busLine
         */
        List<String> busLineNameList = lineDestination.getRoute().getTransits().stream()
                .flatMap(transits -> transits.getSegments().stream())
                .map(Segments::getBus)
                .flatMap(bus -> {
                            if (bus != null && bus.getBuslines() != null) {
                                return bus.getBuslines().stream();
                            } else {
                                return Stream.empty();
                            }
                        }
                )
                .filter(buslines -> buslines.getType().contains("公交"))
                .map(busInfo ->{
                    busInfo.setName(removeParenthesesAndLastChar(busInfo.getName()));
                    return busInfo.getName();
                })
                .collect(Collectors.toList());

        log.info("获取的所有公交线路名称: {}", JSONObject.toJSONString(busLineNameList));
    }

    public static String removeParenthesesAndLastChar(String input) {
        // 使用正则表达式去除括号及其内容
        input = input.replaceAll("\\(.*?\\)", "");
        // 去除最后一个字符
        return input.substring(0, input.length() - 1);
    }
}
