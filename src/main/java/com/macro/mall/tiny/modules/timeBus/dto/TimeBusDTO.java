package com.macro.mall.tiny.modules.timeBus.dto;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class TimeBusDTO {

    @ApiModelProperty("距离到站顺序，eg:1,第一辆车")
    private Integer level;

    @ApiModelProperty("距离到站距离，eg:1,一站")
    private Integer stationDistance;

    @ApiModelProperty("距离到达时间，eg:1,1分钟到站")
    private Integer arriveTime;
}
