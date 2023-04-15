package com.zmy.zmyserver.controller;

import com.zmy.zmyserver.modle.Air;
import com.zmy.zmyserver.modle.AirBaseStatistics;
import com.zmy.zmyserver.modle.AirEleUsage;
import com.zmy.zmyserver.modle.AirsAnalyseParams;
import com.zmy.zmyserver.service.AirService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class AirController {

    @Autowired
    private AirService airService;


    @GetMapping(value = "/airs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Air> selectAll() {
        return airService.selectAll();
    }

    @GetMapping(value = "/airs-count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int count() {
        return airService.count();
    }

    @GetMapping(value = "/airs-usage-rate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int usageRate() throws IOException {
        return airService.usageRate();
    }

    @GetMapping(value = "/airs-ele-usage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AirEleUsage eleUsage() throws IOException {
        return airService.eleUsage();
    }


    @GetMapping(value = "/air", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Air get(@RequestParam String number) {
        return airService.selectByNumber(number);
    }

    @PostMapping(value = "/air", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean add(@RequestBody Air air) {
        return airService.add(air);
    }

    @PutMapping(value = "/air", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean modify(@RequestBody Air air) {
        return airService.modify(air);
    }

    @DeleteMapping(value = "/air", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean delete(@RequestParam String number) {
        return airService.delete(number);
    }

    @GetMapping(value = "/air-region-stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AirBaseStatistics> regionStat() {
        return airService.regionStat();
    }

    @GetMapping(value = "/air-building-stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AirBaseStatistics> buildingStat() {
        return airService.buildingStat();
    }

    @GetMapping(value = "/air-storey-stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AirBaseStatistics> storeyStat() {
        return airService.storeyStat();
    }

    @GetMapping(value = "/air-type-stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AirBaseStatistics> typeStat() {
        return airService.typeStat();
    }


    @PostMapping(value = "/airs-analyse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object analyse(@RequestBody AirsAnalyseParams airsAnalyseParams) {

        return null;
    }


}
