package cn.wolfcode.cloud.seckill.redis;

import cn.wolfcode.cloud.redis.BaseKeyPrefix;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SeckillKeyPrefix extends BaseKeyPrefix {
    public SeckillKeyPrefix(String prefix,int expireTime){
        super(prefix,expireTime);
    }

    public static SeckillKeyPrefix SECKILLGOODHASH = new SeckillKeyPrefix("seckillgoodhash:",-1);
    public static SeckillKeyPrefix SECKILLGOODSTOCK = new SeckillKeyPrefix("seckillgoodhash:",-1);
    public static SeckillKeyPrefix SECKILLORDER = new SeckillKeyPrefix("seckillorder:",-1);
}
