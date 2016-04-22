package com.toolbox.weather.data.parsor.pm.cnemc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.toolbox.weather.bean.PM25Bean;

/**
 * 安徽省环境监测厅
 * 
 * 解析HTML获取 PM2.5数据。
 * 
 * @author yangjunshuai
 * @version 
 * @since Mar 20, 2013
 *
 */
public class AnhuiEmcPM25Parsor extends AbstractCnEmcPM25Parsor{
    
    protected String url = "http://www.ahemc.gov.cn/";
    
    public AnhuiEmcPM25Parsor(){
        this.setUrl(url);
        this.setEncoding("gb2312");
    }
    
    public List<PM25Bean> parsePM25ByHtml(String htmlStr){
        List<PM25Bean> beans = new ArrayList<PM25Bean>();
        Document doc = Jsoup.parse(htmlStr);
        Elements trs = doc.select("#kqzl table tr");
        for(int i=1;i<trs.size();i++){
            Element e = trs.get(i);
            Elements el = e.select("td");
            PM25Bean pmBean = new PM25Bean();
            pmBean.setCityName(el.get(0).html());
            pmBean.setPm25(Integer.parseInt(el.get(2).html()));
            pmBean.setCreateDate(new Date().getTime());
            beans.add(pmBean);
        }
        return beans;
    }
    public static void main(String[] args) {
        AnhuiEmcPM25Parsor b = new AnhuiEmcPM25Parsor();
        List<PM25Bean> bs = b.parsePM25Beans();
        for(PM25Bean m : bs){
            System.out.println(m.toString());    
        }
        System.out.println(bs.size());
        
    }
}
