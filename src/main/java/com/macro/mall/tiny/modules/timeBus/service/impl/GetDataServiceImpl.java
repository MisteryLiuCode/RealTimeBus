package com.macro.mall.tiny.modules.timeBus.service.impl;

import com.alibaba.fastjson.JSON;
import com.macro.mall.tiny.modules.timeBus.dto.LineStationDTO;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusLineMapper;
import com.macro.mall.tiny.modules.timeBus.service.GetDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GetDataServiceImpl implements GetDataService {

    @Resource
    private TBusLineMapper lineMapper;


    @Override
    public String getBusData() {
        List<LineStationDTO> lineStationDTO = lineMapper.selectLineStation();
        return JSON.toJSONString(lineStationDTO);

    }
}
