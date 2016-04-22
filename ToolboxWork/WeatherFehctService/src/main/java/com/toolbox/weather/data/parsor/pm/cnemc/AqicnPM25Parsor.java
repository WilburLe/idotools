package com.toolbox.weather.data.parsor.pm.cnemc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.PM25Bean;

public class AqicnPM25Parsor extends AbstractCnEmcPM25Parsor{
    protected String url = "http://www.aqicn.info/city/all/";
//    protected String url = "http://news.cntv.cn/2013/03/29/ARTI1364499530306636.shtml";
    public AqicnPM25Parsor(){
        super.setUrl(url);
    }
    
    @Override
    public List<PM25Bean> parsePM25ByHtml(String htmlStr) {
        
        List<PM25Bean> beans = new ArrayList<PM25Bean>();
        Document doc = Jsoup.parse(htmlStr);
        Elements citytables = doc.select(".citytable");
        Elements trs = citytables.get(0).select(".citylist");
        for(int i=0;i<trs.size();i++){
            Element tr = trs.get(i);
            Elements td = tr.select(".cityaqilist span");
            PM25Bean pmBean = new PM25Bean();
            
            String cityName = td.get(0).html();
            cityName = cityName.substring(cityName.indexOf("(")+1,cityName.length()-2);
            pmBean.setCityName(cityName);
            
            String pmStr = td.get(1).html();
            if(StringUtility.isEmpty(pmStr) || pmStr.equals("no data"))
                continue;
            pmBean.setPm25(Integer.parseInt(pmStr));
            pmBean.setCreateDate(new Date().getTime());
            beans.add(pmBean);
        }
        return beans;
        
    }   
    
    public static void main(String[] args) {
        AqicnPM25Parsor b = new AqicnPM25Parsor();
        
        List<PM25Bean> list = b.parsePM25Beans();
        for(PM25Bean m :list){
            System.out.println(m.toString());
        }
        System.out.println(list.size());
    }
    
}
