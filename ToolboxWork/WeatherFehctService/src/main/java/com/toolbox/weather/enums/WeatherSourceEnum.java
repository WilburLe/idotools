package com.toolbox.weather.enums;

/**
 * 天气信息来源
 * 
 * 
 * 
 * @author <a href="mailto:runner36@163.com">runner36</a>
 * @author $Author: yangjunshuai $
 * @version $Revision:$ $Date:Jun 13, 2013$ 
 * @since Jun 13, 2013
 * @package com.toolbox.weather.enumation
 */
public enum WeatherSourceEnum {

    China(1, "中国大陆", ""), //
    TaiWan(2, "台湾", ""), //
    Hongkong(3, "香港", ""), //
    Macon(4, "澳门", ""); //

    int    code;
    String label;
    String url;

    WeatherSourceEnum(int code, String label, String url) {
        this.code = code;
        this.label = label;
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}