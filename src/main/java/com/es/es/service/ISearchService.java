package com.es.es.service;

import com.es.es.entity.UserEntity;

import javax.xml.crypto.Data;
import java.util.List;

public interface ISearchService {
    UserEntity getUserById(Long id);
    List<UserEntity> getUserEntityPage(UserEntity userEntity);
    List<UserEntity> getBirthDayBetween(Data from,Data to);
    List<UserEntity> getAgeBetween(Integer age);
    void insertUserEntity(UserEntity userEntity);
}