package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;
@Data
public class LineDestination {

    private String status;
    private String info;
    private String infocode;
    private Route route;
    private String count;
}