package com.toolbox.weather.data.parsor.pm.cnemc;

import java.util.ArrayList;
import java.util.List;

import com.toolbox.weather.bean.PM25Bean;

/**
 * 中国天气网，或者其他通过网页抓取获取 PM值信息的总入口
 * 
 * 
 * 
 * @author yangjunshuai
 * @version 
 * @since Mar 29, 2013
 *
 */
public class CnEmcPM25Parsor {
    
    private static final List<AbstractCnEmcPM25Parsor> parsorList = new ArrayList<AbstractCnEmcPM25Parsor>();
    static {
//        parsorList.add(new BeiJingEmcPM25Parsor());
//        parsorList.add(new LiaoNingEmcPM25Parsor());
        
        parsorList.add(new AqicnPM25Parsor());
    }
    
    public List<PM25Bean> parsePM25Bean() {
        List<PM25Bean> all = new ArrayList<PM25Bean>();
        for(AbstractCnEmcPM25Parsor parsor:parsorList){
            all.addAll(parsor.parsePM25Beans());
        }
        return all;
    }   
    
    
}
