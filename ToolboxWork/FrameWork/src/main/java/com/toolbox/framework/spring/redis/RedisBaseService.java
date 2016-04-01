package com.toolbox.framework.spring.redis;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

public abstract class RedisBaseService<K, V> {
    private final static Log log = LogFactory.getLog(RedisBaseService.class);

    @Autowired
    public RedisTemplate<K, V> redisTemplate;

    protected RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    protected void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected RedisSerializer<String> getRedisSerializer() {
        return redisTemplate.getStringSerializer();
    }

    public void set(final K key, final V value) {
        set(key, value, 0);
        log.info(">>> redis set [" + key.toString() + "] cache success ~");
    }

    public void set(final K key, final V value, final long expiredTime) {
        BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
        if (expiredTime <= 0) {
            valueOper.set(value);
        } else {
            valueOper.set(value, expiredTime, TimeUnit.MILLISECONDS);
        }
    }

    public V get(final K key) {
        BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
        return valueOper.get();
    }

    public void del(K key) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    public Boolean check(K key) {
        Boolean flag = false;
        if (redisTemplate.hasKey(key)) {
            flag = true;
        }
        return flag;
    }

    public Boolean check(K key, V value) {
        Boolean flag = false;
        if (redisTemplate.hasKey(key)) {
            if (value.equals(get(key))) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 针对统计型的 key
     * 解决并发导致的数据减少问题
     * @param key
     *
     */
    public void addCount(final String key) {
        SessionCallback callback = new SessionCallback<Object>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                do {
                    operations.watch(key);
                    if (operations.hasKey(key)) {
                        BoundValueOperations<String, String> valueOper = operations.boundValueOps(key);
                        String val = valueOper.get();
                        operations.multi();
                        int count = Integer.parseInt(val);
                        count++;
                        valueOper.set(count + "");
                    } else {
                        operations.multi();
                        BoundValueOperations<String, String> valueOper = operations.boundValueOps(key);
                        valueOper.set("1");

                    }
                } while (operations.exec() == null);
                return null;
            }
        };
        redisTemplate.execute(callback);
    }
    //#######################################################//

    public void set(final String key, final String value, final int dbIndex) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(dbIndex);
                connection.set(key.getBytes(), value.getBytes());
                return null;
            }
        });
    }

    public String get(final String key, final int dbIndex) {
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    connection.select(dbIndex);
                    return new String(connection.get(key.getBytes()), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "";
            }
        });
    }

    public List<String> getKeys(final String pattern, final int dbIndex) {
        return redisTemplate.execute(new RedisCallback<List<String>>() {
            public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    connection.select(dbIndex);
                    List<String> result = new ArrayList<String>();
                    Set<byte[]> set = connection.keys(pattern.getBytes());
                    Iterator<byte[]> it = set.iterator();
                    while (it.hasNext()) {
                        byte[] bt = it.next();
                        String key = new String(bt, "utf-8");
                        result.add(key);
                    }
                    return result;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public boolean exists(final String key, final int dbIndex) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(dbIndex);
                return connection.exists(key.getBytes());
            }
        });
    }

}