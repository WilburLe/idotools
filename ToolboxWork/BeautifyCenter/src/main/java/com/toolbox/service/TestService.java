package com.toolbox.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.toolbox.entity.TestEntity;
import com.toolbox.framework.spring.mongo.MongoBaseDao;

@Service("TestService")
public class TestService extends MongoBaseDao<TestEntity> {
    @Override
    protected Class<TestEntity> getEntityClass() {
        return TestEntity.class;
    }

    @Override
    protected String getCollection() {
        return null;
    }

    /**
     * 分页查询   对应mongodb操作中的  db.TestEntity.find().skip(10).limit(10);
     *
     *                
     * @param TestEntity
     *                     查询的条件
     * @param start    
     *                     用户分页查询的起始值
     * @param size
     *                     查询的数据数目
     * 
     * @return
     *                     返回查询到的数据集合
     */
    public List<TestEntity> queryPage(TestEntity TestEntity, Integer start, Integer size) {
        Query query = new Query();
        //此处可以增加分页查询条件Criteria.然后query.addCriteria(criteria);
        return this.getPage(query, (start - 1) * size, size);
    }

    /**
     * 查询满足分页的记录总数
     *
     *                
     * @param TestEntity
     *                     查询的条件
     * @return
     *                     返回满足条件的记录总数
     */
    public Long queryPageCount(TestEntity TestEntity) {
        Query query = new Query();
        //此处可以增加分页查询条件Criteria.然后query.addCriteria(criteria);
        return this.getPageCount(query);
    }

    /**
     * 更新操作
     *
     *                
     * @param TestEntity
     *                         要更新的数据
     * @throws Exception
     *                         更新异常
     */
    public void updateFirst(TestEntity TestEntity) throws Exception {
        Update update = new Update();
        if (null == TestEntity.getId() || "".equals(TestEntity.getId().trim())) {
            //如果主键为空,则不进行修改
            throw new Exception("Update data Id is Null");
        }
        update.set("name", TestEntity.getName());
        this.updateFirst(Query.query(Criteria.where("_id").is(TestEntity.getId())), update);
    }

    /**
     * 更新库中所有数据
     *
     * @param TestEntity
     *                         更新的数据
     * @throws Exception
     *                         更新异常
     */
    public void updateMulti(TestEntity TestEntity) throws Exception {
        Update update = new Update();
        if (null == TestEntity.getId() || "".equals(TestEntity.getId().trim())) {
            //如果主键为空,则不进行修改
            throw new Exception("Update data Id is Null");
        }
        update.set("name", TestEntity.getName());
        this.updateMulti(Query.query(Criteria.where("_id").is(TestEntity.getId())), update);
    }

}
