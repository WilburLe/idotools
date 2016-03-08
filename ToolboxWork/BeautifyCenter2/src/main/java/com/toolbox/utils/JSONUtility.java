package com.toolbox.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class JSONUtility {

    public static JSONArray asc(JSONArray arr, String key) {
        JSONArray result = desc(arr, key);
        JSONArray result_ = new JSONArray();
        for (int i = result.size() - 1; i >= 0; i--) {
            result_.add(result.get(i));
        }
        return result_;
    }

    public static JSONArray desc(JSONArray arr, String key) {
        return desc(arr, null, key, new JSONArray());
    }

    private static JSONArray desc(JSONArray arr, JSONObject max, String key, JSONArray result) {
        if (max != null) {
            result.add(max);
        }
        if (arr == null || arr.size() == 0) {
            return result;
        }
        int temp = 0;
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = arr.getJSONObject(i);
            int sortNu = json.getIntValue(key);
            if (sortNu >= temp) {
                temp = sortNu;
                max = json;
            }
        }
        arr.remove(max);
        return desc(arr, max, key, result);
    }

}
