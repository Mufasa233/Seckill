package cn.wolfcode.cloud.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        try(Jedis jedis = jedisPool.getResource()){
            //拼接前缀
            String realKey=prefix.getKeyPrefix()+key;
            String strVal=beanToString(value);
            //判断是否有过期时间
            if(prefix.getExpireSeconds()>0){
                jedis.setex(realKey,prefix.getExpireSeconds(),strVal);
            }else{
                jedis.set(realKey,strVal);
            }
            return true;
        }
    }

    public <T> boolean setNx(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            String strVal = beanToString(value);
            if (prefix.getExpireSeconds() > 0) {
                jedis.setnx(realKey, strVal);
                jedis.expire(realKey, prefix.getExpireSeconds());
            } else {
                jedis.setnx(realKey, strVal);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            String strVal = jedis.get(realKey);
            return stringToBean(strVal, clazz);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.del(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long expire(KeyPrefix prefix, String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.expire(realKey, seconds);
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> void hset(KeyPrefix prefix, String key, String field, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            jedis.hset(realKey, field, beanToString(value));
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> T hget(KeyPrefix prefix, String key, String field, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            String strValue = jedis.hget(realKey, field);
            return stringToBean(strValue, clazz);
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> Map<String, T> hgetAll(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getKeyPrefix() + key;
            Map<String, String> map = jedis.hgetAll(realKey);
            Map<String, T> resultMap = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultMap.put(entry.getKey(), stringToBean(entry.getValue(), clazz));
            }
            return resultMap;
        } finally {
            returnToPool(jedis);
        }
    }

    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == float.class || clazz == Float.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return String.valueOf(value);
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T stringToBean(String value, Class<T> clazz) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == float.class || clazz == Float.class) {
            return (T) Float.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else {
            return JSON.parseObject(value, clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}