package com.toolbox.service;

import java.util.List;

import com.toolbox.entity.UsersEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface UsersService {
    public List<UsersEntity> findsByPage(int start, int limit);

    public int regist(UsersEntity users);

    public void update(UsersEntity users);

    public UsersEntity findByUsername(String username);

    public UsersEntity findById(int id);
}
