package com.macro.mall.tiny.modules.timeBus.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author misteryliu
 * @since 2024-04-06
 */
@Getter
@Setter
@TableName("t_bus_line")
@ApiModel(value = "TBusLine对象", description = "")
public class TBusLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty("路线名称")
    private String lineName;

    @ApiModelProperty("起始站名称")
    private String firstStationName;

    @ApiModelProperty("最后一站名称")
    private String lastStationName;

    @ApiModelProperty("服务时间")
    private String serviceTime;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date creatime;

    @ApiModelProperty("更新时间")
    private Date updatetime;


}
