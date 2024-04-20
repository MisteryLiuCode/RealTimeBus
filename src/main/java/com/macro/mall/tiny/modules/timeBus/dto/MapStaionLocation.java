package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;

@Data
public class MapStaionLocation {
    // 预计到达站点经度
    private double longitude;
    // 预计到达站点纬度
    private double latitude;
    // 预计到达站点
    private String arriveStation;
    // 预计到达还有多少分钟
    private Integer arriveTime;
    // 地图上方描述
    private String desc;
}
