package com.toolbox.framework.spring.mongo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public abstract class MongoBaseDao<T> {
    private final static Log log = LogFactory.getLog(MongoBaseDao.class);

    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 为属性自动注入bean服务
     *
     * @param mongoTemplate
     */
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 钩子方法,由子类实现返回反射对象的类型
     *
     * @return
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 保存一个对象
    *
    *                
    * @param t
    * @return
    */
    public void save(T t) {
        log.info("[Mongo Dao ]save:" + t);
        this.mongoTemplate.save(t);
    }

    /**
    * 根据Id从Collection中查询对象
    *
    *                
    * @param id
    *                 实体对象的Id,对应Collection中记录的_id字段. 
    *                 <p>
    *                     需要说明的是,Mongdo自身没有主键自增机制.解决方法
    *                     <ol>
    *                         <li>实体入库的时候,程序中为实体赋主键值.
    *                         <li>实体入库的时候,在mongodb中自定义函数实现主键自增机制.定义方法同js代码类似
    *                     </ol>
    *                 </p>
    * @return
    */
    public T queryById(String id) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);

        log.info("[Mongo Dao ]queryById:" + query);
        return this.mongoTemplate.findOne(query, this.getEntityClass());
        //        List<T> list = queryList(query);
        //        return list.size() > 0 ? list.get(0) : null;
    }

    /**
    * 根据条件查询集合
    *
    * @param query        
    *                     查询条件
    * @return
    *                     满足条件的集合
    */
    public List<T> queryList(Query query) {
        log.info("[Mongo Dao ]queryList:" + query);
        return this.mongoTemplate.find(query, this.getEntityClass());
    }

    /**
    * 通过条件查询单个实体
    *
    * @param query
    * @return
    */
    public T queryOne(Query query) {
        log.info("[Mongo Dao ]queryOne:" + query);
        return this.mongoTemplate.findOne(query, this.getEntityClass());
    }

    /**
    * 通过条件进行分页查询
    *
    * @param query
    *                     查询条件
    * @param start
    *                     查询起始值 
    *                     <strong> 类似mysql查询中的 limit start, size 中的 start</strong>
    * @param size
    *                     查询大小
    *                     <strong> 类似mysql查询中的 limit start, size 中的 size</strong>
    * @return
    *                     满足条件的集合
    */
    public List<T> getPage(Query query, int start, int size) {
        query.skip(start);
        query.limit(size);
        log.info("[Mongo Dao ]queryPage:" + query + "(" + start + "," + size + ")");
        List<T> lists = this.mongoTemplate.find(query, this.getEntityClass());
        return lists;
    }

    /**
    * 根据条件查询库中符合记录的总数,为分页查询服务
    *
    * @param query
    *                     查询条件
    * @return
    *                     满足条件的记录总数
    */
    public Long getPageCount(Query query) {
        log.info("[Mongo Dao ]queryPageCount:" + query);
        return this.mongoTemplate.count(query, this.getEntityClass());
    }

    /**
    * 根据Id删除用户
    *
    * @param id
    */
    public void deleteById(String id) {
        Criteria criteria = Criteria.where("_id").in(id);
        if (null != criteria) {
            Query query = new Query(criteria);
            log.info("[Mongo Dao ]deleteById:" + query);
            if (null != query && this.queryOne(query) != null) {
                this.delete((T) query);
            }
        }
    }

    /**
    * 删除对象
    *
    * @param t
    */
    public void delete(T t) {
        log.info("[Mongo Dao ]delete:" + t);
        this.mongoTemplate.remove(t);
    }

    /**
    * 更新满足条件的第一个记录
    *
    * @param query
    * @param update
    */
    public void updateFirst(Query query, Update update) {
        log.info("[Mongo Dao ]updateFirst:query(" + query + "),update(" + update + ")");
        this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
    }

    /**
    * 更新满足条件的所有记录
    *
    * @param query
    * @param update
    */
    public void updateMulti(Query query, Update update) {
        log.info("[Mongo Dao ]updateMulti:query(" + query + "),update(" + update + ")");
        this.mongoTemplate.updateMulti(query, update, this.getEntityClass());
    }

    /**
    * 查找更新,如果没有找到符合的记录,则将更新的记录插入库中
    *
    * @param query
    * @param update
    */
    public void updateInser(Query query, Update update) {
        log.info("[Mongo Dao ]updateInser:query(" + query + "),update(" + update + ")");
        this.mongoTemplate.upsert(query, update, this.getEntityClass());
    }

}
