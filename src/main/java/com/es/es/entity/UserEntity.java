package com.es.es.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "userentity")
public class UserEntity {
    private String id;
    private String username;
    private Integer age;

    @Field(type = FieldType.Date,format = DateFormat.date_time_no_millis  ,pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    @Field(type = FieldType.Date,format = DateFormat.date_time_no_millis ,pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Field(type = FieldType.Date,format = DateFormat.date_time_no_millis ,pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;
    private String cityEnName;
}
