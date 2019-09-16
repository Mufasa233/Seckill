package cn.wolfcode.cloud.member.web;

import cn.wolfcode.cloud.member.web.resolver.UserHandlerMethodResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
public class WebMemberConfigure implements WebMvcConfigurer {
    @Autowired
    private UserHandlerMethodResolver userHandlerMethodResolver;

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userHandlerMethodResolver);
    }
}
