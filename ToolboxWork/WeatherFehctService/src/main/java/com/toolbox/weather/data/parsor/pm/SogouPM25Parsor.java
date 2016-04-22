package com.toolbox.weather.data.parsor.pm;

import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.HttpUtility;

public class SogouPM25Parsor {

    /**
     * 根据城市名字，查询其对应PM2.5值（实际来源为墨迹天气）
     * @param cityName
     * @return
     *
     */
    public static int parsePM25Value(String cityName) {
        try {
            String wd = URLEncoder.encode(cityName + "pm2.5", "utf-8");
            String html = HttpUtility.get("http://www.sogou.com/web?query=" + wd, null);
            Document doc = Jsoup.parse(html);
            Elements es = doc.select("div .pm_box");
            return Integer.parseInt(es.get(0).select("div").get(1).text());
        } catch (Exception e) {
//            System.out.println(cityName + "sogou pm25 获取异常");
        }
        return -1;
    }

}
