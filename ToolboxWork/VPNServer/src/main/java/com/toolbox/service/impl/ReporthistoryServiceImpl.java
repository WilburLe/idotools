package com.toolbox.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.dao.ReporthistoryDao;
import com.toolbox.entity.ReporthistoryEntity;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.service.ReporthistoryService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Service("ReporthistoryService")
public class ReporthistoryServiceImpl implements ReporthistoryService {
    private final static String dateformat = "yyyy-MM-dd";

    @Autowired
    private ReporthistoryDao reporthistoryDao;

    @Override
    public void save(ReporthistoryEntity reporthistory) {
        reporthistoryDao.save(reporthistory);
    }

    @Override
    public JSONObject checkInData(String username, Date startDate, String offset) {
        JSONObject result = new JSONObject();
        List<ReporthistoryEntity> list = reporthistoryDao.findsByUsernames(username, startDate);
        List<String> dates = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            ReporthistoryEntity entity = list.get(i);
            String eday = DateUtility.format(entity.getReportdate(), dateformat);
            dates.add(eday);
        }

        //今天是否签到    需要根据时区计算
        int hour = Integer.parseInt(offset) / 1000 / 60 / 60;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, hour);
        Date date = c.getTime();

        String today = DateUtility.format(date, dateformat);
        if (dates.size() > 0 && dates.get(0).equals(today)) {
            result.put("isCheckedInToday", 1);
        } else {
            result.put("isCheckedInToday", 0);
        }
        c.add(Calendar.DAY_OF_MONTH, -1);
        String yesterday = DateUtility.format(c.getTime(), dateformat);
        //连续签到次数
        int checkInCount = 0;
        for (int i = 0; i < list.size(); i++) {
            ReporthistoryEntity entity = list.get(i);
            String eday = DateUtility.format(entity.getReportdate(), dateformat);
            if (eday.equals(today)) {
                checkInCount++;
            }
            if (eday.equals(yesterday)) {
                checkInCount++;
                Calendar c2 = Calendar.getInstance();
                c2.setTime(entity.getReportdate());
                c2.add(Calendar.DAY_OF_MONTH, -1);
                yesterday = DateUtility.format(c2.getTime(), dateformat);
            }
        }
        List<List<String>> checkInDays = dates2list(dates);
        result.put("checkInDays", checkInDays);
        result.put("reportRemail", reportRemails(checkInDays));
        result.put("checkInCount", checkInCount);
        return result;
    }

    @Override
    public ReporthistoryEntity findByUsername(String username) {
        return reporthistoryDao.findByUsername(username);
    }

    private static List<List<String>> dates2list(List<String> dates) {
        List<List<String>> datesps = new ArrayList<List<String>>();
        List<String> datesp = new ArrayList<String>();
        for (int i = 0; i < dates.size(); i++) {
            String nowdate = dates.get(i);
            if (i == dates.size() - 1) {
                datesp.add(nowdate);
                datesps.add(datesp);
                break;
            }
            String nextdate = dates.get(i + 1);
            Calendar c = Calendar.getInstance();
            c.setTime(DateUtility.parseDate(nowdate, dateformat));
            c.add(Calendar.DAY_OF_MONTH, -1);
            String nownextdate = DateUtility.format(c, dateformat);

            if (nextdate.equals(nownextdate)) {
                datesp.add(nowdate);
            } else {
                datesp.add(nowdate);
                datesps.add(datesp);
                datesp = new ArrayList<String>();
            }
        }
        return datesps;
    }

    /**
     * 签到送流量：
     *  第一条签到送5M流量，
     *  第二天签到送10M流量，
     *  第三条签到送20M流量，
     *  第四天签到送30M流量。
     *  此后连续签到 送30M流量。
     *  中间有中断的，再次签到时重新从5M流量开始赠送。
     */
    private static long reportRemails(List<List<String>> datesps) {
        int rs = 0;
        for (int i = 0; i < datesps.size(); i++) {
            int count = datesps.get(i).size();
            rs += reportRemail(count);
        }
        return rs;
    }

    public static long reportRemail(int count) {
        int rs = 0;
        if (count == 1) {
            rs = 5;
        } else if (count == 2) {
            rs = 5 + 10;
        } else if (count == 3) {
            rs = 5 + 10 + 20;
        } else if (count >= 4) {
            rs = 5 + 10 + 20 + ((count - 3) * 30);
        }
        return rs * 1024;
    }
}
