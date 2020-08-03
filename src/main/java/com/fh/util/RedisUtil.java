package com.fh.util;

import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisUtil {

    //删除
    public static Long del(String key){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            Long del = jedis.del(key);
            return del;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    //删除
    public static Long hdel(String key,String field){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            Long res = jedis.hdel(key, field);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    //设置
    public static void set(String key,String value){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    //设置
    public static void setEx(String key,String value,int seconds){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    public static void hset(String key,String field,String value){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            jedis.hset(key,field,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    public static String hget(String key,String field){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            String res = jedis.hget(key, field);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    public static List<String> hget(String key){
        Jedis jedis =null;
        List<String> res = null;
        try {
            jedis = RedisPool.getRedisPool();
            res = jedis.hvals(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
        return res;
    }

    public static boolean exist(String key,String field){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            Boolean res = jedis.hexists(key, field);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    public static boolean exist(String key){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            Boolean res = jedis.exists(key);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    public static void expire(String key,int seconds){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            jedis.expire(key,seconds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }

    //获取
    public static String get(String key){
        Jedis jedis =null;
        try {
            jedis = RedisPool.getRedisPool();
            String res = jedis.get(key);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }
}
