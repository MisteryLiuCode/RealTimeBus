package com.macro.mall.tiny.modules.timeBus.service.impl;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.tiny.modules.timeBus.dto.Data;
import com.macro.mall.tiny.modules.timeBus.dto.TimeBus;
import com.macro.mall.tiny.modules.timeBus.dto.Trip;
import com.macro.mall.tiny.modules.timeBus.service.TimeBusService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TimeBusServiceImpl implements TimeBusService {

    @Value("${bjbus.token}")
    private String token;

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
                    res.append("第").append(i+1).append("辆车还有").append(t.getStationLeft()).append("站;");
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
}
