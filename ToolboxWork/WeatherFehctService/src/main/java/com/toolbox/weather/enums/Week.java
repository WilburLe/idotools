package com.toolbox.weather.enums;

/**
    * 中国天气网 天气定义(含与yahoo对应) 
    * @author yangjunshuai
    * @version 
    * @since Mar 22, 2013
    *
    */
public enum Week {

	MON(2,"Monday"),TUE(3,"Tuesday"),WED(4,"Wednesday"),THU(5,"Thursday"),FRI(6,"Friday"),SAT(7,"Saturday"),SUN(1,"Sunday");
    int code;
    String fullName;
    
    Week(int code, String fullName) {
    	this.code = code;
    	this.fullName = fullName;
    }
    
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
    
    

}
