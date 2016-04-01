package com.toolbox.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.toolbox.web.service.HotRankService;

/**
 * 定时更新数据库中的热门数据
 * 
* @author E-mail:86yc@sina.com
* 
*/
@Service
public class HotRankDataScheduled {
    private final static Log log = LogFactory.getLog(HotRankDataScheduled.class);
    @Autowired
    private HotRankService   hotRankService;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void hot2DB() {
        hotRankService.hot2DB();
        log.info(">>> HotRank Data To DB success~");
    }

}
