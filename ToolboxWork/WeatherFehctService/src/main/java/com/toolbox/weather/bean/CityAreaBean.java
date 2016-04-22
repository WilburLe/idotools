package com.toolbox.weather.bean;

import java.util.ArrayList;
import java.util.List;

public class CityAreaBean {
    private int                     cityId;
    private int                     no;
    private List<CityAreaPointBean> points = new ArrayList<CityAreaPointBean>();

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public List<CityAreaPointBean> getPoints() {
        return points;
    }

    public void setPoints(List<CityAreaPointBean> points) {
        this.points = points;
    }

    public void addPoint(CityAreaPointBean point) {
        this.points.add(point);
    }

}
