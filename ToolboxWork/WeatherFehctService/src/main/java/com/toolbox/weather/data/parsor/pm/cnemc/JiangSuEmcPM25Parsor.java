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
 * 江苏省环境监测厅
 * 
 * 解析HTML获取 PM2.5数据。
 * 
 * @author yangjunshuai
 * @version 
 * @since Mar 20, 2013
 *
 */
public class JiangSuEmcPM25Parsor extends AbstractCnEmcPM25Parsor{
    
    protected String url = "http://www.jsem.net.cn/jshjjc/AQIReport.aspx";
    
    public JiangSuEmcPM25Parsor(){
        this.setUrl(url);
    }
    
    public List<PM25Bean> parsePM25ByHtml(String htmlStr){
        List<PM25Bean> beans = new ArrayList<PM25Bean>();
        Document doc = Jsoup.parse(htmlStr);
        Elements trs = doc.select("#MarqueeDiv1 table tr");
        for(int i=1;i<trs.size();i++){
            Element e = trs.get(i);
            Elements tds = e.select("td");
            PM25Bean bean = new PM25Bean();
            bean.setCityName(tds.get(0).html());
            bean.setPm25(Integer.parseInt(tds.get(1).html()));
            bean.setCreateDate(new Date().getTime());
            beans.add(bean);
        }
        return beans;
    }
    
    public static void main(String[] args) {
        JiangSuEmcPM25Parsor b = new JiangSuEmcPM25Parsor();
        for(PM25Bean m :b.parsePM25Beans()){
            System.out.println(m.toString());    
        }
    }
    
}
