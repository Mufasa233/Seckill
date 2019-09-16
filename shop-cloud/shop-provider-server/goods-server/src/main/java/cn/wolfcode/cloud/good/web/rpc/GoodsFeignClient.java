package cn.wolfcode.cloud.good.web.rpc;

import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.good.domain.Good;
import cn.wolfcode.cloud.good.feign.GoodFeignAPI;
import cn.wolfcode.cloud.good.service.IGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoodsFeignClient implements GoodFeignAPI {
    @Autowired
    private IGoodService goodService;


    public Result<List<Good>> queryIds(List<Long> ids) {
        return Result.success(goodService.queryByIds(ids));
    }
}
