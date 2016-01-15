package com.toolbox.service;

import java.util.List;

import com.toolbox.entity.ExpirationEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface ExpirationService {
    public List<ExpirationEntity> findsByUsernames(List<String> usernames);

    public ExpirationEntity findByUsername(String username);
    
    
}
