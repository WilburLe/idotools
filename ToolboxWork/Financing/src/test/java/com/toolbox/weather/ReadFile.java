package com.toolbox.weather;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.toolbox.framework.spring.support.BaseDao;
import com.toolbox.framework.utils.FileUtility;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class ReadFile extends BaseDao{
    
    private void test() {
        try {
            List<String> lines = FileUtility.readLines(new File("/home/hope/Desktop/areaid_v.csv"), "utf8");
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] fs = line.split(",");
                insert("insert into weathercode (AREAID,NAMEEN,NAMECN,DISTRICTEN,DISTRICTCN,PROVEN,PROVCN,NATIONEN,NATIONCN) values (?,?,?,?,?,?,?,?,?)",
                        fs[0], fs[1], fs[2], fs[3], fs[4], fs[5], fs[6], fs[7], fs[8]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
        ReadFile test = context.getBean(ReadFile.class);
        test.test();
       
    }
}
