package com.toolbox.framework.spring.message;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<JSON> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /** Creates a new instance of the {@code ByteArrayHttpMessageConverter}. */
    public JsonHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return JSON.class.isAssignableFrom(clazz);
    }

    @Override
    protected JSONObject readInternal(Class<? extends JSON> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
        String jsonStr = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
        return JSONObject.parseObject(jsonStr);
    }

    @Override
    protected void writeInternal(JSON json, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
        outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //将返回的结果封装
        JSONObject rs = new JSONObject();
        rs.put("status", 200);
        rs.put("data", json);
        if (json instanceof JSONObject) {
            JSONObject data = JSONObject.parseObject(json.toJSONString());
            //校验AOP错误信息
            if (data.containsKey("status") && data.getIntValue("status") == -999) {
                rs = data;
            }
        }
        
        FileCopyUtils.copy(rs.toString(), new OutputStreamWriter(outputMessage.getBody(), charset));
    }

    protected Charset getContentTypeCharset(MediaType contentType) {
        if (contentType != null && contentType.getCharSet() != null) {
            return contentType.getCharSet();
        } else {
            return DEFAULT_CHARSET;
        }
    }

}
