package com.toolbox.weather.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.toolbox.weather.data.WeatherDataFetcher;

@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
@ScheduleJob(name = "WeatherParseJob", cron = "0 0/15 * * * ?")
public class WeatherParseJob implements Job {

    private Log                log = LogFactory.getLog(getClass());

    @Autowired
    private WeatherDataFetcher weatherDataFetcher;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            weatherDataFetcher.fetchDataAllCity();
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
