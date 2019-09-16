package cn.wolfcode.cloud.redis;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseKeyPrefix implements KeyPrefix {

    private String keyPrefix;
    private  int  expireSeconds;

    public BaseKeyPrefix() {
    }

    public BaseKeyPrefix(String keyPrefix, int expireSeconds) {
        this.keyPrefix = keyPrefix;
        this.expireSeconds = expireSeconds;
    }

    @Override
    public String getKeyPrefix() {
        return this.keyPrefix;
    }

    @Override
    public int getExpireSeconds() {
        return this.expireSeconds;
    }
}
