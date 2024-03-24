package com.macro.mall.tiny.modules.timeBus.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.timeBus.service.TimeBusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResult<String> timeBus857Sunhe() {
        return CommonResult.success(timeBusService.timeBus857Sunhe());
    }

    @ApiOperation(value = "201", notes = "201")
    @GetMapping(value = "/bus201")
    public CommonResult<String> timeBus201Sunhe() {
        return CommonResult.success(timeBusService.timeBus201Sunhe());
    }
}
