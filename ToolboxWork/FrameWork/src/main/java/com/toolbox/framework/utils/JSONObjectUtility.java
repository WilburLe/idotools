package com.toolbox.framework.utils;

import static com.alibaba.fastjson.util.TypeUtils.castToInt;

import com.alibaba.fastjson.JSONObject;

public class JSONObjectUtility extends JSONObject {
    private static final long serialVersionUID = 4215528425635624490L;

    public String optString(String key, String def) {
        Object value = get(key);

        if (value == null) {
            return def;
        }

        return value.toString();
    }

    public int getIntValue(String key, int def) {
        Object value = get(key);

        if (value == null) {
            return def;
        }

        return castToInt(value).intValue();
    }
}
