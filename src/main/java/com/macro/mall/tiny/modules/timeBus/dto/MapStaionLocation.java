package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;

import java.util.List;

@Data
public class MapStaionLocation {
    // 到达站点经度
    private double longitude;
    // 到达站点纬度
    private double latitude;
    // 到达站点名称
    private String arriveStationName;
    // 所有实时车辆
    List<TimeBusDTO> timeBusDTOList;
}
