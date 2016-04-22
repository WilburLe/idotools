package com.toolbox.web.service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.web.entity.SystemConfigEmtity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class SystemConfigService extends MongoBaseDao<SystemConfigEmtity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<SystemConfigEmtity> getEntityClass() {
        return SystemConfigEmtity.class;
    }

    public SystemConfigEmtity findByConfigType(String configType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("configType").is(configType));
        return this.queryOne(query);
    }

    public SystemConfigEmtity findByConfigType(String configType, String market) {
        Query query = new Query();
        query.addCriteria(Criteria.where("configType").is(configType));
        query.addCriteria(Criteria.where("market").is(market));
        return this.queryOne(query);
    }

    public void updateInser(SystemConfigEmtity config) {
        Update update = new Update();
        update.set("config", config.getConfig());
        this.updateInser(new Query(Criteria.where("configType").is(config.getConfigType())), update);
    }

}
