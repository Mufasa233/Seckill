package cn.wolfcode.cloud.seckill.web.frontend;

import cn.wolfcode.cloud.common.exception.BusinessException;
import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.member.domain.User;
import cn.wolfcode.cloud.redis.RedisService;
import cn.wolfcode.cloud.seckill.domian.OrderInfo;
import cn.wolfcode.cloud.seckill.domian.SeckillOrder;
import cn.wolfcode.cloud.seckill.mq.MQConstants;
import cn.wolfcode.cloud.seckill.mq.OrderMessage;
import cn.wolfcode.cloud.seckill.redis.SeckillKeyPrefix;
import cn.wolfcode.cloud.seckill.result.SeckillServerCodeMsg;
import cn.wolfcode.cloud.seckill.service.IOrderService;
import cn.wolfcode.cloud.seckill.service.ISeckillGoodService;
import cn.wolfcode.cloud.seckill.service.ISeckillOrderService;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/seckill")
public class SecKillOrderController {
    @Autowired
    private ISeckillGoodService seckillGoodService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //分段锁的标记
    public static ConcurrentHashMap<Long,Boolean>  overStockSing = new ConcurrentHashMap();

    @RequestMapping("/doSeckill")
    public Result<String> doSeckill(String uuid, Long goodId, User user) {


        //当前用户是否登录
        if (user == null) {
            return Result.error(SeckillServerCodeMsg.USER_NO_LOGIN);
        }

        //查询商品是否存在
        SeckillGoodVo seckillGoodVo = seckillGoodService.findCacheById(goodId);
        if (seckillGoodVo == null) {
            return Result.error(SeckillServerCodeMsg.OP_ERROR);
        }
        //判断当前是否在抢购活动中  秒杀商品的开始时间必须 < 当前时间
        if (seckillGoodVo.getStartDate().getTime() - new Date().getTime() > 0) {
            return Result.error(SeckillServerCodeMsg.OP_ERROR);
        }
        //判断库存是否充足 > 0
        Long num = redisService.decr(SeckillKeyPrefix.SECKILLGOODSTOCK, goodId + "");
        if (num < 0) {
            return Result.error(SeckillServerCodeMsg.SALE_OUT_ERROR);
        }

        //当前用户是否重复下单操作
        OrderInfo orderInfo = redisService.get(SeckillKeyPrefix.SECKILLORDER, user.getId() + ":" + goodId, OrderInfo.class);
        if (orderInfo != null) {
            return Result.error(SeckillServerCodeMsg.REPEAT_ORDER_ERROR);
        }
        /*try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        // 开始下单操作
//        String orderNo = seckillOrderService.doSeckill(seckillGoodVo,user.getId());
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setGoodId(goodId);
        orderMessage.setUserId(user.getId());
        orderMessage.setUuid(uuid);
        rabbitTemplate.convertAndSend("", MQConstants.ORDER_PEDDING_QUEUE, orderMessage);
        return Result.success("正在抢购中.....");
    }

    @RequestMapping("/findSeckillOrderByNo")
    public Result<OrderInfo> findSeckillOrderByNo(String orderNo) {
        OrderInfo orderInfo = orderService.findByNo(orderNo);
        return Result.success(orderInfo);
    }
}
