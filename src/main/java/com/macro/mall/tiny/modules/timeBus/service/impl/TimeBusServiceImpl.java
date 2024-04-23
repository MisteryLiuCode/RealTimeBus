package com.macro.mall.tiny.modules.timeBus.service.impl;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.tiny.common.exception.Asserts;
import com.macro.mall.tiny.modules.timeBus.dto.*;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusStopMapper;
import com.macro.mall.tiny.modules.timeBus.model.TBusStop;
import com.macro.mall.tiny.modules.timeBus.service.TimeBusService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        List<TimeBusDTO> timeBusDTOS = new ArrayList<>();
        TBusStop tBusStop = tBusStopMapper.selectById(busRealTimeParam.getStationId());
        mapStaionLocation.setLatitude(tBusStop.getLatitude());
        mapStaionLocation.setLongitude(tBusStop.getLongitude());
        mapStaionLocation.setArriveStationName(tBusStop.getStopName());
        String conditionstr = busRealTimeParam.getLineId() + "-" + busRealTimeParam.getStationId();

        String timeBus201SunheUrl = "https://www.bjbus.com/api/api_etartime.php?conditionstr=" + conditionstr + "&token=" + token;
        String result = HttpUtil.createGet(timeBus201SunheUrl).contentType("application/json").execute().body();
        log.info("result:{}", result);
        TimeBus timeBus = JSONObject.parseObject(result, TimeBus.class);
        if (timeBus.getErrorCode() != 10000) {
            Asserts.fail("接口调用失败:"+timeBus.getMsg());
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
                        TimeBusDTO timeBusDTO = new TimeBusDTO();
                        timeBusDTO.setLevel(t.getIndex() + 1);
                        timeBusDTO.setStationDistance(t.getStationLeft());
                        timeBusDTO.setArriveTime(t.getEta()/60);
                        log.info("{}:第{}辆车;还有{}站", busRealTimeParam.getLineName(), t.getIndex() + 1, t.getStationLeft());
                        timeBusDTOS.add(timeBusDTO);
                    }
                }
            }
        }
        mapStaionLocation.setTimeBusDTOList(timeBusDTOS);
        log.info("{},调用实时接口结果:{}",busRealTimeParam.getLineName(),JSONObject.toJSONString(mapStaionLocation));
        return JSONObject.toJSONString(mapStaionLocation);
    }
}
