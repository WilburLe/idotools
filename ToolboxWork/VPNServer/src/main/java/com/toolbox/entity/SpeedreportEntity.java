package com.toolbox.entity;

import java.util.Date;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class SpeedreportEntity {
    private int    id;
    private String username;
    private String countrycode;
    private String servername;
    private float  averagetime;
    private float  failrate;
    private Date   reportdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public float getAveragetime() {
        return averagetime;
    }

    public void setAveragetime(float averagetime) {
        this.averagetime = averagetime;
    }

    public float getFailrate() {
        return failrate;
    }

    public void setFailrate(float failrate) {
        this.failrate = failrate;
    }

    public Date getReportdate() {
        return reportdate;
    }

    public void setReportdate(Date reportdate) {
        this.reportdate = reportdate;
    }

}
