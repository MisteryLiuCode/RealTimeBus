package com.macro.mall.tiny.modules.timeBus.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.timeBus.dto.BusByLineIdsParam;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;

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

    // 拿到搜索的线路
    @ApiOperation(value = "以线路名称搜索列表")
    @GetMapping(value = "/getBusDataByLineName/{lineName}")
    public CommonResult<String> getBusDataByLineName(@PathVariable String lineName) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        String res = busLineService.getBusDataByLineName(lineName);
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 运行时间
        log.info("运行时间：{}", endTime - startTime);
        return CommonResult.success(res);
    }

    /**
     * 批量lineId查询线路
     */
    @GetMapping(value = "/getBusDataByLineIds")
    public CommonResult<String> getBusDataByLineIds(@RequestBody BusByLineIdsParam busByLineIdsParam) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        String res = busLineService.getBusDataByLineIds(busByLineIdsParam);
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 运行时间
        log.info("运行时间：{}", endTime - startTime);
        return CommonResult.success(res);
    }

    @RequestMapping(value = "/getDataByJsonFile")
    public String getData() {
        String str = readJsonFile("src/main/resources/bus_lines.json");
        log.info(str);
        return str;
    }

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

