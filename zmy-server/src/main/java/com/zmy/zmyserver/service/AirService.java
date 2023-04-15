package com.zmy.zmyserver.service;

import com.zmy.zmyserver.modle.Air;
import com.zmy.zmyserver.modle.AirBaseStatistics;
import com.zmy.zmyserver.modle.AirEleUsage;
import com.zmy.zmyserver.modle.AirsAnalyseParams;

import java.io.IOException;
import java.util.List;

public interface AirService {
    List<Air> selectAll();

    int count();

    boolean add(Air air);

    boolean modify(Air air);

    boolean delete(String number);

    Air selectByNumber(String number);

    List<AirBaseStatistics> regionStat();

    List<AirBaseStatistics> buildingStat();

    List<AirBaseStatistics> storeyStat();

    List<AirBaseStatistics> typeStat();

    int usageRate() throws IOException;

    AirEleUsage eleUsage() throws IOException;

    Object analyse(AirsAnalyseParams airsAnalyseParams);

}
