package cn.wolfcode.cloud.member.web.resolver;

import cn.wolfcode.cloud.member.domain.User;
import cn.wolfcode.cloud.member.redis.MemberKeyPrefix;
import cn.wolfcode.cloud.member.util.CookieUtil;
import cn.wolfcode.cloud.redis.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserHandlerMethodResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private RedisService redisService;
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == User.class;
    }

    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieUtil.getCookieValue(request, "token");
        if(StringUtils.isEmpty(token)){
            return null;
        }

        return redisService.get(MemberKeyPrefix.USER_TOKEN,token,User.class);
    }
}
