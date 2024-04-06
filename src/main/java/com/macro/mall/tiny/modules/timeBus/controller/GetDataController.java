package com.macro.mall.tiny.modules.timeBus.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.timeBus.service.GetDataService;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;

@Slf4j
@Api(tags = "GetDataController")
@Tag(name = "GetDataController", description = "GetDataController")
@RestController
public class GetDataController {


    @Resource
    private GetDataService getDataService;

    @RequestMapping(value = "/getData")
    public CommonResult<String> getData() {
        String str = readJsonFile("src/main/resources/bus_lines.json");
        log.info(str);
        return CommonResult.success(str);
    }

    @RequestMapping(value = "/getBusData")
    public CommonResult<String> getBusData() {
        return CommonResult.success(getDataService.getBusData());
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
