package com.macro.mall.tiny.modules.timeBus.mapper;

import com.macro.mall.tiny.modules.timeBus.dto.LineStationDTO;
import com.macro.mall.tiny.modules.timeBus.model.TBusLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
public interface TBusLineMapper extends BaseMapper<TBusLine> {

    List<LineStationDTO> selectLineStation();
}
