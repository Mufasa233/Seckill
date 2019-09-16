package cn.wolfcode.cloud.websocketserver.mq;

import cn.wolfcode.cloud.common.result.CodeMsg;
import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.websocketserver.config.WebSocketServer;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class MQListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MQConstants.ORDER_RESULT_NOTIFY_QUEUE),
            exchange = @Exchange(name = MQConstants.ORDER_RESULT_NOTIFY_EXCHANGE, type = "topic"),
            key = MQConstants.ORDER_RESULT_NOTIFY_KEY))
    public void headlerResultNotify(@Payload Map<String, Object> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException, InterruptedException {
        String uuid = (String) map.get("uuid");
        WebSocketServer client = WebSocketServer.clients.get(uuid);
        TimeUnit.SECONDS.sleep(1);
        if (channel != null) {
            int code = (int) map.get("code");
            if (code == 200) {//下单成功
                String orderNo = (String) map.get("orderNo");
                String result = JSON.toJSONString(Result.success(orderNo));//使用fastjson 把对象转换为json数据传输
                client.getSession().getBasicRemote().sendText(result);
            } else {
                String msg = (String) map.get("msg");
                String result = JSON.toJSONString(Result.error(new CodeMsg(code, msg)));
                client.getSession().getBasicRemote().sendText(result);
            }
        }
    }
}