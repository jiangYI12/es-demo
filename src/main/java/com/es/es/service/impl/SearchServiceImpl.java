package com.es.es.service.impl;

import com.es.es.entity.UserEntity;
import com.es.es.service.ICustomSearchService;
import com.es.es.service.ISearchService;
import com.es.es.vo.UserEntityVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
            builder.must(QueryBuilders.rangeQuery("birthday").gte(userEntityVO.getFromData()).lte(userEntityVO.getToData()).format("strict_date_time_no_millis"));
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
    public void insertUserEntity(UserEntity userEntity) {
        elasticsearchRestTemplate.save(userEntity);
    }
}
