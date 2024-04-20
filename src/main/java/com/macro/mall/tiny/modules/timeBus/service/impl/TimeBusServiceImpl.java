package com.macro.mall.tiny.modules.timeBus.service.impl;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.tiny.modules.timeBus.dto.*;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusStopMapper;
import com.macro.mall.tiny.modules.timeBus.model.TBusStop;
import com.macro.mall.tiny.modules.timeBus.service.TimeBusService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TimeBusServiceImpl implements TimeBusService {

    @Value("${bjbus.token}")
    private String token;

    @Resource
    private TBusStopMapper tBusStopMapper;

    @Override
    public String timeBus857Sunhe() {
        String timeBus857SunheUrl = "https://www.bjbus.com/api/api_etartime.php?conditionstr=000000058250561-110100013290008&token=" + token;
        String result = HttpUtil.createGet(timeBus857SunheUrl).contentType("application/json").execute().body();
        log.info("result:{}", result);
        TimeBus timeBus = JSONObject.parseObject(result, TimeBus.class);
        StringBuilder res = new StringBuilder();
        if (timeBus.getErrorCode() != 10000) {
            res.append("接口调用失败:").append(timeBus.getMsg());
        }
        Data data = timeBus.getData().get(0);
        if (Objects.nonNull(data.getDatas())) {
            List<Trip> trips = data.getDatas().getTrip();
            if (CollectionUtils.isNotEmpty(trips)) {
                Collections.sort(trips, (t1, t2) -> {
                    // 降序排序，如果你想要升序，只需交换t1和t2的位置
                    return Integer.compare(t2.getIndex(), t1.getIndex());
                });
                for (int i = 0; i < trips.size(); i++) {
                    Trip t = trips.get(i);
                    log.info("857第:{} 辆车;还有:{}站", t.getIndex() + 1, t.getStationLeft());
                    res.append("第").append(i + 1).append("辆车还有").append(t.getStationLeft()).append("站;");
                }
            } else {
                res.append("没有车");
            }
        }

        return res.toString();
    }

    @Override
    public String timeBus201Sunhe() {
        String timeBus201SunheUrl = "https://www.bjbus.com/api/api_etartime.php?conditionstr=007670584345180-900000183523012&token=" + token;
        String result = HttpUtil.createGet(timeBus201SunheUrl).contentType("application/json").execute().body();
        log.info("result:{}", result);
        TimeBus timeBus = JSONObject.parseObject(result, TimeBus.class);
        StringBuilder res = new StringBuilder();
        if (timeBus.getErrorCode() != 10000) {
            res.append("接口调用失败:").append(timeBus.getMsg());
        }
        if (CollectionUtils.isNotEmpty(timeBus.getData())) {
            Data data = timeBus.getData().get(0);
            if (Objects.nonNull(data.getDatas())) {
                List<Trip> trips = data.getDatas().getTrip();
                if (trips != null) {
                    Collections.sort(trips, (t1, t2) -> {
                        // 降序排序，如果你想要升序，只需交换t1和t2的位置
                        return Integer.compare(t2.getIndex(), t1.getIndex());
                    });
                    for (int i = 0; i < trips.size(); i++) {
                        Trip t = trips.get(i);
                        log.info("专201第{}辆车;还有{}站", t.getIndex() + 1, t.getStationLeft());
                        res.append("第").append(i + 1).append("辆车还有").append(t.getStationLeft()).append("站;");
                    }
                } else {
                    res.append("没有车");
                }
            }
        }
        return res.toString();
    }

    @Override
    public String busRealtime(BusRealTimeParam busRealTimeParam) {

        RealTimeBusDTO realTimeBusDTO = new RealTimeBusDTO();
        String conditionstr = busRealTimeParam.getLineId() + "-" + busRealTimeParam.getStationId();

        String timeBus201SunheUrl = "https://www.bjbus.com/api/api_etartime.php?conditionstr=" + conditionstr + "&token=" + token;
        String result = HttpUtil.createGet(timeBus201SunheUrl).contentType("application/json").execute().body();
        log.info("result:{}", result);
        TimeBus timeBus = JSONObject.parseObject(result, TimeBus.class);
        StringBuilder res = new StringBuilder();
        if (timeBus.getErrorCode() != 10000) {
            res.append("接口调用失败:").append(timeBus.getMsg());
        }
        Trip firstTrip = new Trip();
        if (CollectionUtils.isNotEmpty(timeBus.getData())) {
            Data data = timeBus.getData().get(0);
            if (Objects.nonNull(data.getDatas())) {
                List<Trip> trips = data.getDatas().getTrip();
                if (trips != null) {
                    Collections.sort(trips, (t1, t2) -> {
                        // 降序排序，如果你想要升序，只需交换t1和t2的位置
                        return Integer.compare(t2.getIndex(), t1.getIndex());
                    });
                    firstTrip = trips.get(0);
                    for (int i = 0; i < trips.size(); i++) {
                        Trip t = trips.get(i);
                        log.info("{}第{}辆车;还有{}站", busRealTimeParam, t.getIndex() + 1, t.getStationLeft());
                        res.append("第").append(i + 1).append("辆车还有").append(t.getStationLeft()).append("站;");
                    }
                } else {
                    res.append("没有车");
                }
            }
        }
        realTimeBusDTO.setDetailDesc(res.toString());
        realTimeBusDTO.setArriveTime(firstTrip.getEta()/60);
        log.info("实时到站信息结果:{}", JSONObject.toJSONString(realTimeBusDTO));
        return JSONObject.toJSONString(realTimeBusDTO);
    }

    @Override
    public String getStaionLocation(BusRealTimeParam busRealTimeParam) {
        MapStaionLocation mapStaionLocation = new MapStaionLocation();
        TBusStop tBusStop = tBusStopMapper.selectById(busRealTimeParam.getStationId());

        String conditionstr = busRealTimeParam.getLineId() + "-" + busRealTimeParam.getStationId();

        String timeBus201SunheUrl = "https://www.bjbus.com/api/api_etartime.php?conditionstr=" + conditionstr + "&token=" + token;
        String result = HttpUtil.createGet(timeBus201SunheUrl).contentType("application/json").execute().body();
        log.info("result:{}", result);
        TimeBus timeBus = JSONObject.parseObject(result, TimeBus.class);
        StringBuilder res = new StringBuilder();
        if (timeBus.getErrorCode() != 10000) {
            res.append("接口调用失败:").append(timeBus.getMsg());
        }

        if (CollectionUtils.isNotEmpty(timeBus.getData())) {
            Data data = timeBus.getData().get(0);
            if (Objects.nonNull(data.getDatas())) {
                List<Trip> trips = data.getDatas().getTrip();
                if (trips != null) {
                    Collections.sort(trips, (t1, t2) -> {
                        // 降序排序，如果你想要升序，只需交换t1和t2的位置
                        return Integer.compare(t2.getIndex(), t1.getIndex());
                    });
                    // 最近一辆车
                    Trip trip = trips.get(0);
                    if (tBusStop != null) {
                        StringBuilder desc = new StringBuilder("预计到达");
                        desc.append(tBusStop.getStopName());
                        desc.append("还有");
                        desc.append(trip.getEta() / 60);
                        desc.append("分钟");

                        mapStaionLocation.setDesc(desc.toString());
                        mapStaionLocation.setLatitude(tBusStop.getLatitude());
                        mapStaionLocation.setLongitude(tBusStop.getLongitude());
                        mapStaionLocation.setArriveStation(tBusStop.getStopName());
                        mapStaionLocation.setArriveTime(trip.getEta() / 60);
                    }
                    for (int i = 0; i < trips.size(); i++) {
                        Trip t = trips.get(i);
                        log.info("{}第{}辆车;还有{}站", busRealTimeParam, t.getIndex() + 1, t.getStationLeft());
                        res.append("第").append(i + 1).append("辆车还有").append(t.getStationLeft()).append("站;");
                    }
                } else {
                    if (tBusStop != null) {
                        mapStaionLocation.setDesc("没有车");
                        mapStaionLocation.setLatitude(tBusStop.getLatitude());
                        mapStaionLocation.setLongitude(tBusStop.getLongitude());
                    }
                }
            }
        }
        log.info("获取站点地图结果:{}", JSONObject.toJSONString(mapStaionLocation));
        return JSONObject.toJSONString(mapStaionLocation);
    }
}
