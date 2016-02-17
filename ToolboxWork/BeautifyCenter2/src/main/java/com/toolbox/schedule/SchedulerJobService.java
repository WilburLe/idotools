package com.toolbox.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.framework.utils.StringUtility;

@Service
public class SchedulerJobService {
    private final static Log log = LogFactory.getLog(SchedulerJobService.class);

    @Autowired
    private Scheduler scheduler;

    public void addJob(Class<? extends Job> jobClass, String cron) {
        addJob(jobClass, null, null, cron, null);
    }

    public void addJob(Class<? extends Job> jobClass, String jobKey, String cron) {
        addJob(jobClass, jobKey, null, cron, null);
    }

    public void addJob(Class<? extends Job> jobClass, String jobKey, String cron, String msg) {
        addJob(jobClass, jobKey, null, cron, msg);
    }

    /**
     * 添加一个定时任务
     * @param jobClass
     * @param jobKey
     * @param jobGroup
     * @param cron  
     * @param msg
     */
    // 每隔5秒执行一次：                            */5 * * * * ?
    // 每隔1分钟执行一次：                        0 */1 * * * ?
    // 每天23点执行一次：                          0 0 23 * * ?
    // 每天凌晨1点执行一次：                     0 0 1 * * ?
    // 每月1号凌晨1点执行一次：               0 0 1 1 * ?
    // 每月最后一天23点执行一次：            0 0 23 L * ?
    // 每周星期天凌晨1点实行一次：          0 0 1 ? * L
    // 在26分、29分、33分执行一次：        0 26,29,33 * * * ?
    // 每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?

    public void addJob(Class<? extends Job> jobClass, String jobKey, String jobGroup, String cron, String msg) {
        if (StringUtility.isEmpty(jobKey)) {
            jobKey = jobClass.getName();
        }
        //如果jobKey已存在，先删除
        try {
            boolean exist = scheduler.checkExists(new JobKey(jobKey));
            if (exist) {
                this.deleteJob(jobKey);
                log.info("scheduler delete job > " + jobKey + ", reason this key already exists");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
        if (StringUtility.isNotEmpty(msg)) {
            job.getJobDataMap().put("msg", msg);
        }
        Trigger trigger = null;
        if (StringUtility.isEmpty(jobGroup)) {
            trigger = TriggerBuilder.newTrigger().withIdentity(jobKey)//
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        } else {
            trigger = TriggerBuilder.newTrigger().withIdentity(jobKey, jobGroup)//
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        }
        this.scheduleJob(job, trigger);
        log.info("schedule  " + jobKey + " start success, cron:"+cron);
    }

    public void scheduleJob(JobDetail job, Trigger trigger) {
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    public void deleteJob(String jobKey) {
        try {
            scheduler.deleteJob(new JobKey(jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
