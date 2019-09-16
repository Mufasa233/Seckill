package cn.wolfcode.cloud.good.feign.hystrix;

import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.good.domain.Good;
import cn.wolfcode.cloud.good.feign.GoodFeignAPI;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoodsFeignHystrix implements GoodFeignAPI {

    public Result<List<Good>> queryIds(List<Long> ids) {

        return null;
    }
}
