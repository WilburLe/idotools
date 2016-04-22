package com.toolbox.redis.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.toolbox.framework.thread.ThreadManager;
import com.toolbox.framework.utils.ConfigUtility;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* @author E-mail:86yc@sina.com
* 
*/
public class RedisPoolFactory {
    private static final Log              log          = LogFactory.getLog(RedisPoolFactory.class);
    private final Map<String, JedisPool>  jedisPoolMap = new HashMap<String, JedisPool>();
    private static final RedisPoolFactory instance     = new RedisPoolFactory();

    public enum poolName {
        china, abroad
    };

    private RedisPoolFactory() {
        jedisPoolMap.put(poolName.china.name(), createPool(//
                ConfigUtility.getInstance().getString("redis.hostName")//
                , ConfigUtility.getInstance().getInteger("redis.port")//
                , ConfigUtility.getInstance().getInteger("redis.maxWait")//
                , ConfigUtility.getInstance().getString("redis.pass")//
                , ConfigUtility.getInstance().getInteger("redis.default.db", 0)//
                , poolName.china.name()//
        ));
        jedisPoolMap.put(poolName.abroad.name(), createPool(//
                ConfigUtility.getInstance().getString("redis2.hostName")//
                , ConfigUtility.getInstance().getInteger("redis2.port")//
                , ConfigUtility.getInstance().getInteger("redis2.maxWait")//
                , ConfigUtility.getInstance().getString("redis2.pass")//
                , ConfigUtility.getInstance().getInteger("redis2.default.db", 0)//
                , poolName.abroad.name()//
        ));
    }

    private JedisPool createPool(String host, int port, int timeout, String pass, int defaultDb, String clientName) {
        GenericObjectPoolConfig poolconfig = new GenericObjectPoolConfig();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setMaxTotal(100);
        config.setMaxWaitMillis(5 * 1000);
        config.setTestOnBorrow(true);
        log.info(">>> create redis pool [" + clientName + " > " + host + ":" + port + " db:" + defaultDb + "] success .");
        return new JedisPool(poolconfig, host, port, timeout, pass, defaultDb, clientName);
    }

    public static RedisPoolFactory getInstance() {
        return instance;
    }

    public JedisPool getJedisPool(String poolName) {
        return jedisPoolMap.get(poolName);
    }

    private void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }

    public List<String> keys(String poolName, String pattern, int db) {
        JedisPool pool = null;
        Jedis jedis = null;
        List<String> result = new ArrayList<String>();
        try {
            pool = jedisPoolMap.get(poolName);
            jedis = pool.getResource();
            if (db >= 0) {
                jedis.select(db);
            }
            Set<String> set = jedis.keys(pattern);
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                result.add(key);
            }
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return result;
    }

    public String get(String poolName, String key) {
        return get(poolName, -1, key);
    }

    public String get(String poolName, int db, String key) {
        String value = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = jedisPoolMap.get(poolName);
            jedis = pool.getResource();
            if (db >= 0) {
                jedis.select(db);
            }
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return value;
    }

    public boolean set(String poolName, String key, String val) {
        return set(poolName, -1, key, val);
    }

    public boolean set(String poolName, int db, String key, String val) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = jedisPoolMap.get(poolName);
            jedis = pool.getResource();
            if (db >= 0) {
                jedis.select(db);
            }
            result = jedis.set(key, val);
        } catch (Exception e) {
            //释放redis对象  
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池  
            returnResource(pool, jedis);
        }
        return "OK".equals(result);
    }

    public boolean setex(String poolName, String key, String val, int seconds) {
        return setex(poolName, -1, key, val, seconds);
    }

    public boolean setex(String poolName, int db, String key, String val, int seconds) {
        String result = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = jedisPoolMap.get(poolName);
            jedis = pool.getResource();
            if (db >= 0) {
                jedis.select(db);
            }
            result = jedis.setex(key, seconds, val);
        } catch (Exception e) {
            //释放redis对象  
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池  
            returnResource(pool, jedis);
        }
        return "OK".equals(result);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final int m = i;
            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        String poolName = j % 2 == 0 ? RedisPoolFactory.poolName.china.name() : RedisPoolFactory.poolName.abroad.name();
                        String rk = m + "_" + j + "_" + poolName + "_test";
                        String val = m + "@" + j + "";
                        boolean setr = RedisPoolFactory.getInstance().set(poolName, rk, val);
                        String v = RedisPoolFactory.getInstance().get(poolName, rk);
                        System.out.println(setr + " > " + rk + " > " + v);
                    }
                }
            });
        }
        //        jedis.del("province");
        //        pool.returnResource(jedis);
        //        pool.destroy();
    }

}
