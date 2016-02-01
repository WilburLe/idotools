package com.toolbox.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/

public interface WallpaperService {
    public void save(JSONArray datas);

    public void save(JSONObject data);

    public void update(JSONObject data);

    public void delete(String id);

    public List<JSONObject> findByPage(String tag, int start, int size);

    public JSONObject findByElementId(String elementId);

}
