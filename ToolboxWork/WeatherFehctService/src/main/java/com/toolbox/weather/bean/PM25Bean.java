package com.toolbox.weather.bean;

public class PM25Bean {
    private int  id;
    private int  cityId;
    private int  pm25 = -1;
    private long createDate;    //创建及更新时间
    private int  ranking;       //排序
    private int  rankingTaiwan; //排序

    private String remark;
    private String cityName; //不进行存储
    private String source;   //数据来源    

    public int getRankingTaiwan() {
        return rankingTaiwan;
    }

    public void setRankingTaiwan(int rankingTaiwan) {
        this.rankingTaiwan = rankingTaiwan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String toString() {
        return this.cityId + "|" + this.cityName + "|" + this.pm25;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
