package cn.wolfcode.cloud.seckill.service;

import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;

import java.util.List;

public interface ISeckillGoodService {
    public List<SeckillGoodVo>  query();

    SeckillGoodVo findById(Long id);

    int reduce(Long id);

    void initGoodsToCache();

    List<SeckillGoodVo>  queryByCache();

    SeckillGoodVo findCacheById(Long id);;

    void incr(Long goodId);

    void syncStock(Long goodId);
}
