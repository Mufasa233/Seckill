package cn.wolfcode.cloud.zuul.filter;

import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.member.feign.UserTokenFeignAPI;
import cn.wolfcode.cloud.zuul.util.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class CookieRefreshFilter extends ZuulFilter implements InitializingBean {
   @Autowired
   private UserTokenFeignAPI userFeignApi;

    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    public int filterOrder() {
        return 10;
    }

    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = CookieUtil.getCookieValue(request, "token");
        if(StringUtils.isEmpty(token)){
            return false;
        }
        context.set("token",token);
        return true;
    }

    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        String token = (String) context.get("token");
        Result<Boolean> result = userFeignApi.refreshCookie(token);
        if(result != null && result.getCode() == 200){
            if(result.getData()){
                CookieUtil.addCookie(token,context.getResponse());
            }
        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {
        //在bean对象初始化完成以后可以进行的一个数据初始化操作
        System.out.println(userFeignApi);
    }
}
