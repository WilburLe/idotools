package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum RadgroupTypeEnum {
    FREE("Free", -1), //
    VIP1("VIP1", 7), //
    VIP2("VIP2", 30), //
    VIP3("VIP3", 365), //
    VIP4("VIP4", -1), //
    VIP5("VIP5", -1)//
    ;

    private String name;
    private int days;

    RadgroupTypeEnum(String name, int days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

}
