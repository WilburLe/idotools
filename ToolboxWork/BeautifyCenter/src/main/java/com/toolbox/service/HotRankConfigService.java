package com.toolbox.service;
/**
* @author E-mail:86yc@sina.com
* 
*/

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public interface HotRankConfigService {

    public void save(JSONArray datas);

    public void save(JSONObject data);

    public void update(JSONObject data);

    public void delete(String id);

    public int count(String tag);

    public List<JSONObject> findByPage(String tag, int start, int size);

    public JSONObject findByElementId(String elementId);

}
