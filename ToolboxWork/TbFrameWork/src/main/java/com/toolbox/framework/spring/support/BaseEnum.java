package com.toolbox.framework.spring.support;

import java.io.Serializable;


public interface BaseEnum<ValueType> extends Serializable {

    public String getLable();

    public ValueType getValue();
}
