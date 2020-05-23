package com.es.es.controller;

import com.es.es.entity.UserEntity;
import com.es.es.service.ISearchService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EsCotroller {
    private final ISearchService iSearchService;


    @RequestMapping("/getById")
    public UserEntity getUserEntity(@RequestParam("id") Long id){
        return iSearchService.getUserById(id);
    }

    @RequestMapping("/save")
    public void save(@RequestBody UserEntity userEntity){
        iSearchService.insertUserEntity(userEntity);
    }
}
