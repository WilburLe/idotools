package com.toolbox.framework.spring.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class RedisPoolFactory {

    enum a {
        ee, ww
    };

    public static void main(String[] args) {
        GenericObjectPoolConfig poolconfig = new GenericObjectPoolConfig();
        JedisPoolConfig config = new JedisPoolConfig();
        //        config.se
        config.setMaxIdle(1);
        config.setMaxTotal(100);
        config.setMaxWaitMillis(5 * 1000);
        config.setTestOnBorrow(Boolean.valueOf(("redis.pool.testOnBorrow")));
        JedisPool pool = new JedisPool(poolconfig, "localhost", 6379, 3000, "123456", 2, "client1");

        Jedis jedis = pool.getResource();
        jedis.set("province", "shannxi");
        String province = jedis.get("province");
        System.out.println(province);
        jedis.del("province");
        pool.returnResource(jedis);
    }

}
