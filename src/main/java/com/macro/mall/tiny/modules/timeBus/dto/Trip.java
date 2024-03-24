package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;

@Data
public class Trip {

    private String cd;
    private int eta;
    private int distance;
    private int index;
    private int stationLeft;
}
