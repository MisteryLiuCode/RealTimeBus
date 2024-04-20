package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;

@Data
public class RealTimeBusDTO {
    // 详情页描述:eg:第一辆车还有2站
    private String detailDesc;

    // 最近一辆车还有多久,eg:2分钟
    private Integer arriveTime;
}
