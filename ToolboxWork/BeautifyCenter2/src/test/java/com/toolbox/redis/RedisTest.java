package com.toolbox.redis;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.toolbox.web.service.AbstractRedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class RedisTest extends AbstractRedisService<String, String> {

    public void test() {
        this.set("test", "10", 10);
    }

    public static void main1(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
        RedisTest test = context.getBean(RedisTest.class);
        test.test();
    }

    public static void main(String[] args) {
        /* In this line, replace <name> with your cache name: */
        JedisShardInfo shardInfo = new JedisShardInfo("127.0.0.1", 19000);
        shardInfo.setPassword("xDQBIqLA+nNLk6QpRxQvjoXkzpnZ4zvntEpOSMyXtQY="); /* Use your access key. */
        Jedis jedis = new Jedis(shardInfo);
        jedis.select(1);
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        System.out.println(value);
    }
}
