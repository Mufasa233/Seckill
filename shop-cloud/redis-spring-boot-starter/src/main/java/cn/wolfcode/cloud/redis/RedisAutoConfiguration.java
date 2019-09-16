package cn.wolfcode.cloud.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConditionalOnClass({ Jedis.class, JedisPool.class })
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

    //创建一个jedisPool
    @Bean
    @ConditionalOnMissingBean
    public JedisPool jedisPool(RedisProperties redisProperties){
        //jedisPool的配置类对象
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getPoolMaxIdle());
        config.setMaxTotal(redisProperties.getPoolMaxTotal());
        config.setMaxWaitMillis(redisProperties.getPoolMaxWait());
        JedisPool jedisPool = new JedisPool(config,redisProperties.getHost(),
                redisProperties.getPort(),redisProperties.getTimeout(),redisProperties.getPassword());
        return jedisPool;
    }

    @Bean
    public Jedis jedis(){
        Jedis jedis = new Jedis();
        //jedis.auth(redisProperties.getPassword());
        return jedis;
    }

    @Bean
    public RedisService redisService(){
        RedisService redisService = new RedisService();
        return redisService;
    }

}
