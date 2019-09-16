package cn.wolfcode.cloud.good.feign;

import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.good.domain.Good;
import cn.wolfcode.cloud.good.feign.hystrix.GoodsFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "goods-server",fallback = GoodsFeignHystrix.class)
public interface GoodFeignAPI {
    @RequestMapping("/goods/queryIds")
    public Result<List<Good>> queryIds(@RequestParam("ids") List<Long> ids);
}
