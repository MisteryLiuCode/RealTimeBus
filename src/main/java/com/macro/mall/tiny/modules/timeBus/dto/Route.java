
package com.macro.mall.tiny.modules.timeBus.dto;
import lombok.Data;

import java.util.List;

@Data
public class Route {

    private String origin;
    private String destination;
    private String distance;
    private List<Transits> transits;
}