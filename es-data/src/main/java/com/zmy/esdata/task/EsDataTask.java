package com.zmy.esdata.task;

import com.zmy.esdata.modle.Student;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Component
public class EsDataTask implements CommandLineRunner {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void run(String... args) throws Exception {

        if (!createIndex()) {
            return;
        }
        //insertDocument();
        query();
    }

    public boolean createIndex() {
// 从 spring data es 4.0开始所有索引操作都在这个接口
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Student.class);
        // 是否存在，存在则删除
        if (indexOperations.exists()) {
            //indexOperations.delete();
            return true;
        }
        // 创建索引
        indexOperations.create();
        // createMapping 根据实体类获取映射关系
        // putMapping 把映射关系添加到索引中
        Document mapping = indexOperations.createMapping(Student.class);
        indexOperations.putMapping(mapping);

        return true;
    }


    public void insertDocument() {
        elasticsearchRestTemplate.save(Student.builder().name("shawn").age(111).date(new Date()).build());
        elasticsearchRestTemplate.save(Student.builder().name("kevin").age(222).date(new Date()).build());
    }


    public void query() {
        Query query = new NativeSearchQuery(QueryBuilders.matchQuery("name", "shawn"));
        SearchHits<Student> search = elasticsearchRestTemplate.search(query, Student.class);
        List<SearchHit<Student>> searchHits = search.getSearchHits();
        System.out.println(search.getTotalHits());
        List<Student> list = new ArrayList<>();
        searchHits.forEach(sh -> {
            list.add(sh.getContent());
        });
        System.out.println(list);

    }



}
