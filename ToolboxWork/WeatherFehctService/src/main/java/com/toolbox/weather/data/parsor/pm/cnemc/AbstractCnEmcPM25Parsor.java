package com.toolbox.weather.data.parsor.pm.cnemc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.weather.bean.PM25Bean;

/**
 * 根据环境保卫厅网站 获取PM信息
 * 
 * @author yangjunshuai
 * @version 
 * @since Mar 20, 2013
 *
 */
public abstract class AbstractCnEmcPM25Parsor {
    protected Log log = LogFactory.getLog(getClass());
    
    protected String url;
    protected Map<String,String> params = null;
    protected String encoding = "utf-8";
    
    
    public abstract List<PM25Bean> parsePM25ByHtml(String htmlStr);
  
    public List<PM25Bean> parsePM25Beans(){
        return parsePM25ByHtml(requestURL());
    }
    
    public int parsePM25Value(){
        return -1;
    }
    
    protected void init(String url,Map<String,String> params,String encoding){
        this.url = url;
        this.params = params;
        this.encoding = encoding;
    }
    
    protected String requestURL(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("time", System.currentTimeMillis()+"");
        return HttpUtility.get(url,map,encoding);
    }

    public String getUrl() {
        return url;
    }

    public AbstractCnEmcPM25Parsor setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public AbstractCnEmcPM25Parsor setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public AbstractCnEmcPM25Parsor setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }
    
}
