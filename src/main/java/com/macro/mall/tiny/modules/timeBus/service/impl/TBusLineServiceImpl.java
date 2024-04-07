package com.macro.mall.tiny.modules.timeBus.service.impl;

import com.alibaba.fastjson.JSON;
import com.macro.mall.tiny.common.service.RedisService;
import com.macro.mall.tiny.modules.timeBus.dto.LineStationDTO;
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
 *  服务实现类
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


    @Resource
    private TBusLineMapper lineMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public String getBusData() {
        String REDIS_KEY = REDIS_DATABASE + ":" + REDIS_KEY_DATA;
        // 从缓存中获取
        Object data = redisService.get(REDIS_KEY);
        if (data != null) {
            return (String)data;
        }

        List<LineStationDTO> lineStationDTOList = lineMapper.selectLineStation();
        String jsonData = JSON.toJSONString(lineStationDTOList);
        // 查询结果放入redis
        redisService.set(REDIS_KEY, jsonData);
        return jsonData;
    }
}
