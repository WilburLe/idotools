package com.toolbox.web.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.web.entity.BannerContentEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class BannerContentService extends MongoBaseDao<BannerContentEntity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<BannerContentEntity> getEntityClass() {
        return BannerContentEntity.class;
    }

    public List<BannerContentEntity> findsByBannerId(String bannerId) {
        return this.queryList(new Query(Criteria.where("bannerId").is(bannerId)).with(new Sort(Direction.DESC, "sortNu")));
    }

    public void del(String bannerId, String appId) {
        this.delete(new Query(Criteria.where("bannerId").is(bannerId).andOperator(Criteria.where("appId").is(appId))));
    }

}
