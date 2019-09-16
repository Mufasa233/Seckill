package cn.wolfcode.cloud.seckill.service.impl;

import cn.wolfcode.cloud.common.exception.BusinessException;
import cn.wolfcode.cloud.redis.RedisService;
import cn.wolfcode.cloud.seckill.domian.OrderInfo;
import cn.wolfcode.cloud.seckill.domian.SeckillOrder;
import cn.wolfcode.cloud.seckill.mapper.OrderMapper;
import cn.wolfcode.cloud.seckill.mapper.SeckillOrderMapper;
import cn.wolfcode.cloud.seckill.mq.MQConstants;
import cn.wolfcode.cloud.seckill.redis.SeckillKeyPrefix;
import cn.wolfcode.cloud.seckill.result.SeckillServerCodeMsg;
import cn.wolfcode.cloud.seckill.service.IOrderService;
import cn.wolfcode.cloud.seckill.service.ISeckillGoodService;
import cn.wolfcode.cloud.seckill.service.ISeckillOrderService;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ISeckillOrderServiceImpl implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private ISeckillGoodService seckillGoodService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public SeckillOrder findByUserIdAndGoodId(Long userId, Long goodId) {
        return seckillOrderMapper.findByUserIdAndGoodId(userId, goodId);
    }

    @Override
    @Transactional
    public String doSeckill(SeckillGoodVo seckillGoodVo, Long id) {
        //扣减库存
        int count = seckillGoodService.reduce(seckillGoodVo.getId());
        if (count > 0) {
            //创建商品订单
            OrderInfo orderInfo = orderService.createOrderInfo(seckillGoodVo, id);
            //创建秒杀订单
            createSeckillOrder(orderInfo);
            //存放订单信息到redis缓存
            redisService.setNx(SeckillKeyPrefix.SECKILLORDER, orderInfo.getUserId() + ":" + orderInfo.getGoodId(), orderInfo);
            return orderInfo.getOrderNo();
        } else {
            throw new BusinessException(SeckillServerCodeMsg.SALE_OUT_ERROR);
        }
    }

    public void syncStock(Long goodId) {
        //1回补预库存
        int StockNum = seckillOrderMapper.findStock(goodId);
        if (StockNum > 0) {
            redisService.set(SeckillKeyPrefix.SECKILLGOODSTOCK, goodId + "", StockNum);
            //取消标记位
            rabbitTemplate.convertAndSend(MQConstants.SECKILL_OVER_SIGN_PUBSUB_EX,"",goodId);
        }

    }

    public int updateStatus(Long orderNo) {
        return seckillOrderMapper.updateStatus(orderNo);
    }

    public int createSeckillOrder(OrderInfo orderInfo) {
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodId(orderInfo.getGoodId());
        seckillOrder.setOrderNo(orderInfo.getOrderNo());
        seckillOrder.setUserId(orderInfo.getUserId());
        int count = seckillOrderMapper.createSeckillOrder(seckillOrder);
        return count;
    }
}
