package com.zmy.esdata.produce;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmy.esdata.es.EsUtil;
import com.zmy.esdata.modle.Teacher;
import com.zmy.esdata.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
//@Component
public class DataProduceExample implements ApplicationRunner {

    private static final String INDEX_NAME = "teacher";
    //@Qualifier("restHighLevelClient")
    //@Autowired
    private RestHighLevelClient client = EsUtil.client();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //createIndex();
        //deleteIndex();
        //insertDocument();
        //buckAdd();
        //queryLike();
        //queryRange();
        //queryPrefix();
        //queryWidcard();
        //queryIds();
        //queryMulti();
        //queryFuzzy();
        //queryBool();
        //queryFilter();
        //queryAggr();
        //queryAggrFilter();
        queryAggrDate();
        //queryMax();
        //queryMin();
        //queryAvg();


        System.exit(0);

    }


    public void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete);
    }


    public void insertDocument() throws IOException {
        Teacher teacher = Teacher.builder().name("shawn").age(123).date(new Date()).build();
        // 创建要添加的索引
        IndexRequest request = new IndexRequest(INDEX_NAME);
        // 添加 id 等
        request.timeout("1s").timeout(TimeValue.timeValueSeconds(1)).source(JSON.toJSONString(teacher), XContentType.JSON);
        // 发送客户端
        IndexResponse index = client.index(request, RequestOptions.DEFAULT);
        System.out.println(index.status());
        System.out.println(index);
    }


    public void buckAdd() throws IOException {

        ArrayList<Teacher> list = new ArrayList<>();
        list.add(Teacher.builder().name("shawn").age(111).date(new Date()).build());
        list.add(Teacher.builder().name("kevin").age(222).date(new Date()).build());
        list.add(Teacher.builder().name("hai").age(333).date(new Date()).build());
        list.add(Teacher.builder().name("tom").age(444).date(new Date()).build());

        BulkRequest request = new BulkRequest();
        list.forEach(s -> {
            request.timeout("10s").add(new IndexRequest(INDEX_NAME).source(JSON.toJSONString(s), XContentType.JSON));
        });
        BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println("bulk = " + bulk);
    }


    public void query1() throws IOException {
        GetRequest request = new GetRequest(INDEX_NAME, "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        //System.out.println(response);
        Teacher teacher = JSON.parseObject(response.getSourceAsString(), Teacher.class);
        System.out.println(teacher);

    }

    //https://blog.csdn.net/m0_46937429/article/details/122322505

    // 抽离出来的查询的方法
    public void query(QueryBuilder queryBuilder) throws IOException {
        // 创建 请求
        SearchRequest request = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(queryBuilder);
        request.source(builder);
        // 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 总条数
        System.out.println("总条数" + response.getHits().getTotalHits().value);
        // 最大分数
        System.out.println("最大分数" + response.getHits().getMaxScore());
        // 查询时间
        System.out.println("查询时间" + response.getTook());
        // 拿到我们的数据
        SearchHit[] hits = response.getHits().getHits();
        Arrays.stream(hits).forEach(s -> {
            System.out.println("id 是" + s.getId());
            // 具体数据
            System.out.println("具体数据" + s.getSourceAsString());
        });
    }

    // 关键词查询

    public void queryLike() throws IOException {
        query(QueryBuilders.termQuery("name", "shawn"));
    }

    // range 查询
    public void queryRange() throws IOException {
        query(QueryBuilders.rangeQuery("age").gt(1).lte(1000));
    }


    // 前缀查询
    public void queryPrefix() throws IOException {
        query(QueryBuilders.prefixQuery("name", "sha"));
    }


    public void queryWidcard() throws IOException {
        // 通配符查询 ？ 表示一个字符 * 任意多个字符
        query(QueryBuilders.wildcardQuery("name", "sh*"));
    }

    public void queryIds() throws IOException {
        // ids 多个指定 id 查询
        query(QueryBuilders.idsQuery().addIds("1"));
    }

    public void queryMulti() throws IOException {
        // multi_match 多字段查询
        query(QueryBuilders.multiMatchQuery("shawn", "name", "id"));
    }

    public void queryFuzzy() throws IOException {
        query(QueryBuilders.fuzzyQuery("name", "shawn"));
    }

    public void queryBool() throws IOException {
        query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("name", "shawn")).must(QueryBuilders.idsQuery().addIds("1")));
    }

    // 查询全部
    public void queryFilter() throws IOException {
        // 这里可以添加多个索引的名字 indixes 是加了s 的是可以添加多个 索引给他 查询的请求
        SearchRequest request = new SearchRequest(INDEX_NAME);
        // 创建构造器
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 添加搜索的条件  如果同时使用过滤跟query 的话优先是执行的 过滤的
        builder.query(QueryBuilders.matchAllQuery()).postFilter(QueryBuilders.termQuery("name", "shawn"));
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 总条数
        System.out.println("总条数" + response.getHits().getTotalHits().value);
        // 最大分数
        System.out.println("最大分数" + response.getHits().getMaxScore());
        // 查询时间
        System.out.println("查询时间" + response.getTook());
        // 拿到我们的数据
        SearchHit[] hits = response.getHits().getHits();
        Arrays.stream(hits).forEach(s -> {
            System.out.println("id 是" + s.getId());
            // 具体数据
            System.out.println("具体数据" + s.getSourceAsString());
        });

    }


    public void queryAggr() throws IOException {
        SearchRequest request = new SearchRequest(EsUtil.INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
                .aggregation(AggregationBuilders.terms("number_group").field("number.keyword").subAggregation(AggregationBuilders.max("w_group").field("w")).subAggregation(AggregationBuilders.sum("sum_group").field("w"))) // 设置聚合操作
                .size(0)
        ; // 查询条件
        request.source(searchSourceBuilder);
        // 客户端发送消息
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms aggregation = aggregations.get("number_group");
        List<? extends Terms.Bucket> list = aggregation.getBuckets();
        list.stream().forEach(s -> {
            System.out.println(s.getKey());
            System.out.println(s.getDocCount());
            ParsedMax max = s.getAggregations().get("w_group");
            System.out.println(max.getValue());

            ParsedSum sum = s.getAggregations().get("sum_group");
            System.out.println(sum.getValue());

        });

    }

    public void queryAggrFilter() throws IOException {


        SearchRequest request = new SearchRequest(EsUtil.INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
                .postFilter(QueryBuilders.rangeQuery("time").gte(TimeUtil.today()))
                .aggregation(AggregationBuilders.terms("number_group").field("number.keyword")) // 设置聚合操作
                .size(0)
        ; // 查询条件
        request.source(searchSourceBuilder);
        // 客户端发送消息
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms aggregation = aggregations.get("number_group");
        List<? extends Terms.Bucket> list = aggregation.getBuckets();
        System.out.println(list.size());

    }


    public void queryAggrDate() throws IOException {


        DateTime parse1 = DateUtil.parse("2022-04-01 00:00:00");
        Date date1 = parse1.toJdkDate();

        DateTime parse2 = DateUtil.parse("2023-04-13 23:59:59");
        Date date2 = parse2.toJdkDate();


        //QueryBuilder qBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").from(date1).to(date2)).must(QueryBuilders.termQuery("type", 1));
        QueryBuilder qBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("time").from(date1).to(date2));

        // 1、创建search请求
        SearchRequest searchRequest = new SearchRequest(EsUtil.INDEX_NAME);

        // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(qBuilder)
                .size(0)
                .timeout(new TimeValue(60000));
        //字段值项分组聚合
        //DateHistogramAggregationBuilder days = AggregationBuilders.dateHistogram("day").field("time").timeZone(ZoneId.of("Asia/Shanghai")).dateHistogramInterval(DateHistogramInterval.HOUR);
        DateHistogramAggregationBuilder days = AggregationBuilders.dateHistogram("time_group").field("time").dateHistogramInterval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.terms("type_group").field("region").subAggregation(AggregationBuilders.sum("w_sum").field("w")));


        sourceBuilder.aggregation(days);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        if (RestStatus.OK.equals(searchResponse.status())) {
            // 获取聚合结果
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedDateHistogram day = aggregations.get("time_group");
            List<? extends Histogram.Bucket> buckets1 = day.getBuckets();
            for (Histogram.Bucket bucket : buckets1) {
                System.out.println(bucket.getKey());
                System.out.println(bucket.getDocCount());
                ParsedSum wGroup = bucket.getAggregations().get("w_group");
                double value = wGroup.value();
                System.out.println(value);
                System.out.println("-----------------");
            }


            //JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(day));
            //JSONArray buckets = jsonObject.getJSONArray("buckets");
            //for (Object bucks : buckets) {
            //    JSONObject buck = (JSONObject) bucks;
            //    String keyAsString = buck.getString("keyAsString");
            //    String docCount = buck.getString("docCount");
            //    System.out.println(keyAsString + "\t-->\t" + docCount);
            //}
        }


    }


    public void queryMax() throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery())
                .aggregation(AggregationBuilders.max("age_max").field("age"));
        request.source(builder);
        // 客户端发送消息
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 打印数据
        Aggregations aggregations = response.getAggregations();
        ParsedMax aggregation = aggregations.get("age_max");
        double value = aggregation.getValue();
        System.out.println(value);

    }

    public void queryMin() throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery())
                .aggregation(AggregationBuilders.min("age_min").field("age"));
        request.source(builder);
        // 客户端发送消息
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 打印数据
        Aggregations aggregations = response.getAggregations();
        ParsedMin aggregation = aggregations.get("age_min");
        double value = aggregation.getValue();
        System.out.println(value);

    }


    // 求平均值
    public void queryAvg() throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery())
                .aggregation(AggregationBuilders.avg("age_avg").field("age"))

        ;
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        ParsedAvg contractTerm = aggregations.get("age_avg");
        System.out.println(contractTerm.getValue());
    }


}



