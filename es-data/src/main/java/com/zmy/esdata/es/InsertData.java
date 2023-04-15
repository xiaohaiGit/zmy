package com.zmy.esdata.es;

import com.alibaba.fastjson.JSON;
import com.zmy.esdata.dao.AirMapper;
import com.zmy.esdata.modle.Air;
import com.zmy.esdata.modle.AirLog;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class InsertData implements ApplicationRunner {

    @Autowired
    private AirMapper airMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        RestHighLevelClient client = EsUtil.client();


        String start = "2021-01-01 00:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        Date startDate = dateFormat.parse(start);
        long startTime = startDate.getTime();

        String endDate = "2023-04-15 23:59:59";
        Date date = dateFormat.parse(endDate);
        long endTime = date.getTime();

        List<Air> airs = airs();
        long count = (endTime - startTime) / 100000;
        long sum = 0;
        for (long i = 0; i < count; i++) {
            long insertTime = startTime + i * 100000;
            Date datetime = new Date(insertTime);

            ArrayList<AirLog> airLogs = new ArrayList<>();
            for (Air air : airs) {
                long w = getW();
                if (w == 25 || w == 30 | w == 35) {
                    w = 0;
                }
                AirLog airLog = AirLog.builder()
                        .region(air.getRegion())
                        .type(air.getType())
                        .number(air.getNumber())
                        .building(air.getBuilding())
                        .storey(air.getStorey())
                        .time(datetime)
                        .w(w)
                        .build();
                airLogs.add(airLog);
                sum++;
            }

            batchInsert(airLogs, client);
            log.info("insert time : {} , sum : {}", insertTime, sum);
            airLogs.clear();

        }
        client.close();
    }


    private void batchInsert(ArrayList<AirLog> airLogs, RestHighLevelClient client) throws IOException {
        BulkRequest request = new BulkRequest();
        airLogs.forEach(s -> {
            request.timeout("10s").add(new IndexRequest(EsUtil.INDEX_NAME).source(JSON.toJSONString(s), XContentType.JSON));
        });
        BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
        //System.out.println("bulk = " + bulk);

    }


    private List<Air> airs() {
        List<Air> airs = airMapper.selectAll();
        return airs;
    }


    private long getW() {
        Random rand = new Random();
        return rand.nextInt(20) + 20;
    }


}
