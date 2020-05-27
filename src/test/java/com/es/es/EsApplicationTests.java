package com.es.es;

import com.es.es.entity.UserEntity;
import com.es.es.service.ICustomSearchService;
import com.es.es.service.ISearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class EsApplicationTests {

    @Autowired
    private ISearchService iSearchService;
    @Autowired
    private ICustomSearchService iCustomSearchService;
    @Test
    void userEntity3() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCityEnName("佛山");
        userEntity.setUsername("蔡徐坤");
        iCustomSearchService.createSuggest(userEntity);
    }
    @Test
    void userEntity4() {
        List<UserEntity> list =  new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setAge(18);
        userEntity.setBirthday(new Date());
        userEntity.setCityEnName("佛山");
        userEntity.setUsername("蔡徐坤");
        userEntity.setCreateTime(new Date());
        userEntity.setLastUpdateTime(new Date());
        list.add(userEntity);
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId("2");
        userEntity1.setAge(20);
        userEntity1.setBirthday(new Date());
        userEntity1.setCityEnName("深圳");
        userEntity1.setUsername("五一放");
        userEntity1.setCreateTime(new Date());
        userEntity1.setLastUpdateTime(new Date());
        list.add(userEntity1);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId("3");
        userEntity2.setAge(30);
        userEntity2.setBirthday(new Date());
        userEntity2.setCityEnName("佛山");
        userEntity2.setUsername("我爱我的祖国");
        userEntity2.setCreateTime(new Date());
        userEntity2.setLastUpdateTime(new Date());
        list.add(userEntity2);
        UserEntity userEntity3 = new UserEntity();
        userEntity3.setId("4");
        userEntity3.setAge(27);
        userEntity3.setBirthday(new Date());
        userEntity3.setCityEnName("新疆");
        userEntity3.setUsername("刘德华");
        userEntity3.setCreateTime(new Date());
        userEntity3.setLastUpdateTime(new Date());
        list.add(userEntity3);
        UserEntity userEntity4 = new UserEntity();
        userEntity4.setId("5");
        userEntity4.setAge(42);
        userEntity4.setBirthday(new Date());
        userEntity4.setCityEnName("佛山");
        userEntity4.setUsername("我的祖国是中国");
        userEntity4.setCreateTime(new Date());
        userEntity4.setLastUpdateTime(new Date());
        list.add(userEntity4);
        UserEntity userEntity5 = new UserEntity();
        userEntity5.setId("6");
        userEntity5.setAge(29);
        userEntity5.setBirthday(new Date());
        userEntity5.setCityEnName("东莞");
        userEntity5.setUsername("大爱我的中国祖国");
        userEntity5.setCreateTime(new Date());
        userEntity5.setLastUpdateTime(new Date());
        list.add(userEntity5);
        UserEntity userEntity6 = new UserEntity();
        userEntity6.setId("7");
        userEntity6.setAge(22);
        userEntity6.setBirthday(new Date());
        userEntity6.setCityEnName("武汉");
        userEntity6.setUsername("刘亦菲");
        userEntity6.setCreateTime(new Date());
        userEntity6.setLastUpdateTime(new Date());
        list.add(userEntity6);
        UserEntity userEntity7 = new UserEntity();
        userEntity7.setId("8");
        userEntity7.setAge(18);
        userEntity7.setBirthday(new Date());
        userEntity7.setCityEnName("东莞");
        userEntity7.setUsername("如含");
        userEntity7.setCreateTime(new Date());
        userEntity7.setLastUpdateTime(new Date());
        list.add(userEntity7);
        UserEntity userEntity8 = new UserEntity();
        userEntity8.setId("9");
        userEntity8.setAge(30);
        userEntity8.setBirthday(new Date());
        userEntity8.setCityEnName("深圳");
        userEntity8.setUsername("吾亦素");
        userEntity8.setCreateTime(new Date());
        userEntity8.setLastUpdateTime(new Date());
        list.add(userEntity8);
        UserEntity userEntity9 = new UserEntity();
        userEntity9.setId("10");
        userEntity9.setAge(55);
        userEntity9.setBirthday(new Date());
        userEntity9.setCityEnName("台湾");
        userEntity9.setUsername("彭于晏");
        userEntity9.setCreateTime(new Date());
        userEntity9.setLastUpdateTime(new Date());
        list.add(userEntity9);
        for (UserEntity u :
                list) {
            iCustomSearchService.createSuggest(u);
            iSearchService.save(u);
        }
    }

}
