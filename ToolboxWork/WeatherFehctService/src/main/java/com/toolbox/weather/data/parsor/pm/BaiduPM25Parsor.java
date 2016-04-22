package com.toolbox.weather.data.parsor.pm;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.UUIDUtility;

public class BaiduPM25Parsor {

    /**
     * 根据城市名字，查询其对应PM2.5值（实际来源为墨迹天气）
     * @param cityName
     * @return
     *
     */
    public static int parsePM25Value(String cityName) {
        try {
            String wd = URLEncoder.encode("天气", "utf-8") + "+" + URLEncoder.encode(cityName, "utf-8");
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Cookie", "BAIDUID=" + UUIDUtility.uuid22());
            String url = "http://www.baidu.com/s?wd=" + wd;
            String html = HttpUtility.get(url, null, headers, "UTF-8", 0);
            Document doc = Jsoup.parse(html);
            Elements es = doc.select(".op_weather4_twoicon_pm25 b");
            return Integer.parseInt(es.text());
        } catch (Exception e) {
            //System.out.println("baidu PM25数据抓取异常:cityId=" + cityName + ", msg=" + e.getMessage());
        }
        return -1;
    }

}
