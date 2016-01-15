package com.toolbox.service;

import java.util.List;

import com.toolbox.entity.UsersEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface UsersService {
    public List<UsersEntity> findsByPage(int start, int limit);
    public int save(UsersEntity users);
}
