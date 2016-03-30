package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum SystemErrorEnum {
    pass(9001, "The password is not correct"), //
    share(9002, "Only once a month"), //
    nouser(9003, "Not find this user"), //
    report(9004, "Only once a day"), //
    report_vip(9005, "VIP user can not report"), //
    report_anonymous(9006, "Anonymous user can not report"),
    ;

    private int status;
    private String error;

    SystemErrorEnum(int status, String error) {
        this.status = status;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
