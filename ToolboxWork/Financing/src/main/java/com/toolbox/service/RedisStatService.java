package com.toolbox.service;

import java.util.Calendar;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.spring.redis.RedisBaseService;
import com.toolbox.framework.utils.DateUtility;

@Service("redisStatService")
public class RedisStatService extends RedisBaseService<String, String> {

    public void openstat(String code, String uid) {
        Calendar cd = Calendar.getInstance();
        String date = DateUtility.format(cd, "yyyy-MM-dd");
        long stime = cd.getTimeInMillis();
        long etime = DateUtility.parseDate(date + " 24:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
        long expiredTime = etime - stime;

        String c_key = code + "@" + date;
        String u_key = code + "@" + uid + "@" + date;

        boolean addUv = false;
        if (!this.check(u_key)) {
            addUv = true;
            this.set(u_key, u_key, expiredTime);
        }
        update(c_key, addUv);
    }

    private void update(final String key, final boolean addUv) {
        SessionCallback callback = new SessionCallback<Object>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                do {
                    operations.watch(key);
                    if (operations.hasKey(key)) {
                        BoundValueOperations<String, String> valueOper = operations.boundValueOps(key);
                        String val = valueOper.get();
                        //                        ValueOperations vops = operations.opsForValue();
                        //                        String val = (String) vops.get(key);
                        operations.multi();

                        JSONObject j_val = JSON.parseObject(val);
                        long pv = j_val.getLongValue("pv");
                        long uv = j_val.getLongValue("uv");
                        if (addUv) {
                            uv++;
                        }
                        pv++;
                        j_val.put("pv", pv);
                        j_val.put("uv", uv);

                        valueOper.set(j_val.toJSONString());
                        //                        vops.set(key, j_val.toJSONString());
                    } else {
                        operations.multi();
                        BoundValueOperations<String, String> valueOper = operations.boundValueOps(key);
                        JSONObject j_val = new JSONObject();
                        j_val.put("pv", 1);
                        j_val.put("uv", 1);
                        valueOper.set(j_val.toJSONString());

                    }
                } while (operations.exec() == null);
                return null;
            }
        };
        redisTemplate.execute(callback);
    }

    public JSON viewstat(String code, int days) {
        JSONArray arr = new JSONArray();
        Calendar cd = Calendar.getInstance();
        for (int i = 0; i < days; i++) {
            String date = DateUtility.format(cd, "yyyy-MM-dd");
            String key = code + "@" + date;
            if (this.check(key)) {
                String val = this.get(key);
                JSONObject json = new JSONObject();
                json.put("date", date);
                json.put("val", val);
                arr.add(json);
            }
            cd.add(Calendar.DAY_OF_MONTH, -1);
        }
        return arr;

    }

}
