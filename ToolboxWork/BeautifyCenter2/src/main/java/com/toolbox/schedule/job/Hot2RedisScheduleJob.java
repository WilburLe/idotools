package com.toolbox.schedule.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.toolbox.utils.SpringUtility;
import com.toolbox.web.service.HotRankService;

/**
 * hot2redis
 * 
 * 将热门的数据存入redis
 * 
* @author E-mail:86yc@sina.com
* 
*/
public class Hot2RedisScheduleJob implements Job {
    private final static Log log = LogFactory.getLog(Hot2RedisScheduleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String appType = null;
        if (context.getJobDetail().getJobDataMap().containsKey("msg")) {
            appType = context.getJobDetail().getJobDataMap().get("msg").toString();
        }
        Object obj = SpringUtility.getBean("HotRankService");
        HotRankService hotRankService = (HotRankService) obj;

        hotRankService.hot2redis();
        log.info("schedule Hot2RedisScheduleJob execute success, appType:" + appType);
    }

}
