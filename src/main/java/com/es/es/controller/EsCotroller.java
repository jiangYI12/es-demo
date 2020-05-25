package com.es.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.es.es.entity.UserEntity;
import com.es.es.service.ICustomSearchService;
import com.es.es.service.ISearchService;
import com.es.es.vo.UserEntityVO;
import lombok.AllArgsConstructor;
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
    public SearchHits<UserEntity> getUserEntityPage(@RequestBody UserEntityVO userEntityVO){
        return iCustomSearchService.getUserEntityPage(userEntityVO);
    }

    @RequestMapping("/getCityAgg")
    public String getCityAgg(@RequestBody UserEntityVO userEntityVO){
        return  iCustomSearchService.statisticsCity(userEntityVO);
    }

    @RequestMapping("/save")
    public void save(@RequestBody UserEntity userEntity){
        iCustomSearchService.insertUserEntity(userEntity);
    }
}
