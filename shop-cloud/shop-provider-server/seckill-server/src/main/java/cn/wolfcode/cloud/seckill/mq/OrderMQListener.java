package cn.wolfcode.cloud.seckill.mq;

import cn.wolfcode.cloud.seckill.result.SeckillServerCodeMsg;
import cn.wolfcode.cloud.seckill.service.ISeckillGoodService;
import cn.wolfcode.cloud.seckill.service.ISeckillOrderService;
import cn.wolfcode.cloud.seckill.vo.SeckillGoodVo;
import cn.wolfcode.cloud.seckill.web.frontend.SecKillOrderController;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderMQListener {
    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private ISeckillGoodService seckillGoodService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //异步处理订单
    @RabbitListener(queuesToDeclare = {
            @Queue(MQConstants.ORDER_PEDDING_QUEUE)
    })
    public void handlerOrderPeddingQueue(@Payload OrderMessage orderMessage, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("uuid", orderMessage.getUuid());
        try {
            SeckillGoodVo seckillGoodVo = seckillGoodService.findCacheById(orderMessage.getGoodId());
            String orderNo = seckillOrderService.doSeckill(seckillGoodVo, orderMessage.getUserId());
            //发送成功的消息 到延时队列
            hashMap.put("code", 200);
            hashMap.put("goodId", orderMessage.getGoodId());
            hashMap.put("orderNo", orderNo);
            rabbitTemplate.convertAndSend(MQConstants.ORDER_RESULT_EXCHANGE, MQConstants.ORDER_RESULT_SUCCESS_KEY, hashMap);
        } catch (Exception e) {
            hashMap.put("code", SeckillServerCodeMsg.SECKILL_ERROR.getCode());
            hashMap.put("msg", SeckillServerCodeMsg.SECKILL_ERROR.getMsg());
            //往失败队列存放  发送创建失败的队列消息
            rabbitTemplate.convertAndSend(MQConstants.ORDER_RESULT_EXCHANGE, MQConstants.ORDER_RESULT_FAIL_KEY, hashMap);
            e.printStackTrace();
        } finally {
            channel.basicAck(deliveryTag, false);
        }
    }

    //处理创建订单失败的业务逻辑
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MQConstants.ORDER_RESULT_FAIL_QUEUE),
            exchange = @Exchange(name = MQConstants.ORDER_RESULT_EXCHANGE, type = "topic"),
            key = MQConstants.ORDER_RESULT_FAIL_KEY))
    public void handlerOrderFailMsg(@Payload Map<String, Object> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        //1回补预库存
        //2取消本地缓存标记
        try {
            Long goodId = (Long) map.get("goodId");
            seckillOrderService.syncStock(goodId);
        } catch (Exception e) {
            //可以把消息存放到对应的消息队列
            e.printStackTrace();
        } finally {
            channel.basicAck(deliveryTag, false);
        }
    }

    @Bean//创建一个bean对象  创建一个延时队列
    public org.springframework.amqp.core.Queue orderDelayQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", MQConstants.DELAY_EXCHANGE);//到期之后的消息的转发的交换机
        map.put("x-dead-letter-routing-key", MQConstants.ORDER_DELAY_KEY);//消息的路由key
        map.put("x-message-ttl", 1000 * 60 * 30);//设置队列的相关参数,消息的有效时间
        org.springframework.amqp.core.Queue queue = new org.springframework.amqp.core.Queue(MQConstants.ORDER_RESULT_SUCCESS_DELAY_QUEUE, true, false, false, map);
        return queue;
    }

    @Bean//创建一个绑定关系
    public Binding bind2Delay(org.springframework.amqp.core.Queue orderDelayQueue) {
        return BindingBuilder.bind(orderDelayQueue)//构建者的方法  队列
                .to(new TopicExchange(MQConstants.ORDER_RESULT_EXCHANGE))//交换机topic
                .with(MQConstants.ORDER_RESULT_SUCCESS_KEY);//路由key
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MQConstants.ORDER_TIMEOUT_QUEUE),
            exchange = @Exchange(name = MQConstants.DELAY_EXCHANGE, type = "topic"),
            key = MQConstants.ORDER_DELAY_KEY))
    public void handlerTimeOutQueue(@Payload Map<String, Object> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        //1.根据订单的编号去修改订单状态  lineCount 返回受影响的行数,如果是0,说明订单已经支付,如果>0说明修改成功,这个订单是超时订单
        try {
            Long goodId = (Long) (map.get("goodId"));
            Long orderNo = (Long) (map.get("orderNo"));
            int lineCount = seckillOrderService.updateStatus(orderNo);
            if (lineCount > 0) {
                //2.回补数据库中的库存
                seckillGoodService.incr(goodId);
                //3.回补预库存
                seckillGoodService.syncStock(goodId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.basicAck(deliveryTag, false);
        }


    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = MQConstants.SECKILL_OVER_SIGN_PUBSUB_EX, type = "fanout"))})
    public void handlerCancelSeckillOverSign(@Payload Long goodId, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        SecKillOrderController.overStockSing.put(goodId, false);
    }
}

















