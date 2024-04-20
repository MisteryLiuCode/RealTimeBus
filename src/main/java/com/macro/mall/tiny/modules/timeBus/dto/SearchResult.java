package com.macro.mall.tiny.modules.timeBus.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {

    List<LineStationDTO> lineStationDTOList;

    private String searchLineName;


}
