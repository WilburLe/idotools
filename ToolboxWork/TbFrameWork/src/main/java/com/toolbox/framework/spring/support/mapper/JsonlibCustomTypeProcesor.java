package com.toolbox.framework.spring.support.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.StringUtility;


public class JsonlibCustomTypeProcesor implements CustomTypeProcesor {

    @Override
    public Object getObjectValue(ResultSet rs, int index, Class<?> propertyType) throws SQLException {
        Object value = null;
        String text = (String) JdbcUtils.getResultSetValue(rs, index, String.class);
        if (StringUtility.isNotEmpty(text)) {
            value = JSONObject.parse(text);
        }
        return value;
    }

    @Override
    public Object getDatabaseValue(Object value) {
        return value == null ? null : ((JSONObject) value).toString();
    }

    @Override
    public boolean isMatch(Class<?> propertyType) {
        if (JSONObject.class.equals(propertyType)) {
            return true;
        }
        return false;
    }
}
