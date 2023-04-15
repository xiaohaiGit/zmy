package com.zmy.esdata.modle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirLog {
    private int region;
    private int type;
    private String number;
    private int building;
    private int storey;

    // 电量，单位 w
    private long w;

    private Date time;
}
