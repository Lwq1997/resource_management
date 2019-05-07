package com.lwq.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

/**
 * @Author: Lwq
 * @Date: 2019/3/18 13:35
 * @Version 1.0
 * @Describe
 */
@Service("redisUtil")
public class RedisUtil {

    @Autowired
    RedisPool redisPool;

    /**
     * 获取当个对象
     * */
    public <T> T get(KeyPrefix prefix, String key,  Class<T> clazz) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            String  str = shardedJedis.get(realKey);
            T t =  stringToBean(str, clazz);
            return t;
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 设置对象
     * */
    public <T> boolean set(KeyPrefix prefix, String key,  T value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            String str = beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds <= 0) {
                shardedJedis.set(realKey, str);
            }else {
                shardedJedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  shardedJedis.exists(realKey);
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  shardedJedis.incr(realKey);
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  shardedJedis.decr(realKey);
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 删除
     * */
    public boolean delete(KeyPrefix prefix, String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            Long ret = shardedJedis.del(realKey);
            return ret>0;
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }


    public static  <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }
}
