package com.macro.mall.tiny.modules.timeBus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.timeBus.dto.BusByLineIdsParam;
import com.macro.mall.tiny.modules.timeBus.model.TBusLine;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
public interface TBusLineHarmonyService extends IService<TBusLine> {

    String getBusData();
    String getBusDataByLineName(String lineName);

    String getBusDataByLineIds(BusByLineIdsParam busByLineIdsParam);
}
