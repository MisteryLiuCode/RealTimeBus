package com.macro.mall.tiny.modules.timeBus.service.impl;

import cn.hutool.http.HttpUtil;
import com.macro.mall.tiny.modules.timeBus.dto.LocationInfoParam;
import com.macro.mall.tiny.modules.timeBus.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MapServiceImpl implements MapService {

    private String getCityUrl = "https://restapi.amap.com/v3/geocode/regeo?parameters";

    @Override
    public String getCity(LocationInfoParam locationInfoParam) {
        getCityUrl += "&location=" + locationInfoParam.getLongitude() + "," + locationInfoParam.getLatitude()+"&key="+"0fb664b477f15256035f259fd406defc";
        String result = HttpUtil.createGet(getCityUrl).contentType("application/json").execute().body();


        return null;
    }
}
