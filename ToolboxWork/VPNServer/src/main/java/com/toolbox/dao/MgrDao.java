package com.toolbox.dao;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.toolbox.entity.ReporthistoryEntity;
import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.DateUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Repository
public class MgrDao extends BaseDao {
    public void deleteUser(String username) {
        update("delete from users where username=?", username);
        update("delete from expiration where username=?", username);
        update("delete from loginhistory where username=?", username);
        update("delete from radacct where username=?", username);
        update("delete from radcheck where username=?", username);
        update("delete from radusergroup where username=?", username);
        update("delete from reporthistory where username=?", username);
        update("delete from sharehistory where username=?", username);
        update("delete from speedreport where username=?", username);
        update("delete from subscription where username=?", username);
        update("delete from speedreport where username=?", username);
    }

    public void delCheckin(int id) {
        update("delete from reporthistory where id=?", id);
    }

    public void delCheckin(String username, String datestr) {
        Date date = DateUtility.parseDate(datestr, "yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        update("delete from reporthistory where username=? and reportdate between ? and ?", username, date, c.getTime());
    }

    public void checkinDays(String username, int days) {
        update("delete from reporthistory where username=?", username);

        Calendar cd = Calendar.getInstance();
        for (int i = 0; i < days && i < 31; i++) {
            System.out.println(DateUtility.format(cd));
            ReporthistoryEntity re = new ReporthistoryEntity();
            re.setBonus("5");
            re.setUsername(username);
            re.setReportdate(cd.getTime());
            insertBean("reporthistory", re);
            cd.add(Calendar.DAY_OF_MONTH, -1);
        }

    }

}
