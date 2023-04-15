package com.zmy.zmyserver.service.impl;

import com.zmy.zmyserver.dao.AirMapper;
import com.zmy.zmyserver.exception.AirExistsException;
import com.zmy.zmyserver.exception.AirNotExistsException;
import com.zmy.zmyserver.modle.Air;
import com.zmy.zmyserver.modle.AirBaseStatistics;
import com.zmy.zmyserver.modle.AirEleUsage;
import com.zmy.zmyserver.modle.AirsAnalyseParams;
import com.zmy.zmyserver.service.AirService;
import com.zmy.zmyserver.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AirServiceImpl implements AirService {


    public static final String INDEX_NAME = "air-log";

    @Autowired
    private AirMapper airMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<Air> selectAll() {
        List<Air> airs = airMapper.selectAll();
        log.info("select air : {}", airs);
        return airs;
    }

    @Override
    public int count() {
        int count = airMapper.count();
        log.info("air count is : {}", count);
        return count;
    }

    @Override
    public boolean add(Air air) {
        Air airDb = airMapper.selectByNumber(air.getNumber());
        if (airDb != null) {
            throw new AirExistsException();
        }

        air.setUpdateTime(new Date());
        int count = airMapper.add(air);
        log.info("add air count : {}", count);
        return true;
    }

    @Override
    public boolean modify(Air air) {
        Air airDb = airMapper.selectByNumber(air.getNumber());
        if (airDb == null) {
            throw new AirNotExistsException();
        }

        int count = airMapper.modify(air);
        log.info("modify air count : {}", count);
        return true;
    }

    @Override
    public boolean delete(String number) {
        Air airDb = airMapper.selectByNumber(number);
        if (airDb == null) {
            throw new AirNotExistsException();
        }

        int count = airMapper.delete(number);
        log.info("delete air count : {}", count);
        return true;
    }

    @Override
    public Air selectByNumber(String number) {
        Air airDb = airMapper.selectByNumber(number);
        if (airDb == null) {
            throw new AirNotExistsException();
        }

        return airMapper.selectByNumber(number);
    }

    @Override
    public List<AirBaseStatistics> regionStat() {
        List<AirBaseStatistics> result = airMapper.regionStat();
        log.info("air region stat : {}", result);
        return result;
    }

    @Override
    public List<AirBaseStatistics> buildingStat() {
        List<AirBaseStatistics> result = airMapper.buildingStat();
        log.info("air building stat : {}", result);
        return result;
    }

    @Override
    public List<AirBaseStatistics> storeyStat() {
        List<AirBaseStatistics> result = airMapper.storeyStat();
        log.info("air storey stat : {}", result);
        return result;
    }

    @Override
    public List<AirBaseStatistics> typeStat() {
        List<AirBaseStatistics> result = airMapper.typeStat();
        log.info("air type stat : {}", result);
        return result;
    }

    @Override
    public int usageRate() throws IOException {

        int count = airMapper.count();

        SearchRequest request = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").gte(TimeUtil.today())))
                .aggregation(AggregationBuilders.terms("number_group").field("number.keyword")) // 设置聚合操作
                .size(0)
        ; // 查询条件
        request.source(searchSourceBuilder);
        // 客户端发送消息
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms aggregation = aggregations.get("number_group");
        List<? extends Terms.Bucket> list = aggregation.getBuckets();
        int usage = list.size();

        log.info("count : {} , usage : {}", count, usage);

        return usage / count * 100;
    }


    @Override
    public AirEleUsage eleUsage() throws IOException {

        int count = airMapper.count();

        SearchRequest request = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").gte(TimeUtil.today())))
                .aggregation(AggregationBuilders.sum("w_group").field("w")) // 设置聚合操作
                .size(0)
        ; // 查询条件
        request.source(searchSourceBuilder);
        // 客户端发送消息
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        ParsedSum parsedSum = aggregations.get("w_group");
        double value = parsedSum.getValue();

        log.info("count : {} , sum : {}", count, value);

        return AirEleUsage.builder().total((long) (value / 1000)).avg((int) (value / count / 1000)).build();
    }

    @Override
    public Object analyse(AirsAnalyseParams airsAnalyseParams) {
        return null;
    }


}
