package com.toolbox.service;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.entity.ReporthistoryEntity;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface ReporthistoryService {
    public void save(ReporthistoryEntity reporthistory);

    public ReporthistoryEntity findByUsername(String username);

    public JSONObject checkInData(String username, Date startDate, String offset);
}
