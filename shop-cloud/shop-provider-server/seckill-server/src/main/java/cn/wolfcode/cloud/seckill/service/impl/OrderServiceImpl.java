package cn.wolfcode.cloud.seckill.service.impl;

import cn.wolfcode.cloud.seckill.domian.OrderInfo;
import cn.wolfcode.cloud.seckill.mapper.OrderMapper;
import cn.wolfcode.cloud.seckill.service.IOrderService;
import cn.wolfcode.cloud.seckill.util.IdGenerateUtil;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    public OrderInfo createOrderInfo(SeckillGoodVo seckillGoodVo, Long userId) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(IdGenerateUtil.get().nextId()+"");
        orderInfo.setUserId(userId);
        orderInfo.setGoodId(seckillGoodVo.getId());
        orderInfo.setGoodName(seckillGoodVo.getGoodName());
        orderInfo.setGoodImg(seckillGoodVo.getGoodImg());
        orderInfo.setGoodPrice(seckillGoodVo.getGoodPrice());
        orderInfo.setSeckillPrice(seckillGoodVo.getSeckillPrice());
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodCount(1);
        orderMapper.createOrder(orderInfo);
        return orderInfo;
    }

    public OrderInfo findByNo(String orderNo) {
        return orderMapper.findByNo(orderNo);
    }
}
