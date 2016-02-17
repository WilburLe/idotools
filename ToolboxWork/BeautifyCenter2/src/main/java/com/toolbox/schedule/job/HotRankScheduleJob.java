package com.toolbox.schedule.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.toolbox.service.HotRankService;
import com.toolbox.utils.SpringUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class HotRankScheduleJob implements Job {
    private final static Log log = LogFactory.getLog(HotRankScheduleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String appType = null;
        if (context.getJobDetail().getJobDataMap().containsKey("msg")) {
            appType = context.getJobDetail().getJobDataMap().get("msg").toString();
        }
        Object obj = SpringUtility.getBean("HotRankService");
        HotRankService hotRankService = (HotRankService) obj;
        hotRankService.resetHotRank(appType);
        log.info("schedule HotRankScheduleJob execute success, appType:" + appType);
    }

}
