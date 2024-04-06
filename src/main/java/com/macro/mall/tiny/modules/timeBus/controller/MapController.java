package com.macro.mall.tiny.modules.timeBus.controller;


import com.macro.mall.tiny.modules.timeBus.dto.LocationInfoParam;
import com.macro.mall.tiny.modules.timeBus.service.MapService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "MapController")
@Tag(name = "MapController", description = "MapController")
@RequestMapping("/timeBus")
@RestController
public class MapController {

    @Resource
    private MapService mapService;
    /**
     * 经纬度获取城市
     */
    @RequestMapping("/getCity")
    public String getCity(@RequestBody LocationInfoParam location ) {
        mapService.getCity(location);
        return null;
    }



}
