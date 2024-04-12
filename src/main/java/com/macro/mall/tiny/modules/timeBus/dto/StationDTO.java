package com.macro.mall.tiny.modules.timeBus.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class StationDTO {

    @ApiModelProperty("车站id")
    private Long id;

    @ApiModelProperty("路线id")
    private String lineId;

    @ApiModelProperty("站点顺序")
    private Integer stopNumber;

    @ApiModelProperty("站点名称")
    private String stopName;

    @ApiModelProperty("纬度")
    private Double latitude;

    @ApiModelProperty("经度")
    private Double longitude;
}
