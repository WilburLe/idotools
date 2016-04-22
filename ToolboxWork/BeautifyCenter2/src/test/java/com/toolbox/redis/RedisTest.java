package com.toolbox.redis;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.toolbox.framework.spring.redis.RedisBaseService;
import com.toolbox.framework.utils.ConfigUtility;
import com.toolbox.redis.tools.RedisPoolFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Component
public class RedisTest extends RedisBaseService<String, String> {

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
        JedisShardInfo shardInfo = new JedisShardInfo("127.0.0.1", 8379);
        shardInfo.setPassword("idotools.2016"); 
        Jedis jedis = new Jedis(shardInfo);
        jedis.select(4);
        jedis.set("test_1234", "123456789");
        String value = jedis.get("test_1234");
        System.out.println(value);
    }
    
    public static void main4(String[] args) {
        /* In this line, replace <name> with your cache name: */
        JedisShardInfo shardInfo = new JedisShardInfo("weather1.cat429.com", 7379);
        shardInfo.setPassword("idotools.2016"); /* Use your access key. */
        Jedis jedis = new Jedis(shardInfo);
        jedis.select(4);
        jedis.set("test_123", "123456");
        String value = jedis.get("test_123");
        System.out.println(value);
    }

    public static void main3(String[] args) {
        
        String m = ConfigUtility.getInstance().getString("redis.hostName")//
                + ConfigUtility.getInstance().getInteger("redis.port")//
                + ConfigUtility.getInstance().getInteger("redis.maxWait")//
                + ConfigUtility.getInstance().getString("redis.pass")//
                + ConfigUtility.getInstance().getInteger("redis.default.db", 0);//
        System.out.println(">>> " + m);
        List<String> keys = RedisPoolFactory.getInstance().keys(RedisPoolFactory.poolName.abroad.name(), "*", 5);
        for (int i = 0; i < keys.size(); i++) {
            System.out.println(i + " > " + keys.get(i));
        }
    }
}
