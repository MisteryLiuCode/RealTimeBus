package com.macro.mall.tiny.modules.timeBus.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.timeBus.dto.BusRealTimeParam;
import com.macro.mall.tiny.modules.timeBus.service.TimeBusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "TimeBusController")
@Tag(name = "TimeBusController", description = "TimeBusController")
@RequestMapping("/timeBus")
@RestController
public class TimeBusController {

    @Resource
    private TimeBusService timeBusService;

    @ApiOperation(value = "857", notes = "857")
    @GetMapping(value = "/bus857")
    @ApiIgnore
    public CommonResult<String> timeBus857Sunhe() {
        return CommonResult.success(timeBusService.timeBus857Sunhe());
    }

    @ApiOperation(value = "201", notes = "201")
    @GetMapping(value = "/bus201")
    @ApiIgnore
    public CommonResult<String> timeBus201Sunhe() {
        return CommonResult.success(timeBusService.timeBus201Sunhe());
    }

    /**
     * 获取实时数据通用接口
     */
    @ApiOperation(value = "实时数据通用接口", notes = "实时数据通用接口")
    @RequestMapping(value = "/busRealtime", method = RequestMethod.POST)
    public CommonResult<String> busRealtime(@RequestBody BusRealTimeParam busRealTimeParam) {
        return CommonResult.success(timeBusService.busRealtime(busRealTimeParam));
    }
}
