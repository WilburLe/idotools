package com.toolbox.weather.tools;


public class CityTool {
    
    public static boolean isChinaCity(int id){
        if(id>10000 && !isTWCity(id) &&!isHongKong(id) && !isMacao(id))
            return true;
        return false;
    }
    
    public static boolean isTWCity(int id){
        if(id>=12140 && id <=12165)
            return true;
        return false;
    }
    
    public static boolean isHongKong(int id){
        if(id==12070)
            return true;
        return false;
    }
    
    public static boolean isMacao(int id){
        if(id==12071)
            return true;
        return false;
    }
    
    
    
 }

