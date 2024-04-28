package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchHarmonyResult {

    List<LineStationDTO> lineStationDTOList;

    private String searchLineName;


}
