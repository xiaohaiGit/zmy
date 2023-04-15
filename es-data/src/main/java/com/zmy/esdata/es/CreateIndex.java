package com.zmy.esdata.es;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.io.IOException;

public class CreateIndex {


    public static void main(String[] args) throws IOException {

        RestHighLevelClient client = EsUtil.client();

        CreateIndexRequest request = new CreateIndexRequest(EsUtil.INDEX_NAME);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);

        client.close();

    }


}
