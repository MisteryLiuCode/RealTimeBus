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
@TableName("t_bus_stop")
@ApiModel(value = "TBusStop对象", description = "")
public class TBusStop implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("车站id")
    private String id;

    @ApiModelProperty("路线id")
    private String lineId;

    @ApiModelProperty("站点顺序")
    private Integer stopNumber;

    @ApiModelProperty("站点名称")
    private String stopName;

    @ApiModelProperty("创建时间")
    private Date createtime;

    @ApiModelProperty("更新时间")
    private Date updatetime;


}
