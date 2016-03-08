package com.toolbox.schedule.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.toolbox.utils.SpringUtility;
import com.toolbox.web.service.StatService;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class StatScheduleJob implements Job {
    private final static Log log = LogFactory.getLog(StatScheduleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String appType = null;
        if (context.getJobDetail().getJobDataMap().containsKey("msg")) {
            appType = context.getJobDetail().getJobDataMap().get("msg").toString();
        }
        Object obj = SpringUtility.getBean("HotRankService");
        StatService statService = (StatService) obj;
        statService.statWallpaper();
        statService.statLockscreen();
        log.info("schedule StatScheduleJob execute success, appType:" + appType);
    }

}
