package com.macro.mall.tiny.modules.timeBus.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.timeBus.dto.BusByLineIdsParam;
import com.macro.mall.tiny.modules.timeBus.dto.LineStationDTO;
import com.macro.mall.tiny.modules.timeBus.dto.SearchHarmonyResult;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusLineMapper;
import com.macro.mall.tiny.modules.timeBus.model.TBusLine;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineHarmonyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
@Service
public class TBusLineHarmonyServiceImpl extends ServiceImpl<TBusLineMapper, TBusLine> implements TBusLineHarmonyService {

    @Value("${redis.database}")
    private String REDIS_DATABASE;

    @Value("${redis.harmonyKey.searchLine}")
    private String REDIS_HARMONY_KEY_DATA;

    private String REDIS_KEY = REDIS_DATABASE + ":" + REDIS_HARMONY_KEY_DATA;


    @Resource
    private TBusLineMapper lineMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public String getBusData() {
        // 从缓存中获取
        Object data = redisService.get(REDIS_HARMONY_KEY_DATA);
        if (data != null) {
            return data.toString();
        }

        List<LineStationDTO> lineStationDTOList = lineMapper.selectLineStation();
        String jsonData = JSON.toJSONString(lineStationDTOList);
        // 查询结果放入redis
        redisService.set(REDIS_HARMONY_KEY_DATA, jsonData);
        return jsonData;
    }

    @Override
    public String getBusDataByLineName(String searchText) {
        SearchHarmonyResult searchHarmonyResult = new SearchHarmonyResult();
        String REDIS_KEY = REDIS_DATABASE + ":" + REDIS_HARMONY_KEY_DATA + ":" + searchText;
        // 从缓存中获取
        Object data = redisService.get(REDIS_KEY);
        if (data != null) {
            return data.toString();
        }
        List<LineStationDTO> lineStationDTOList = lineMapper.selectLineStationByLineName(searchText);
        searchHarmonyResult.setLineStationDTOList(lineStationDTOList);
        searchHarmonyResult.setSearchText(searchText);
        String jsonData = JSON.toJSONString(searchHarmonyResult);
        // 查询结果放入redis
        redisService.set(REDIS_KEY, jsonData);
        return jsonData;
    }

    @Override
    public String getBusDataByLineIds(BusByLineIdsParam busByLineIdsParam) {
        Object data = redisService.get(REDIS_KEY);
        JSONObject.parseArray(data.toString(), List.class);
        return null;
    }
}
