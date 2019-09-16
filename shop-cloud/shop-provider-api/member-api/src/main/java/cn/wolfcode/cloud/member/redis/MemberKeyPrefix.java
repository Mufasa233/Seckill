package cn.wolfcode.cloud.member.redis;

import cn.wolfcode.cloud.redis.BaseKeyPrefix;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class MemberKeyPrefix extends BaseKeyPrefix {
    public MemberKeyPrefix(String prifix, int expireTime){
        super(prifix,expireTime);
    }
    public static MemberKeyPrefix USER_TOKEN = new MemberKeyPrefix("token",1800);
}
