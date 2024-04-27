
package com.macro.mall.tiny.modules.timeBus.dto;
import java.util.List;
import lombok.Data;
@Data
public class Buslines {

    private Departure_stop departure_stop;
    private Arrival_stop arrival_stop;
    private String name;
    private String id;
    private String type;
    private String bustimetag;
    private String start_time;
    private String end_time;
    private String via_num;
    private List<Via_stops> via_stops;


}