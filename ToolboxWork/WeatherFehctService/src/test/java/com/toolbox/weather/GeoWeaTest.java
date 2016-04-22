package com.toolbox.weather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.HttpUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class GeoWeaTest extends BaseDao {

    private List<Map<String, Object>> citys() {
        String sql = "SELECT c1.id, c1.name, c1.locationLat as lat, c1.locationLng as lng, c2.name as pName "//
                + "FROM city c1 join city c2 on (c1.parentId=c2.id) "//
                + "where c1.treePath like ?";
        List<Map<String, Object>> data = this.getJdbcTemplate().queryForList(sql, "%[1][%");
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

    private String weaCity(String cityname) {
        String url = "http://apis.baidu.com";
        String path = "/apistore/weatherservice/citylist" + "?cityname=" + HttpUtility.urlEncode(cityname, "utf8");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("apikey", "04c36c547c9fa053d178d880a47b343e");
        String res = HttpUtility.get(url + path, null, headers, "utf8", 0);
        return res;
    }

    private void test() {
        List<Map<String, Object>> dbcitys = citys();
        for (int i = 0; i < dbcitys.size(); i++) {
            Map<String, Object> dbcity = dbcitys.get(i);
            String name = dbcity.get("name").toString();
            String pName = dbcity.get("pName").toString();
            String geocity = geo(dbcity.get("lat") + "," + dbcity.get("lng"));
            JSONObject geo_city = JSONObject.parseObject(geocity);
            JSONObject cityInfo = geo_city.getJSONObject("result").getJSONObject("addressComponent");
            String province = cityInfo.getString("province");
            String district = cityInfo.getString("district");
            String adcode = cityInfo.getString("adcode");
            if (district.length() > 2) {
                district = district.replace("市", "").replace("县", "").replace("区", "");
            }
            String weacity = weaCity(district);
            JSONObject wea_city = JSONObject.parseObject(weacity);
            int errNum = wea_city.getIntValue("errNum");
            if (errNum == 0) {
                JSONArray retData = wea_city.getJSONArray("retData");
                for (int m = 0; m < retData.size(); m++) {
                    JSONObject rData = retData.getJSONObject(m);
                    String province_cn = rData.getString("province_cn");
                    String district_cn = rData.getString("district_cn");
                    String area_id = rData.getString("area_id");
                    if (pName.indexOf(district_cn) >= 0 || pName.indexOf(province_cn) >= 0) {
                        String sql = "insert into wea_geo (name, geoid, weaid) values ('" + name + "', '" + adcode + "', '" + area_id + "');";
                        insert(sql);
                        System.out.println(sql);
                    }
                }
            } else {
            }
        }
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
        GeoWeaTest test = context.getBean(GeoWeaTest.class);
                test.test();
    }
}
