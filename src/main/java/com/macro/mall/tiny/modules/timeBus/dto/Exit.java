package com.macro.mall.tiny.modules.timeBus.dto;
import lombok.Data;
@Data
public class Exit {

    private String name;
    private String location;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setLocation(String location) {
         this.location = location;
     }
     public String getLocation() {
         return location;
     }

}