package com.toolbox.common;

/**
* @author E-mail:86yc@sina.com
* 
*/
public enum UserEnum {
    named(1024 * 1024), // 
    anonymous(512 * 1024);

    private long dataRemain;

    UserEnum(long dataRemain) {
        this.dataRemain = dataRemain;
    }

    public long getDataRemain() {
        return dataRemain;
    }

    public void setDataRemain(long dataRemain) {
        this.dataRemain = dataRemain;
    }

}
