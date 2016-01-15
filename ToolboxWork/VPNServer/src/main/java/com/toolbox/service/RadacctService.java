package com.toolbox.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.toolbox.entity.RadacctEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface RadacctService {
    public RadacctEntity findByUsername(String username);

    public long findUserFreeAcc(String username, Date start);
}
