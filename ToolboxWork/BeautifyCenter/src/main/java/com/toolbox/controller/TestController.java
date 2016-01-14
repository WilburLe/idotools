package com.toolbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toolbox.entity.TestEntity;
import com.toolbox.service.TestService;

@Controller
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping(value = "mgotest")
    public @ResponseBody String test() {
        TestEntity t = new TestEntity();
        t.set_id("123456");
        t.setName("test_name");
        t.setAge(18);
        t.setData("216-01-14");
        testService.save(t);

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
