package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum SystemErrorEnum {
    pass(9001, "The password is not correct"), //
    share(9002, "Only once a month"), //
    nouser(9003, "Not find this user"), //
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
