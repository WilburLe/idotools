package com.toolbox.web.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.toolbox.framework.spring.mongo.MongoBaseDao;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.web.entity.LockscreenEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class LockScreenService extends MongoBaseDao<LockscreenEntity> {

    @Override
    protected String getCollection() {
        return null;
    }

    @Override
    protected Class<LockscreenEntity> getEntityClass() {
        return LockscreenEntity.class;
    }

    public void saveList(List<LockscreenEntity> lockscenerys) {
        for (LockscreenEntity lockscenery : lockscenerys) {
            this.save(lockscenery);
        }
    }

    public LockscreenEntity findByElementId(String elementId) {
        return this.queryOne(new Query(Criteria.where("elementId").is(elementId)));
    }

    public LockscreenEntity findByPackageName(String packageName) {
        return this.queryOne(new Query(Criteria.where("packageName").is(packageName)));
    }

    public List<LockscreenEntity> findByPage(String market, int start, int size) {
        Query query = new Query();
        if (StringUtility.isNotEmpty(market) && !"all".equals(market)) {
            query.addCriteria(Criteria.where("market").is(market));
        }
        query.with(new Sort(Direction.DESC, "createDate"));
        if (size == -1) {
            return this.queryList(query);
        } else {
            return this.getPage(query, start * size, size);
        }
    }

    public int count() {
        Query query = new Query();
        Long count = this.getPageCount(query);
        return count != null ? count.intValue() : 0;
    }

    public void deleteByElementId(String elementId) {
        this.delete(new Query(Criteria.where("elementId").is(elementId)));
    }

    public List<LockscreenEntity> findOrderByDownload(int size, String downloadSource) {
        Query query = new Query();
        //        query.addCriteria(Criteria.where("tags.0").exists(false));
        query.with(new Sort(new Order(Direction.DESC, downloadSource)));
        return this.getPage(query, 0, size);
    }

}
