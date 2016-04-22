package com.toolbox.weather.data.parsor.pm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.StringUtility;

public class AomenPM25Parsor {

    public static final String  url     = "http://www.smg.gov.mo/www/c_index.php";
    private static final Pattern pattern = Pattern.compile("\\d+-\\d+");

    public static int parsePM25Value() {
        try {
            String res = HttpUtility.get(url, null, "UTF-8");
            if (StringUtility.isEmpty(res))
                return -1;
            Document doc = Jsoup.parse(res);
            Elements es_c = doc.getElementsByAttributeValue("bgcolor", "#cccccc");

            Elements es_a = doc.getElementsByAttributeValue("bgcolor", "#aaaaaa");
            if (es_c == null || es_c.size() == 0)
                return -1;

            List<Integer> pvs = new ArrayList<Integer>();
            String[] vs = null;
            for (int j = 0; j < es_c.size(); j++) {
                String tmp = es_c.get(j).select("td").get(1).text();
                Matcher m = pattern.matcher(tmp);

                if (m.find()) {
                    vs = tmp.split("-");
                    pvs.add(Integer.parseInt(vs[0]));
                    pvs.add(Integer.parseInt(vs[1]));
                }
            }

            for (int i = 0; i < es_a.size(); i++) {
                String vstr = es_a.get(i).select("td").get(1).text();
                Matcher m = pattern.matcher(vstr);
                if (m.find()) {
                    vs = vstr.split("-");
                    pvs.add(Integer.parseInt(vs[0]));
                    pvs.add(Integer.parseInt(vs[1]));
                }
            }
            if (pvs.size() == 0) {
                return -1;
            }
            int pm25 = 0;
            for (int i : pvs) {
                pm25 += i;
            }
            pm25 = pm25 / pvs.size();
            return pm25;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(parsePM25Value());
    }
    
}
