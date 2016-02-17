package com.toolbox.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.toolbox.schedule.job.HotRankScheduleJob;
import com.toolbox.web.entity.WallpaperEntity;
import com.toolbox.web.service.WallpaperService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class Test {
    @Autowired
    private SchedulerJobService scheduler;
    @Autowired
    private WallpaperService    wallpaperService;

    private void t1() {
        scheduler.addJob(HotRankScheduleJob.class, "*/1 * * * * ?");
        scheduler.addJob(HotRankScheduleJob.class, null, "*/2 * * * * ?", "hello");
    }

    private void t2() {
        List<WallpaperEntity> wallpapers = wallpaperService.findOrderByDownload(50, "downloadCount.china");
        for (WallpaperEntity wallpaper : wallpapers) {
            System.out.println(wallpaper.getActionCount());
        }
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
        Test test = context.getBean(Test.class);
        test.t2();
    }
}
