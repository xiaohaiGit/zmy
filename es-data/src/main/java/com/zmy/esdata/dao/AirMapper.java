package com.zmy.esdata.dao;


import com.zmy.esdata.modle.Air;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AirMapper {

    List<Air> selectAll();

    int add(Air air);

    int modify(Air air);

    int delete(String number);

    Air selectByNumber(String number);


    List<AirBaseStatistics> regionStat();

    List<AirBaseStatistics> buildingStat();

    List<AirBaseStatistics> storeyStat();

    List<AirBaseStatistics> typeStat();


}
