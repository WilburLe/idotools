package com.toolbox.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
* @author E-mail:86yc@sina.com
* 
*/
public interface TagEditService {

    public List<JSONObject> findAllTag();

    public JSONObject findTagByName(String name);

    public void save(JSONObject data);

    public void update(JSONObject data);
    
    public void delete(String name, String uuid);
}
