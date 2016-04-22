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
 * 辽宁省环境监测厅
 * 
 * 解析HTML获取 PM2.5数据。
 * 
 * @author yangjunshuai
 * @version 
 * @since Mar 20, 2013
 *
 */
public class LiaoNingEmcPM25Parsor extends AbstractCnEmcPM25Parsor{
    
    protected String url = "http://www.lnemc.cn/l_yubao.asp";
    
    public LiaoNingEmcPM25Parsor(){
        this.setUrl(url);
        this.setEncoding("gb2312");
    }
    
    public List<PM25Bean> parsePM25ByHtml(String htmlStr){
        List<PM25Bean> beans = new ArrayList<PM25Bean>();
        Document doc = Jsoup.parse(htmlStr);
        Elements trs = doc.select("#table2 tr");
        for(int i=1;i<trs.size();i++){
            Element e = trs.get(i);
            Elements el = e.select("td font");
            PM25Bean pmBean = new PM25Bean();
            pmBean.setCityName(el.get(0).html());
            String pmStr = el.get(1).html();
            String[] strs =pmStr.split("-");
            pmBean.setPm25((Integer.parseInt(strs[0])+Integer.parseInt(strs[1]))/2);
            pmBean.setCreateDate(new Date().getTime());
            beans.add(pmBean);
        }
        return beans;
        
    }
    public static void main(String[] args) {
        LiaoNingEmcPM25Parsor b = new LiaoNingEmcPM25Parsor();
        for(PM25Bean m :b.parsePM25Beans()){
            System.out.println(m.toString());    
        }
        
    }
}
