package cn.wolfcode.cloud.redis;

public interface KeyPrefix {
    //前缀
    public String getKeyPrefix();
    //过期时间
    public int getExpireSeconds();

}

