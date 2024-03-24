package com.macro.mall.tiny.modules.timeBus.dto;

import java.util.List;
@lombok.Data
public class TimeBus {

    private String msg;
    private List<Data> data;
    private int errorCode;
}
