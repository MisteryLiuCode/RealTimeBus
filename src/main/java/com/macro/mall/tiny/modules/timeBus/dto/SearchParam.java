package com.macro.mall.tiny.modules.timeBus.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class SearchParam {

    @ApiModelProperty("搜索内容")
    private String searchText;

    @ApiModelProperty("所在位置经度")
    private Double longitude;

    @ApiModelProperty("所在位置纬度")
    private Double latitude;

}
