package com.toolbox.service;

import java.util.Date;

import com.toolbox.entity.RadacctEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface RadacctService {
    public RadacctEntity findByUsername(String username);

    /**
     * 返回byte
     * @param username
     * @param start
     * @return
     *
     */
    public long findUserFreeAcc(String username, Date start);
}
