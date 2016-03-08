package com.toolbox.web.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.web.entity.AppTabEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class AppTabService extends MongoBaseDao<AppTabEntity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<AppTabEntity> getEntityClass() {
        return AppTabEntity.class;
    }

    public List<AppTabEntity> findAllTab() {
        return this.queryList(new Query().with(new Sort(Direction.ASC, "sortNu")));
    }

    public AppTabEntity findTabByElementId(String elementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("elementId").is(elementId));
        return this.queryOne(query);
    }

    public void delTab(String elementId) {
        this.delete(new Query(Criteria.where("elementId").is(elementId)));
    }

}
