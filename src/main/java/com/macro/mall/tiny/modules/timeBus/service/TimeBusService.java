package com.macro.mall.tiny.modules.timeBus.service;

import com.macro.mall.tiny.modules.timeBus.dto.BusRealTimeParam;

public interface TimeBusService {


    String timeBus857Sunhe();

    String timeBus201Sunhe();

    String busRealtime(BusRealTimeParam busRealTimeParam);

    String getStaionLocation(BusRealTimeParam busRealTimeParam);
}
