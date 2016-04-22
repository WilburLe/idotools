package com.toolbox.weather.data.parsor.pm.cnemc;

import java.util.List;

import com.toolbox.weather.bean.PM25Bean;

public class HeBeiEmcPM25Parsor extends AbstractCnEmcPM25Parsor{
    
    protected String url = "http://www.bjmemc.com.cn/";
    
    public HeBeiEmcPM25Parsor(){
        this.setUrl(url);
    }
    
    public List<PM25Bean> parsePM25ByHtml(String htmlStr){
        return null;
    }

    
}
