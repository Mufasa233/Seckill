package cn.wolfcode.cloud.member.server.impl;

import cn.wolfcode.cloud.common.exception.BusinessException;
import cn.wolfcode.cloud.member.domain.User;
import cn.wolfcode.cloud.member.mapper.UserMapper;
import cn.wolfcode.cloud.member.redis.MemberKeyPrefix;
import cn.wolfcode.cloud.member.result.MemberServerCodeMsg;
import cn.wolfcode.cloud.member.server.IUserService;
import cn.wolfcode.cloud.member.util.MD5Util;
import cn.wolfcode.cloud.member.vo.LoginVo;
import cn.wolfcode.cloud.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
    public User get(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public String login(LoginVo loginVo) {
        User user = this.get(Long.valueOf(loginVo.getUsername()));
        if(user==null){
            throw new BusinessException(MemberServerCodeMsg.USER_CODE_MSG);
        }
        String formPass = loginVo.getPassword();
        String salt = user.getSalt();
        String dbPass = MD5Util.password2Db(formPass,salt);
        if(!dbPass.equals(user.getPassword())){
            throw new BusinessException(MemberServerCodeMsg.PASSWORD_CODE_MSG);
        }
        //创建token
        String token=createToken(user);
        return token;
    }

    public boolean refreshCookie(String token) {
        boolean exists = redisService.exists(MemberKeyPrefix.USER_TOKEN, token);
        if(exists){
            redisService.expire(MemberKeyPrefix.USER_TOKEN,token,MemberKeyPrefix.USER_TOKEN.getExpireSeconds());
        }
        return true;
    }

    public User getCurrentUser(String token) {
        User user = redisService.get(MemberKeyPrefix.USER_TOKEN, token, User.class);
        return user;
    }

    private String createToken(User user) {
        String token = UUID.randomUUID().toString().replace("-","");
        redisService.set(MemberKeyPrefix.USER_TOKEN,token,user);
        return token;
    }

}