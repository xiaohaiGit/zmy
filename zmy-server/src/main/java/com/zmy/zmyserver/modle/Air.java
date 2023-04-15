package com.zmy.zmyserver.modle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Air {
    private int id;
    private int region;
    private int type;
    private String number;
    private int building;
    private int storey;
    private int status;
    private String desc;
    private Date createTime;
    private Date updateTime;
}
