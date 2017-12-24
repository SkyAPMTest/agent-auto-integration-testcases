package org.apache.skywalking.apm.testcase.jedis;

import redis.clients.jedis.Jedis;

public class RedisCommandExecutor {
    private Jedis jedis;

    public RedisCommandExecutor() {
        jedis = new Jedis(RedisConfig.getRedisHost(), RedisConfig.getRedisPort());
    }

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public void get(String key) {
        jedis.get(key);
    }

    public void del(String key) {
        jedis.del(key);
    }

    public void closeClient(){
        jedis.close();
    }
}
