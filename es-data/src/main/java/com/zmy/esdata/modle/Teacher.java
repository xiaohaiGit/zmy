package com.zmy.esdata.modle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "teacher", shards = 1, replicas = 1)
public class Teacher {

    //@Id
    //private int id;
    @Field(type = FieldType.Keyword, fielddata = true)
    private String name;
    @Field(type = FieldType.Keyword)
    private int age;

    @Field(type = FieldType.Date)
    private Date date;

}
