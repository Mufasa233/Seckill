package cn.wolfcode.cloud.seckill.service;

import cn.wolfcode.cloud.seckill.domian.OrderInfo;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;

public interface IOrderService {
    OrderInfo createOrderInfo(SeckillGoodVo seckillGoodVo, Long userId);

    OrderInfo findByNo(String orderNo);
}
