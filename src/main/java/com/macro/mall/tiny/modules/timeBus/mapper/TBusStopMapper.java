package com.macro.mall.tiny.modules.timeBus.mapper;

import com.macro.mall.tiny.modules.timeBus.model.TBusStop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
public interface TBusStopMapper extends BaseMapper<TBusStop> {

    @Select("SELECT DISTINCT(stop_name)  FROM t_bus_stop where longitude is null")
    List<String> selectDistinctStopName();

    int updateByStopName(String stopName, double longitude, double latitude);
}
