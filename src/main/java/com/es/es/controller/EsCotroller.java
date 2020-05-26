package com.es.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.es.es.entity.UserEntity;
import com.es.es.service.ICustomSearchService;
import com.es.es.service.ISearchService;
import com.es.es.vo.UserEntityVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class EsCotroller {
    private final ICustomSearchService iCustomSearchService;


    @RequestMapping("/getById")
    public UserEntity getUserEntity(@RequestParam("id") Long id){
        return iCustomSearchService.getUserById(id);
    }

    @RequestMapping("/getRangeBirthDay")
    public List<UserEntity> getRangeBirthDay(@RequestBody UserEntityVO userEntityVO){
        return iCustomSearchService.getBirthDayBetween(userEntityVO);
    }

    @RequestMapping("/getUserEntityPage")
    public SearchHits<UserEntity> getUserEntityPage(@RequestBody UserEntityVO userEntityVO) {
        return iCustomSearchService.getUserEntityPage(userEntityVO);
    }

    @RequestMapping(value = "/getCityAgg",produces = "application/json;charset=UTF-8")
    public String getCityAgg(@RequestBody UserEntityVO userEntityVO) throws JsonProcessingException {
        return  iCustomSearchService.statisticsCity(userEntityVO);
    }

    //搜索自动补全
    @RequestMapping(value = "/suggest",produces = "application/json;charset=UTF-8")
    public SearchResponse suggest(@RequestParam("username") String username) throws JsonProcessingException {
        return  iCustomSearchService.searchSuggest(username);
    }

    @RequestMapping("/save")
    public void save(@RequestBody UserEntity userEntity){
        iCustomSearchService.insertUserEntity(userEntity);
    }
}
