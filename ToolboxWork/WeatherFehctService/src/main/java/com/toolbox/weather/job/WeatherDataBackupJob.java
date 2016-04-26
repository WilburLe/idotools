package com.toolbox.weather.job;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.toolbox.framework.utils.DateUtility;
import com.toolbox.weather.service.WeatherService;

@PersistJobDataAfterExecution
public class WeatherDataBackupJob implements Job {

    private Log            log = LogFactory.getLog(getClass());
    @Autowired
    private WeatherService weatherService;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        weatherService.backupCityWeatherData();
        log.info("WeatherDataBackupJob > " + DateUtility.format(new Date(), "yyyy_MM") + " 天气数据备份完毕~");
    }

}
