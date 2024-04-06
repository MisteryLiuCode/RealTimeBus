package com.macro.mall.tiny.modules.timeBus.service;

import com.macro.mall.tiny.modules.timeBus.dto.LocationInfoParam;
import org.springframework.stereotype.Service;


public interface MapService {

    String getCity(LocationInfoParam locationInfoParam);
}
