package com.macro.mall.tiny.modules.timeBus.service;

import com.macro.mall.tiny.modules.timeBus.dto.BusByLineIdsParam;
import com.macro.mall.tiny.modules.timeBus.dto.SearchParam;
import com.macro.mall.tiny.modules.timeBus.model.TBusLine;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
public interface TBusLineService extends IService<TBusLine> {

    String getBusData();
    String getBusDataByLineName(SearchParam searchParam);

    String getBusDataByLineIds(BusByLineIdsParam busByLineIdsParam);
}
