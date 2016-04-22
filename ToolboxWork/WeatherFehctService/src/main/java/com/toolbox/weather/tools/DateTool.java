package com.toolbox.weather.tools;

import java.util.Calendar;

public class DateTool {

    public static int getHalfHourDate(Calendar date) {
        int MINUTE = date.get(Calendar.MINUTE);
        System.out.println(MINUTE);
        if (MINUTE > 30) {
            date.add(Calendar.HOUR_OF_DAY, 1);
            date.set(Calendar.MINUTE, 00);
        } else {
            date.set(Calendar.MINUTE, 30);
        }
        date.set(Calendar.SECOND, 00);
        return (int)(date.getTimeInMillis()/1000);
    }

    public static int getHalfHourDate() {
        return getHalfHourDate(Calendar.getInstance());
    }

    public static int getHalfHourDate(int unix) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(unix * 1000L);
        return getHalfHourDate(date);
    }
    
    public static void main(String[] args) {
        System.out.println(getHalfHourDate(1415546100));
    }
}
