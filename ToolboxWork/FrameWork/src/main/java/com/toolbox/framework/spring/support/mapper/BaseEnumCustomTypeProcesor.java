package com.toolbox.framework.spring.support.mapper;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.support.JdbcUtils;

import com.toolbox.framework.spring.exception.SysException;
import com.toolbox.framework.spring.support.BaseEnum;

public class BaseEnumCustomTypeProcesor implements CustomTypeProcesor {

    private Map<Class<?>, Class<?>> valueTypes = new HashMap<Class<?>, Class<?>>();

    private Class<?> getValueType(Class<?> propertyType) {
        Class<?> valueType = valueTypes.get(propertyType);
        if (valueType == null) {
            try {
                Method getValueMethod = propertyType.getMethod("getValue", new Class[] {});
                valueType = getValueMethod.getReturnType();
            } catch (Exception e) {
                throw new SysException(e);
            }
            valueTypes.put(propertyType, valueType);
        }
        return valueType;
    }

    @Override
    public Object getObjectValue(ResultSet rs, int index, Class<?> propertyType) throws SQLException {
        Class<?> valueType = getValueType(propertyType);
        Object v = JdbcUtils.getResultSetValue(rs, index, valueType);
        if (v != null) {
            BaseEnum<?>[] enums = (BaseEnum<?>[]) propertyType.getEnumConstants();
            for (BaseEnum<?> n : enums) {
                if (n.getValue().equals(v)) {
                    return n;
                }
            }
        }
        return null;
    }

    @Override
    public Object getDatabaseValue(Object value) {
        return value == null ? null : ((BaseEnum<?>) value).getValue();
    }

    @Override
    public boolean isMatch(Class<?> propertyType) {
        if (BaseEnum.class.isAssignableFrom(propertyType)) {
            return true;
        }
        return false;
    }

}
