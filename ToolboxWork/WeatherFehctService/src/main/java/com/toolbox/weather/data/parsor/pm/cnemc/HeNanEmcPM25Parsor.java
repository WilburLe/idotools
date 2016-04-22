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
 * 四川省环境监测厅
 * 
 * 解析HTML获取 PM2.5数据。
 * 
 * @author yangjunshuai
 * @version 
 * @since Mar 20, 2013
 *
 */
public class HeNanEmcPM25Parsor extends AbstractCnEmcPM25Parsor{
    
    protected String url = "http://www.hnep.gov.cn/";
    
    public HeNanEmcPM25Parsor(){
        this.setUrl(url);
        this.setEncoding("gb2312");
    }
    
    public List<PM25Bean> parsePM25ByHtml(String htmlStr){
        List<PM25Bean> beans = new ArrayList<PM25Bean>();
        Document doc = Jsoup.parse(htmlStr);
        Elements trs = doc.select("#demo1 table tr");
        for(int i=0;i<trs.size();i++){
            Element tr = trs.get(i);
            Elements td = tr.select("td");
            PM25Bean pmBean = new PM25Bean();
            pmBean.setCityName(td.get(0).select("span").get(0).html());
            pmBean.setPm25(Integer.parseInt(td.get(4).html()));
            pmBean.setCreateDate(new Date().getTime());
            beans.add(pmBean);
        }
        return beans;
        
    }
    public static void main(String[] args) {
        HeNanEmcPM25Parsor b = new HeNanEmcPM25Parsor();
        for(PM25Bean m :b.parsePM25Beans()){
            System.out.println(m.toString());    
        }
        
    }
}
