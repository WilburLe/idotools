/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:WeatherRankJob.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 5, 2013 9:24:29 PM
 * 
 */
package com.toolbox.weather.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.toolbox.weather.data.RankingListProcessor;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 5, 2013
 * 
 */
@PersistJobDataAfterExecution
@ScheduleJob(name = "RankingListJob", cron = "0 0/5 * * * ?")
public class RankingListJob implements Job {
    @Autowired
    private RankingListProcessor rankingListProcessor;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        //实时温度排序
        rankingListProcessor.tempRanking();
        //PM排序
        rankingListProcessor.pmRanking();
    }

}
