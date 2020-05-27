package com.es.es.service.impl;

import com.es.es.entity.UserEntity;
import com.es.es.entity.UserSuggest;
import com.es.es.service.ICustomSearchService;
import com.es.es.service.ISearchService;
import com.es.es.vo.UserEntityVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SearchServiceImpl implements ICustomSearchService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    private final ElasticsearchOperations elasticsearchOperations;


    private final ISearchService iSearchService;

    private final ObjectMapper objectMapper;

    private RestHighLevelClient restClient;

    @Override
    public UserEntity getUserById(Long id) {
        UserEntity userEntity = elasticsearchOperations.get(String.valueOf(id),UserEntity.class);
        return userEntity;
    }

    //条件分页搜索
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

    //搜索栏建议
    @Override
    public Object searchSuggest(String userName) {
        String suggestField="suggest";//指定在哪个字段搜索
        Integer suggestMaxCount=10;//获得最大suggest条数
        //设置建议搜索
        CompletionSuggestionBuilder suggestionBuilderDistrict  = SuggestBuilders.completionSuggestion(suggestField)
             .size(suggestMaxCount)
             .prefix(userName, Fuzziness.AUTO);
        //创建建议搜索
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("autocomplete", suggestionBuilderDistrict);//添加suggest
        //执行建议搜索
     SearchResponse searchResponse = elasticsearchRestTemplate
             .suggest(suggestBuilder, IndexCoordinates.of("userentity"));
       List<Suggest.Suggestion.Entry.Option> options  = (List<Suggest.Suggestion.Entry.Option>) searchResponse.getSuggest().getSuggestion("autocomplete").<UserEntity>getEntries().get(0).getOptions();
        List<String> list = new ArrayList<>();
       for(Suggest.Suggestion.Entry.Option o:options){
           list.add(o.getText().toString());
        }
       return list;
    }

    //聚合搜索
    @Override
    public String statisticsCity(UserEntityVO userEntityVO) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        builder.must(QueryBuilders.rangeQuery("birthday")
                .gte(sdf.format(userEntityVO.getFromData())+"Z").lte(sdf.format(userEntityVO.getToData())+"Z")
                .format(DateFormat.date_time_no_millis.name()));
        //聚合运算
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .addAggregation(AggregationBuilders.terms("cityAgg").field("cityEnName"))
                .build();
        log.info(nativeSearchQuery.getQuery().toString());
        log.info(nativeSearchQuery.getAggregations().toString());
        SearchHits search = elasticsearchOperations.search(nativeSearchQuery,UserEntity.class);
        try {
            return  objectMapper.writeValueAsString(search);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertUserEntity(UserEntity userEntity) {
        elasticsearchRestTemplate.save(userEntity);
    }

    //获取分词结果并插入搜索建议字段
    @Override
    public void createSuggest(UserEntity userEntity) {

        AnalyzeRequest analyzeRequest = AnalyzeRequest
               .buildCustomAnalyzer("userentity","ik_max_word").build(userEntity.getUsername(),userEntity.getCityEnName());
        try {
            List<AnalyzeResponse.AnalyzeToken> analyzeTokens = restClient.indices()
                    .analyze(analyzeRequest, RequestOptions.DEFAULT).getTokens();
            List<UserSuggest> userSuggests = new LinkedList<>();
            for(AnalyzeResponse.AnalyzeToken analyzeToken:analyzeTokens){
                UserSuggest userSuggest = new UserSuggest();
                userSuggest.setInput(analyzeToken.getTerm());
                userSuggest.setWeight(10);
                userSuggests.add(userSuggest);
            }
            userEntity.setSuggest(userSuggests);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
