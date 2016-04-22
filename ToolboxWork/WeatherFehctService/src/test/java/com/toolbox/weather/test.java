package com.toolbox.weather;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.HttpUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class test {
    public static void main(String[] args) {

//        Map<String, String> params = new HashMap<String, String>();
//        params.put("Normal", "[{\"keyword\": \"河南,南阳\",\"connectKey\": \"H+oSeGpA\",\"language\": \"zh_cn\"}]");
//        params.put("keyword", "河南,南阳");
        
        JSONObject params = new JSONObject();
        JSONArray Normal = new JSONArray();
        JSONObject data = new JSONObject();
        data.put("keyword", "河南,南阳");
        data.put("connectKey", "H+oSeGpA");
        data.put("language", "zh_cn");
        
        JSONObject data2 = new JSONObject();
        data2.put("city", "中国_河南_南阳_卧龙区");
        data2.put("cityId", "1-59063_13_AL");
        data2.put("language", "zh-cn");
        data2.put("unit", "0");
        data2.put("connectKey", "H+oSeGpA");
        data2.put("longitude", "116.40");
        data2.put("latitude", "38.63");
        data2.put("futureCount", "2");
        data2.put("showAlarm", "0");
        data2.put("simCounty", "cn");
        
        Normal.add(data2);
        params.put("Normal", Normal);
        String r = HttpUtility.post("http://127.0.0.1:9285/", params.toJSONString(), null, "utf-8", 0);
        System.out.println(r);
        System.out.println(r.length());
        try {                                                                       //5uCItjTAohgFxo9a
            System.out.println(AESForNodejs.decrypt(r,"5uCItjTAohgFxo9a"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
