package com.toolbox.weather.bean;

public class CityWeatherBean {
    private long   id;
    private int    cityId;
    private int    weatherCityId;
    private String qqCityId;
    private String fetchData;         //原始预测数据
    private String hourForcast;       //小时预测>JSON
    private String dayForcast;        //天预测>JSON
    private String liveInfo;          //生活指数数据>JSON
    private int    curCode   = -1;    //当前天气
    private String curCodeInfo;       //
    private double curTemp;           //当前气温
    private double low;               //最高
    private double high;              //最低
    private int    code      = -1;    //白天预测
    private String codeInfo;
    private int    nightCode = -1;    //夜间预测
    private String nightCodeInfo;
    private long   unixTime;          //几点发布的数据
    private int    windLevel;         //风力等级
    private String windLevelInfo;     //风力等级
    private String windDirection;     //风向
    private long   sunriseTime;       //日出时间
    private long   sunsetTime;        //日落时间
    private int    tempRanking;       //温度排行
    private int    tempRankingTaiwan; //台湾温度排行
    private int    pm25;              //pm25
    private int    pmRanking;         //pm排行
    private int    pmRankingTaiwan;   //台湾pm排行
    private String pmSource;          //pm来源
    private int    humidity;          //湿度
    private int    visibility;        //能见度
    private String trafficControls;   //尾号限行
    private String extInfo;           //扩展字段
    private String source;            //来源地址
    private long   createDate;
    private int    fetchCount;        //抓取次数

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getWeatherCityId() {
        return weatherCityId;
    }

    public void setWeatherCityId(int weatherCityId) {
        this.weatherCityId = weatherCityId;
    }

    public String getQqCityId() {
        return qqCityId;
    }

    public void setQqCityId(String qqCityId) {
        this.qqCityId = qqCityId;
    }

    public String getFetchData() {
        return fetchData;
    }

    public void setFetchData(String fetchData) {
        this.fetchData = fetchData;
    }

    public String getHourForcast() {
        return hourForcast;
    }

    public void setHourForcast(String hourForcast) {
        this.hourForcast = hourForcast;
    }

    public String getDayForcast() {
        return dayForcast;
    }

    public void setDayForcast(String dayForcast) {
        this.dayForcast = dayForcast;
    }

    public String getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(String liveInfo) {
        this.liveInfo = liveInfo;
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

    public double getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(double curTemp) {
        this.curTemp = curTemp;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }

    public int getNightCode() {
        return nightCode;
    }

    public void setNightCode(int nightCode) {
        this.nightCode = nightCode;
    }

    public String getNightCodeInfo() {
        return nightCodeInfo;
    }

    public void setNightCodeInfo(String nightCodeInfo) {
        this.nightCodeInfo = nightCodeInfo;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
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

    public long getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(long sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(long sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public int getTempRanking() {
        return tempRanking;
    }

    public void setTempRanking(int tempRanking) {
        this.tempRanking = tempRanking;
    }

    public int getTempRankingTaiwan() {
        return tempRankingTaiwan;
    }

    public void setTempRankingTaiwan(int tempRankingTaiwan) {
        this.tempRankingTaiwan = tempRankingTaiwan;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getPmRanking() {
        return pmRanking;
    }

    public void setPmRanking(int pmRanking) {
        this.pmRanking = pmRanking;
    }

    public int getPmRankingTaiwan() {
        return pmRankingTaiwan;
    }

    public void setPmRankingTaiwan(int pmRankingTaiwan) {
        this.pmRankingTaiwan = pmRankingTaiwan;
    }

    public String getPmSource() {
        return pmSource;
    }

    public void setPmSource(String pmSource) {
        this.pmSource = pmSource;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getFetchCount() {
        return fetchCount;
    }

    public void setFetchCount(int fetchCount) {
        this.fetchCount = fetchCount;
    }

    public String getTrafficControls() {
        return trafficControls;
    }

    public void setTrafficControls(String trafficControls) {
        this.trafficControls = trafficControls;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

}
