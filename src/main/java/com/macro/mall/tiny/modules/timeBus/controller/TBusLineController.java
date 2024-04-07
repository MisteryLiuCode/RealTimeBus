package com.macro.mall.tiny.modules.timeBus.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
@Slf4j
@Api(tags = "TBusLineController")
@Tag(name = "TBusLineController", description = "TBusLineController")
@RestController
@RequestMapping("/timeBus/tBusLine")
public class TBusLineController {

    @Resource
    private TBusLineService busLineService;

    @RequestMapping(value = "/getBusData")
    public CommonResult<String> getBusData() {
        // 开始时间
        long startTime = System.currentTimeMillis();
        String res = busLineService.getBusData();
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 运行时间
        log.info("运行时间：{}", endTime - startTime);
        return CommonResult.success(res);
    }

}

