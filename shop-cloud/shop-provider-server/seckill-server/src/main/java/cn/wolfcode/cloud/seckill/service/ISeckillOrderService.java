package cn.wolfcode.cloud.seckill.service;

import cn.wolfcode.cloud.seckill.domian.SeckillOrder;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;

public interface ISeckillOrderService {
    SeckillOrder findByUserIdAndGoodId(Long id, Long goodId);

    String doSeckill(SeckillGoodVo seckillGoodVo, Long id);

    void syncStock(Long goodId);

    int updateStatus(Long orderNo);
}
