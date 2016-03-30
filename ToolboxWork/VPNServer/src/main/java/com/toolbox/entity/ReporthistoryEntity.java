package com.toolbox.entity;

import java.util.Date;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class ReporthistoryEntity {
    private int    id;
    private String username;
    private String bonus;
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

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public Date getReportdate() {
        return reportdate;
    }

    public void setReportdate(Date reportdate) {
        this.reportdate = reportdate;
    }

}
