package com.toolbox.weather.bean;

import java.util.Date;

public class CityWeatherAlarmBean {
    private int    id;
    private int    cityId;
    private String weatherCityId; //对应天气网ID
    private String alarmType;     //类型
    private String alarmYs;       //颜色
    private Date   publicDate;    //发布时间
    private Date   createDate;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherCityId() {
        return weatherCityId;
    }

    public void setWeatherCityId(String weatherCityId) {
        this.weatherCityId = weatherCityId;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmYs() {
        return alarmYs;
    }

    public void setAlarmYs(String alarmYs) {
        this.alarmYs = alarmYs;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public Date getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
