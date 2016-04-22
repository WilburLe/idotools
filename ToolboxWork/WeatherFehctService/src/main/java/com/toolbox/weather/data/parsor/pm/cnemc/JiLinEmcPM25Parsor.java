package com.toolbox.weather.data.parsor.pm.cnemc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.PM25Bean;

public class JiLinEmcPM25Parsor extends AbstractCnEmcPM25Parsor {

    protected String url = "http://jc.bjmemc.com.cn/IndexAirReport/AirForeCastAndReport.aspx";

    public JiLinEmcPM25Parsor() {
        this.setUrl(url);
    }

    public List<PM25Bean> parsePM25ByHtml(String htmlStr) {
        //百度（天气预报+北京存在，则不需要进行此处理）
        Document doc = Jsoup.parse(htmlStr);
        Elements newsHeadlines = doc.select("#Span2");

        String pmStr = newsHeadlines.get(0).html();
        Pattern p = Pattern.compile(".*(\\d{2,3})\\s*-?\\s*(\\d{2,3})\\s*");
        Matcher m = p.matcher(pmStr);
        while (m.find()) {
            System.out.println(StringUtility.isNotEmpty(m.group(1)));
            System.out.println(StringUtility.isNotEmpty(m.group(2)));
            if (StringUtility.isNotEmpty(m.group(1)) && StringUtility.isNotEmpty(m.group(2)))
                System.out.println((Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2);
        }
        System.out.println(newsHeadlines.size());
        return null;

    }
}
