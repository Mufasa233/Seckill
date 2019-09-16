package cn.wolfcode.cloud.member.feign;

import cn.wolfcode.cloud.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "member-server")
public interface UserTokenFeignAPI {
    @RequestMapping("/token/refreshCookie")
    @ResponseBody
    public Result refreshCookie (@RequestParam("token") String token);
}
