package com.es.es.service.impl;

import com.es.es.entity.UserEntity;
import com.es.es.service.ISearchService;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.List;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements ISearchService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public UserEntity getUserById(Long id) {
        UserEntity userEntity = elasticsearchRestTemplate.get(String.valueOf(id),UserEntity.class);
        return userEntity;
    }

    @Override
    public List<UserEntity> getUserEntityPage(UserEntity userEntity) {
        return null;
    }

    @Override
    public List<UserEntity> getBirthDayBetween(Data from, Data to) {
        return null;
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
