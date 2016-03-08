package com.toolbox.redis;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class RedisTest extends AbstractRedisService<String, String> {

    
    public void test() {
        this.set("test", "10", 10);
    }
    
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
        RedisTest test = context.getBean(RedisTest.class);
        test.test();
    }
}
