package cn.wolfcode.cloud.member.web.rpc;

import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.member.feign.UserTokenFeignAPI;
import cn.wolfcode.cloud.member.server.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserTokenFeignClient implements UserTokenFeignAPI {

    @Autowired
    private IUserService userService;

    @Override
    public Result refreshCookie(String token) {
        userService.refreshCookie(token);
        return Result.success();
    }
}
