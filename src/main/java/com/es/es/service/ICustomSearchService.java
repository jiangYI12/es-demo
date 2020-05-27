package com.es.es.service;

import com.es.es.entity.UserEntity;
import com.es.es.vo.UserEntityVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface ICustomSearchService {
    UserEntity getUserById(Long id);
    SearchHits<UserEntity> getUserEntityPage(UserEntityVO userEntityVO);
    List<UserEntity> getBirthDayBetween(UserEntityVO userEntityVO);
    List<UserEntity> getAgeBetween(Integer age);
    Object searchSuggest(String userName);
    String statisticsCity(UserEntityVO userEntityVO);
    void insertUserEntity(UserEntity userEntity);
    void createSuggest(UserEntity userEntity);
}
