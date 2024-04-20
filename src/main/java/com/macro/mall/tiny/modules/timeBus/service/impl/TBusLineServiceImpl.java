package com.macro.mall.tiny.modules.timeBus.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.timeBus.dto.BusByLineIdsParam;
import com.macro.mall.tiny.modules.timeBus.dto.LineStationDTO;
import com.macro.mall.tiny.modules.timeBus.dto.SearchResult;
import com.macro.mall.tiny.modules.timeBus.model.TBusLine;
import com.macro.mall.tiny.modules.timeBus.mapper.TBusLineMapper;
import com.macro.mall.tiny.modules.timeBus.service.TBusLineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TBusLineServiceImpl extends ServiceImpl<TBusLineMapper, TBusLine> implements TBusLineService {

    @Value("${redis.database}")
    private String REDIS_DATABASE;

    @Value("${redis.key.data}")
    private String REDIS_KEY_DATA;

    private String REDIS_KEY = REDIS_DATABASE + ":" + REDIS_KEY_DATA;


    @Resource
    private TBusLineMapper lineMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public String getBusData() {
        // 从缓存中获取
        Object data = redisService.get(REDIS_KEY);
        if (data != null) {
            return data.toString();
        }

        List<LineStationDTO> lineStationDTOList = lineMapper.selectLineStation();
        String jsonData = JSON.toJSONString(lineStationDTOList);
        // 查询结果放入redis
        redisService.set(REDIS_KEY, jsonData);
        return jsonData;
    }

    @Override
    public String getBusDataByLineName(String lineName) {
        SearchResult searchResult = new SearchResult();
        String REDIS_KEY = REDIS_DATABASE + ":" + REDIS_KEY_DATA + ":" + lineName;
        // 从缓存中获取
        Object data = redisService.get(REDIS_KEY);
        if (data != null) {
            return data.toString();
        }
        List<LineStationDTO> lineStationDTOList = lineMapper.selectLineStationByLineName(lineName);
        searchResult.setLineStationDTOList(lineStationDTOList);
        searchResult.setSearchLineName(lineName);
        String jsonData = JSON.toJSONString(searchResult);
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
