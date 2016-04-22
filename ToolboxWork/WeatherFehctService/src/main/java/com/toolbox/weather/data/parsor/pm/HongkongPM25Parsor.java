package com.toolbox.weather.data.parsor.pm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.HttpUtility;

public class HongkongPM25Parsor {
    public static String dataUrl = "http://www.aqhi.gov.hk/tc.html";

    public static int parsePM25Value() {
        String html = HttpUtility.get(dataUrl);
        Document doc = Jsoup.parse(html);
        Elements eles = doc.select(".tblMapCont");

        float pmTotal = 0;
        int pmSize = 0;
        for (int i = 0; i < eles.size(); i++) {
            Element ele = eles.get(i);
            // String area = ele.select(".tblMapCont_Title a").text();
            Elements weather = ele.select(".tblMapCont_Value");
            for (int j = 0; j < weather.size(); j++) {
                String val = weather.get(j).text();
                if (val != null && val.indexOf("PM2.5") >= 0) {
                    String pm = weather.get(j + 1).text();
                    try {
                        float p = Float.parseFloat(pm);
                        pmTotal += p;
                        pmSize++;
                    } catch (Exception e) {
                    }
                }
            }
        }
        if (pmSize > 0) {
            return (int) pmTotal / pmSize;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(HongkongPM25Parsor.parsePM25Value());
    }
}
