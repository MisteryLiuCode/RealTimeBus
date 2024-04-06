package com.macro.mall.tiny.modules.timeBus.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LineStationDTO {

    private Long id;

    @ApiModelProperty("路线名称")
    private String lineName;

    @ApiModelProperty("起始站名称")
    private String firstStation;

    @ApiModelProperty("最后一站名称")
    private String lastStation;

    @ApiModelProperty("服务时间")
    private String serviceTime;

    @ApiModelProperty("描述")
    private String description;

    private List<StationDTO> stations;


}
