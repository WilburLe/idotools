package com.toolbox.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.ToolboxConfigDao;
import com.toolbox.entity.ToolboxConfigEntity;
import com.toolbox.service.ToolboxConfigService;

@Service("ToolboxConfigService")
public class ToolboxConfigServiceImpl implements ToolboxConfigService {
    @Autowired
    private ToolboxConfigDao toolboxConfigDao;

    @Override
    public ToolboxConfigEntity findByCode(String code) {
        return toolboxConfigDao.findByCode(code);
    }

    @Override
    public void updateByCode(ToolboxConfigEntity entity) {
        toolboxConfigDao.updateByCode(entity);
    }

}
