package com.toolbox.weather.bean;

public class CurWeather {
    private double curTemp;          //当前温度
    private int    curCode;          //天气code
    private String curCodeInfo;      //天气code对应信息
    private int    windLevel;        //风力等级
    private String windLevelInfo;    //
    private String windDirection;    //风向
    private double relativeHumidity; //相对湿度

    public double getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(double curTemp) {
        this.curTemp = curTemp;
    }

    public int getCurCode() {
        return curCode;
    }

    public void setCurCode(int curCode) {
        this.curCode = curCode;
    }

    public String getCurCodeInfo() {
        return curCodeInfo;
    }

    public void setCurCodeInfo(String curCodeInfo) {
        this.curCodeInfo = curCodeInfo;
    }

    public int getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(int windLevel) {
        this.windLevel = windLevel;
    }

    public String getWindLevelInfo() {
        return windLevelInfo;
    }

    public void setWindLevelInfo(String windLevelInfo) {
        this.windLevelInfo = windLevelInfo;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public double getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

}
