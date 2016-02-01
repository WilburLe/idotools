package com.toolbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.toolbox.entity.TestEntity;
import com.toolbox.service.Mongo2JsonService;
import com.toolbox.service.TestService;

@Controller
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private Mongo2JsonService mongo2JsonService;
    
    @RequestMapping(value="json1")
    public @ResponseBody JSON json1() {
        mongo2JsonService.setCollection("jsontest");
        String id = "123456789";
//        JSONObject json = new JSONObject();
//        json.put("_id", id);
//        json.put("k1", "v1");
//        json.put("k2", "v2");
//        mongo2JsonService.save(json);
        Criteria criteria = Criteria.where("_id").in(id);
         Query query = new Query(criteria);
        
//         String res = mongo2JsonService.getString(query);
//        String res = mongo2JsonService.queryById(id);
//        return JSON.parseObject(res);
         return mongo2JsonService.getOne(query);
    }
    
    @RequestMapping(value = "mgotest")
    public @ResponseBody String test() {
        TestEntity t = new TestEntity();
        t.set_id("123456");
        t.setName("test_name");
        t.setAge(18);
        t.setData("216-01-14");
        testService.save(t);
//
        return "SUCCESS";
    }

    @RequestMapping(value = "mgotestget/{id}")
    public @ResponseBody TestEntity testget(@PathVariable(value = "id") String id) {
        TestEntity t = testService.queryById(id);

        return t;
    }

    @RequestMapping(value = "mgotestgets/{id}")
    public @ResponseBody List<TestEntity> testgets(@PathVariable(value = "id") String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("name").is(id);
        query.addCriteria(criteria);
        List<TestEntity> ts = testService.queryList(query);

        return ts;
    }
}
