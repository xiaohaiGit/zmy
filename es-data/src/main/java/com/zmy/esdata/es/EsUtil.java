package com.zmy.esdata.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class EsUtil {


    public static final String INDEX_NAME = "air-log";

    public static RestHighLevelClient client() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("172.16.11.226", 9200, "http"))
        );
        return client;
    }
}
