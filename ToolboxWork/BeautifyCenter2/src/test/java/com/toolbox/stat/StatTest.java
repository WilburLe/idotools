package com.toolbox.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.toolbox.web.service.StatService;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class StatTest  {
    @Autowired
    private StatService service;
    
    public void test() {
    }
    
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
        StatTest test = context.getBean(StatTest.class);
        test.test();
    }
}
