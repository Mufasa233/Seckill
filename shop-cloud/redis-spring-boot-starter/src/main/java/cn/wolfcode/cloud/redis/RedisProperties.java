package cn.wolfcode.cloud.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shop.redis")
@Setter
@Getter
public class RedisProperties {
    private String host = "localhost";
    private int port = 6379;
    private int timeout = 10;
    private String password;
    private int poolMaxTotal = 500;
    private int poolMaxIdle = 500;
    private int poolMaxWait = 500;
}

