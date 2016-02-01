package com.toolbox.framework.spring.mongo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.toolbox.framework.utils.StringUtility;

public abstract class MongoBaseDao<T> {
    private final static Log log = LogFactory.getLog(MongoBaseDao.class);

    @Autowired
    protected MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    protected abstract String getCollection();

    /**
     * 钩子方法,由子类实现返回反射对象的类型
     */
    protected abstract Class<T> getEntityClass();


    /**
     * 保存
     */
    public void save(T t) {
        if (StringUtility.isNotEmpty(getCollection())) {
            this.mongoTemplate.save(t, getCollection());
        } else {
            this.mongoTemplate.save(t);
        }
    }

    /**
     * 查找多个
     */
    public List<T> queryList(Query query) {
        if (StringUtility.isNotEmpty(getCollection())) {
            return this.mongoTemplate.find(query, this.getEntityClass(), getCollection());
        } else {
            return this.mongoTemplate.find(query, this.getEntityClass());
        }
    }

    /**
     * 查找一个
     */
    public T queryById(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        return queryOne(query);
    }

    public T queryOne(Query query) {
        if (StringUtility.isNotEmpty(getCollection())) {
            return this.mongoTemplate.findOne(query, this.getEntityClass(), getCollection());
        } else {
            return this.mongoTemplate.findOne(query, this.getEntityClass());
        }
    }

    /**
     * 分页
     */
    public List<T> getPage(Query query, int start, int size) {
        query.skip(start);
        query.limit(size);
        if (StringUtility.isNotEmpty(getCollection())) {
            return this.mongoTemplate.find(query, this.getEntityClass(), getCollection());
        } else {
            return this.mongoTemplate.find(query, this.getEntityClass());
        }
    }

    /**
     * 获取数量
     */
    public Long getPageCount(Query query) {
        if (StringUtility.isNotEmpty(getCollection())) {
            return this.mongoTemplate.count(query, this.getEntityClass(), getCollection());
        } else {
            return this.mongoTemplate.count(query, this.getEntityClass());
        }

    }

    public void delete(Query query) {
        this.delete(query);
    }

    /**
     *根据ID删除
     */
    public void deleteById(String id) {
        Criteria criteria = Criteria.where("_id").in(id);
        if (null != criteria) {
            Query query = new Query(criteria);
            if (null != query && this.queryOne(query) != null) {
                this.delete((T) query);
            }
        }
    }

    /**
     * 删除对象
     */
    public void delete(T t) {
        if (StringUtility.isNotEmpty(getCollection())) {
            this.mongoTemplate.remove(t, getCollection());
        } else {
            this.mongoTemplate.remove(t);
        }
    }

    /**
    * 更新满足条件的第一个记录
    */
    public void updateFirst(Query query, Update update) {
        if (StringUtility.isNotEmpty(getCollection())) {
            this.mongoTemplate.updateFirst(query, update, this.getEntityClass(), getCollection());
        } else {
            this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
        }
    }

    /**
    * 更新满足条件的所有记录
    */
    public void updateMulti(Query query, Update update) {
        if (StringUtility.isNotEmpty(getCollection())) {
            this.mongoTemplate.updateMulti(query, update, this.getEntityClass(), getCollection());
        } else {
            this.mongoTemplate.updateMulti(query, update, this.getEntityClass());
        }
    }

    /**
    * 查找更新,如果没有找到符合的记录,则将更新的记录插入库中
    */
    public void updateInser(Query query, Update update) {
        if (StringUtility.isNotEmpty(getCollection())) {
            this.mongoTemplate.upsert(query, update, this.getEntityClass(), getCollection());
        } else {
            this.mongoTemplate.upsert(query, update, this.getEntityClass());
        }
    }

}
