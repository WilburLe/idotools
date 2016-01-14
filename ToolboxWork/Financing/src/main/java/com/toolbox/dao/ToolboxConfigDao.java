package com.toolbox.dao;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.ToolboxConfigEntity;
import com.toolbox.framework.spring.support.BaseDao;

@Repository
public class ToolboxConfigDao extends BaseDao {
    public void updateByCode(ToolboxConfigEntity entity) {
        update("update toolbox_config set content = ? where code = ?", entity.getContent(), entity.getCode());
    }

    public ToolboxConfigEntity findByCode(String code) {
        return queryForBean("select * from toolbox_config where code=?", ToolboxConfigEntity.class, code);
    }
}
