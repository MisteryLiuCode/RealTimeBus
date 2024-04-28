package com.macro.mall.tiny.modules.timeBus.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.timeBus.dto.*;
import com.macro.mall.tiny.modules.timeBus.model.TBusLine;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusLineMapper;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
@Service
@Slf4j
public class TBusLineServiceImpl extends ServiceImpl<TBusLineMapper, TBusLine> implements TBusLineService {

    @Value("${redis.database}")
    private String REDIS_DATABASE;

    @Value("${redis.iosKey.searchLine}")
    private String REDIS_KEY_DATA;

    private String REDIS_KEY = REDIS_DATABASE + ":" + REDIS_KEY_DATA;


    @Resource
    private TBusLineMapper lineMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public String getBusData() {
        // 从缓存中获取
        Object data = redisService.get(REDIS_KEY);
        if (data != null) {
            return data.toString();
        }

        List<LineStationDTO> lineStationDTOList = lineMapper.selectLineStation();
        String jsonData = JSON.toJSONString(lineStationDTOList);
        // 查询结果放入redis
        redisService.set(REDIS_KEY, jsonData);
        return jsonData;
    }

    @Override
    public String getBusDataByLineName(SearchParam searchParam) {
//        String REDIS_KEY = REDIS_DATABASE + ":" + REDIS_KEY_DATA + ":" + searchParam;
//        // 从缓存中获取
//        Object data = redisService.get(REDIS_KEY);
//        if (data != null) {
//            return data.toString();
//        }
        SearchResult searchResult = new SearchResult();
        // 从数据库里查询
        List<LineStationDTO> lineStationDTOList;
        lineStationDTOList = lineMapper.selectLineStationByLineName(searchParam.getSearchText(), null);
//        if (lineStationDTOList.size() == 0) {
//            log.info("开始按照目的地搜索");
//            // 调用高德目的地搜索
//            String destinationLonLat = getStationByLineIds(searchParam.getSearchText());
//            if (StringUtils.isNotBlank(destinationLonLat)) {
//                List<String> lineDescList = getLineByDestination(searchParam, destinationLonLat);
//                log.info("按照目的地搜索的线路：" + JSONObject.toJSONString(lineDescList));
//                lineStationDTOList = lineMapper.selectLineStationByLineName(null, lineDescList);
//                log.info("按照目的地搜索的线路结果：" + JSONObject.toJSONString(lineStationDTOList));
//            }
//        }
        String jsonData = "";
        if (lineStationDTOList.size() != 0) {
            searchResult.setSearchText(searchParam.getSearchText());
            searchResult.setLineStationsList(lineStationDTOList);
            jsonData = JSON.toJSONString(searchResult);
            // 查询结果放入redis
            redisService.set(REDIS_KEY, jsonData);
        }
        return jsonData;
    }

    @Override
    public String getBusDataByLineIds(BusByLineIdsParam busByLineIdsParam) {
        Object data = redisService.get(REDIS_KEY);
        JSONObject.parseArray(data.toString(), List.class);
        return null;
    }


    public List<String> getLineByDestination(SearchParam searchParam, String destinationLonLat) {
        // 我的位置经纬度
        String myLocation = searchParam.getLongitude() + "," + searchParam.getLatitude();

        String baseUrl = "https://restapi.amap.com/v5/direction/transit/integrated?";

        String apiKey = "7cc79318587d2f506147dc351024ca2f";

        String url = baseUrl + "origin=" + myLocation + "&destination=" + destinationLonLat + "&key=" + apiKey + "&city1=010&city2=010";

        String resp = HttpUtil.createGet(url).execute().body();

        LineDestination lineDestination = JSONObject.parseObject(resp, LineDestination.class);

        /**
         * - 结果集拿到root
         * - 拿到transits
         * - 拿到每一个segments
         * - 拿到每一个bus
         * - 筛选type为公交的busLine
         */
        List<String> busDescList = lineDestination.getRoute().getTransits().stream().flatMap(transits -> transits.getSegments().stream()).map(Segments::getBus).flatMap(bus -> {
            if (bus != null && bus.getBuslines() != null) {
                return bus.getBuslines().stream();
            } else {
                return Stream.empty();
            }
        }).filter(buslines -> buslines.getType().contains("公交")).map(busInfo -> {
            busInfo.setName(processString(busInfo.getName()));
            return busInfo.getName();
        }).collect(Collectors.toList());

        log.info("获取的所有公交线路名称: {}", JSONObject.toJSONString(busDescList));
        return busDescList;
    }


    /**
     * 获取一个地方经纬度
     */
    public String getStationByLineIds(String placeName) {
        String baseUrl = "https://restapi.amap.com/v3/geocode/geo?";
        String apiKey = "7cc79318587d2f506147dc351024ca2f";
        log.info("需要地理编码的位置", placeName);

        try {
            String url = baseUrl + "address=北京市" + placeName + "&cityCode=010" + "&output=json&key=" + apiKey;
            log.info("获取一个地方的经纬度,请求地址: {}", url);
            String resp = HttpUtil.createGet(url).execute().body();
            StopLocation location = JSONObject.parseObject(resp, StopLocation.class);

            if (!"1".equals(location.getStatus()) || "0".equals(location.getCount())) {
                log.error("{}: 查询失败, 响应: {}", placeName, resp);
                return "";
            }

            Optional<Geocodes> maybeGeocode = location.getGeocodes().stream().findFirst();

            if (maybeGeocode.isPresent()) {
                Geocodes geocodes = maybeGeocode.get();
                log.debug("{}: 查询成功, 经纬度: {}", placeName, geocodes.getLocation());
                return geocodes.getLocation();
            } else {
                log.error("{}: 无有效经纬度信息, 响应: {}", placeName, resp);
            }
        } catch (Exception e) {
            log.error("处理过程中发生异常", e);
        }
        return "";
    }

    public static String processString(String input) {
        // 删除"路"，确保它位于一个括号"("之前
        input = input.replaceAll("路(?=\\()", "");
        // 将两个连续的"-"替换为一个"-"
        input = input.replaceAll("--", "-");
        return input;
    }
}
