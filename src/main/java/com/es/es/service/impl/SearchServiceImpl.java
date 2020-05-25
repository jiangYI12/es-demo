package com.es.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.es.es.entity.UserEntity;
import com.es.es.service.ICustomSearchService;
import com.es.es.service.ISearchService;
import com.es.es.vo.UserEntityVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchDateConverter;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SearchServiceImpl implements ICustomSearchService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ISearchService iSearchService;

    @Override
    public UserEntity getUserById(Long id) {
        UserEntity userEntity = elasticsearchOperations.get(String.valueOf(id),UserEntity.class);
        return userEntity;
    }

    @Override
    public  SearchHits<UserEntity> getUserEntityPage(UserEntityVO userEntityVO) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.matchQuery("username",userEntityVO.getUsername()));
        //时间条件
        if(!ObjectUtils.isEmpty(userEntityVO.getFromData())&&!ObjectUtils.isEmpty(userEntityVO.getToData())){
            //时间转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            builder.must(QueryBuilders.rangeQuery("birthday")
                    .gte(sdf.format(userEntityVO.getFromData())+"Z").lte(sdf.format(userEntityVO.getToData())+"Z")
                    .format(DateFormat.date_time_no_millis.name()));
        }
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        HighlightBuilder.Field highlightField =  new HighlightBuilder.Field("username");
        highlightField.highlighterType("unified");
        highlightBuilder.field(highlightField);

        //开始查询
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
        //查询条件
                .withQuery(builder)
        //高亮
                .withHighlightBuilder(highlightBuilder)
        //分业
                .withPageable(PageRequest.of(userEntityVO.getPageNo(),userEntityVO.getPageSize()))
                .build();
        log.info(nativeSearchQuery.getQuery().toString());
        SearchHits<UserEntity> search = elasticsearchOperations.search(nativeSearchQuery,UserEntity.class);

        return search;
    }

    @Override
    public List<UserEntity> getBirthDayBetween(UserEntityVO userEntityVO) {
        List<UserEntity> list = iSearchService.findByBirthdayBetweenAndUsername(userEntityVO.getFromData(),userEntityVO.getToData(),userEntityVO.getUsername());
        return list;
    }

    @Override
    public List<UserEntity> getAgeBetween(Integer age) {
        return null;
    }

    @Override
    public String statisticsCity(UserEntityVO userEntityVO) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        builder.must(QueryBuilders.rangeQuery("birthday")
                .gte(sdf.format(userEntityVO.getFromData())+"Z").lte(sdf.format(userEntityVO.getToData())+"Z")
                .format(DateFormat.date_time_no_millis.name()));
        //聚合运算
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.terms("cityAgg").field("cityEnName"))
                .build();
//        log.info(nativeSearchQuery.getQuery().toString());
        log.info(nativeSearchQuery.getAggregations().toString());
        SearchHits search = elasticsearchOperations.search(nativeSearchQuery,UserEntity.class);
        return JSONObject.toJSONString(search.getAggregations().asMap());
    }

    @Override
    public void insertUserEntity(UserEntity userEntity) {
        elasticsearchRestTemplate.save(userEntity);
    }
}
