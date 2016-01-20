package com.toolbox.service;

import com.toolbox.entity.ToolboxConfigEntity;

public interface ToolboxConfigService {
    public void updateByCode(ToolboxConfigEntity entity);

    public ToolboxConfigEntity findByCode(String code);
}
