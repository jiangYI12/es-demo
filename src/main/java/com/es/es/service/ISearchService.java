package com.es.es.service;

import com.es.es.entity.UserEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;
import java.util.List;

public interface ISearchService extends ElasticsearchRepository<UserEntity,String> {
    List<UserEntity> findByBirthdayBetweenAndUsername(Date from,Date to,String username);
}
