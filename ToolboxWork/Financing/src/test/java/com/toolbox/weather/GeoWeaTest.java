package com.toolbox.weather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.PinyinUtility;
import com.toolbox.framework.utils.StringUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class GeoWeaTest extends BaseDao {

    private List<Map<String, Object>> citys() {
        String sql = "SELECT* FROM geo_weather_city";
        List<Map<String, Object>> data = this.getJdbcTemplate().queryForList(sql);
        return data;
    }

    private String geo(String latlng) {
        String url = "http://api.map.baidu.com";
        String path = "/geocoder/v2/?ak=FDb8a96df0329f10675a4d84fd172b8a" + //
        //"&callback=result" +//
        "&location=" + latlng + //
                "&output=json" + //
                "&pois=0";
        String res = HttpUtility.get(url + path);
        return res;
    }

    private String geo2(String address, String city) {
        String url = "http://api.map.baidu.com";
        String path = "/geocoder/v2/?ak=FDb8a96df0329f10675a4d84fd172b8a" + //
        //"&callback=result" +//
        "&address=" + HttpUtility.urlEncode(address, "utf8") + //
                "&city=" + HttpUtility.urlEncode(city, "utf8") + //
                "&output=json" + //
                "&pois=0";
        String res = HttpUtility.get(url + path);
        return res;
    }

    private String weaCity(String cityname) {
        String url = "http://apis.baidu.com";
        String path = "/apistore/weatherservice/citylist" + "?cityname=" + HttpUtility.urlEncode(cityname, "utf8");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("apikey", "04c36c547c9fa053d178d880a47b343e");
        String res = HttpUtility.get(url + path, null, headers, "utf8", 0);
        return res;
    }

    private void test() {
        List<Map<String, Object>> citys = citys();
        for (int i = 0; i < citys.size(); i++) {
            Map<String, Object> city = citys.get(i);
            String name = city.get("name").toString();
            if (name.length() > 2) {
                if (name.indexOf("自治") > 0) {
                    name = name.substring(0, name.indexOf("自治"));
                } else if (name.lastIndexOf("地区") > 0) {
                    name = name.substring(0, name.indexOf("地区"));
                } else if (name.lastIndexOf("省") > 0 || name.lastIndexOf("市") > 0 || name.lastIndexOf("县") > 0 || name.lastIndexOf("盟") > 0) {
                    name = name.substring(0, name.length() - 1);
                }
            }
            String py = PinyinUtility.toPinyin(name);
            update("UPDATE geo_weather_city SET name=?, eName=? WHERE id=?", name, py, city.get("id"));
            System.out.println(name + " > " + py);
        }
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
        GeoWeaTest test = context.getBean(GeoWeaTest.class);
//        System.out.println(test.geo2("北京朝阳区", "朝阳区'"));
        System.out.println(test.geo("41.526707,120.402390"));
    }
}
