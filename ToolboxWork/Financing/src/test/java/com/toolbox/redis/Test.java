package com.toolbox.redis;

import java.util.Set;

import com.toolbox.redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;

public class Test {

    
    public static void main(String[] args) {
        Jedis jedis = new Jedis("123.56.47.52", 6379);
        jedis.auth("123.123");
        System.out.println(jedis.dbSize());
        jedis.select(1);
        Set<String> vals = jedis.keys("test@*");
        String vs = JSON.toJSONString(vals);
        System.out.println(vs);
        System.out.println(jedis.get("test1"));
//        jedis.set("u1", "8888888888888888");

        System.out.println(jedis.get("u1"));
    }
}
