package com.toolbox.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.dao.RadacctDao;
import com.toolbox.entity.RadacctEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.service.RadacctService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("RadacctService")
public class RadacctServiceImpl implements RadacctService {
    private final static Log log = LogFactory.getLog(RadacctServiceImpl.class);
    @Autowired
    private RadacctDao       radacctDao;

    public long findUserFreeAcc(String username, Date start) {
        long total = 0;
        List<Map<String, Object>> map = radacctDao.findUserFreeAcc(username, start);
        for (Map<String, Object> acc : map) {
            try {
                long input = Long.parseLong(acc.get("acctinputoctets").toString());
                total += input;
            } catch (Exception e) {
                log.error("/n -----findUserFreeAcc ERROR------ /n" + username + "," + DateUtility.format(start) + ",input error /n msg:" + e.getMessage() + " /n ----------- /n");
            }
            try {
                long output = Long.parseLong(acc.get("acctoutputoctets").toString());
                total += output;
            } catch (Exception e) {
                log.error("/n -----findUserFreeAcc ERROR------ /n" + username + "," + DateUtility.format(start) + ",output error /n msg:" + e.getMessage() + " /n ----------- /n");
            }

        }
        return total;
    }

    public RadacctEntity findByUsername(String username) {
        return radacctDao.findByUsername(username);
    }
}
