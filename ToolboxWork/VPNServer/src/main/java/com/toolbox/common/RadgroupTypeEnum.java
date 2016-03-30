package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum RadgroupTypeEnum {
    FREE("Free", -1), //
    Guest("Guest", -2), //
    VIP1("VIP1", 7), //
    VIP2("VIP2", 30), //
    VIP3("VIP3", 365), //
    VIP4("VIP4", 365 * 2), //
    VIP5("VIP5", 365 * 3)//
    ;

    private String name;
    private int days;

    RadgroupTypeEnum(String name, int days) {
        this.name = name;
        this.days = days;
    }

    public static RadgroupTypeEnum byName(String name) {
        RadgroupTypeEnum result = null;
        RadgroupTypeEnum[] valus = RadgroupTypeEnum.values();
        for (int i = 0; i < valus.length; i++) {
            RadgroupTypeEnum group = valus[i];
            if (group.getName().equals(name)) {
                result = group;
                break;
            }
        }
        return result;
    }

    public static RadgroupTypeEnum byDays(int days) {
        RadgroupTypeEnum result = null;
        if (days <= RadgroupTypeEnum.FREE.getDays()) {
            result = RadgroupTypeEnum.FREE;
            return result;
        }
        if (days < RadgroupTypeEnum.VIP2.getDays()) {
            result = RadgroupTypeEnum.VIP1;
            return result;
        }

        if (days < RadgroupTypeEnum.VIP3.getDays()) {
            result = RadgroupTypeEnum.VIP2;
            return result;
        }
        //超过VIP3的都标记为VIP3
        if (days >= RadgroupTypeEnum.VIP3.getDays()) {
            result = RadgroupTypeEnum.VIP3;
            return result;
        }
        return result;
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
