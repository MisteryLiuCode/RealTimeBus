package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;

import java.util.List;

@Data
public class StopLocation {

    private String status;
    private String info;
    private String infocode;
    private String count;
    private List<Geocodes> geocodes;
}
