package cn.wolfcode.cloud.seckill.web.frontend;

import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.seckill.service.ISeckillGoodService;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGood")
public class SeckillGoodController {
    @Autowired
    private ISeckillGoodService seckillGoodService;
    /*@RequestMapping("query")
    public Result<List<SeckillGoodVo>> query(){
        List<SeckillGoodVo> seckillGoodVos = seckillGoodService.query();
        return Result.success(seckillGoodVos);
    }*/
    @RequestMapping("query")
    public Result<List<SeckillGoodVo>> query() {
        //调用service的方法去处理List<SeckillGoodVo>
        List<SeckillGoodVo> seckillGoodVos = seckillGoodService.queryByCache();
        // 把结果封装为Result的结果返回
        return Result.success(seckillGoodVos);

    }

    /*@RequestMapping("/findById")
    public Result<SeckillGoodVo> findById(@RequestParam("id") Long id){
        return Result.success(seckillGoodService.findById(id));
    }*/


    @RequestMapping("findById")
    public Result<SeckillGoodVo> findById(@RequestParam("id") Long id) {
        return Result.success(seckillGoodService.findCacheById(id));
    }

    @RequestMapping("/initGoods")
    public Result<Boolean> initGoods() {
        seckillGoodService.initGoodsToCache();
        return Result.success(true);
    }


}
