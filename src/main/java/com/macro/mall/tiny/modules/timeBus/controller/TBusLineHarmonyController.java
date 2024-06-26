package com.macro.mall.tiny.modules.timeBus.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.timeBus.dto.BusByLineIdsParam;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineHarmonyService;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
//@Api(tags = "TBusLineHarmonyController")
//@Tag(name = "TBusLineHarmonyController", description = "TBusLineHarmonyController")
@RestController
@RequestMapping("/timeBus/tBusLineHarmony")
public class TBusLineHarmonyController {

    @Resource
    private TBusLineHarmonyService busLineHarmonyService;

    @RequestMapping(value = "/getBusData")
    @ApiIgnore
    public CommonResult<String> getBusData() {
        // 开始时间
        long startTime = System.currentTimeMillis();
        String res = busLineHarmonyService.getBusData();
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 运行时间
        log.info("运行时间：{}", endTime - startTime);
        return CommonResult.success(res);
    }

    // 拿到搜索的线路
    @ApiOperation(value = "以线路名称搜索列表")
    @RequestMapping(value = "/getBusDataByLineName/{searchText}",method = RequestMethod.GET)
    @ApiIgnore
    public CommonResult<String> getBusDataByLineName(@PathVariable String searchText) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        String res = busLineHarmonyService.getBusDataByLineName(searchText);
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 运行时间
        log.info("运行时间：{}", endTime - startTime);
        return CommonResult.success(res);
    }

    /**
     * 批量lineId查询线路
     */
    @ApiIgnore
    @GetMapping(value = "/getBusDataByLineIds")
    public CommonResult<String> getBusDataByLineIds(@RequestBody BusByLineIdsParam busByLineIdsParam) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        String res = busLineHarmonyService.getBusDataByLineIds(busByLineIdsParam);
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 运行时间
        log.info("运行时间：{}", endTime - startTime);
        return CommonResult.success(res);
    }

    @RequestMapping(value = "/getDataByJsonFile")
    @ApiIgnore
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

