package cn.wolfcode.cloud.seckill.mq;

public class MQConstants {
    public static final String ORDER_PEDDING_QUEUE = "ORDER_PEDDING_QUEUE";//处理订单消息的队列
    public static final String ORDER_RESULT_EXCHANGE = "ORDER_RESULT_EXCHANGE";//订单结果的交换机
    public static final String ORDER_RESULT_SUCCESS_KEY = "ORDER.SUCCESS";//订单成功的路由Key
    public static final String ORDER_RESULT_SUCCESS_DELAY_QUEUE = "ORDER_SUCCESS_DELAY_QUEUE";//订单成功的延时队列名称
    public static final String ORDER_RESULT_FAIL_KEY = "ORDER.FAIL";//订单失败的路由Key
    public static final String ORDER_RESULT_FAIL_QUEUE = "ORDER_RESULT_FAIL_QUEUE";//订单失败的队列
    public static final String DELAY_EXCHANGE = "DELAY_EXCHANGE";//延时需要发送到哪个交换机
    public static final String ORDER_DELAY_KEY = "ORDER.TIMEOUT";//订单超时的路由Key
    public static final String ORDER_TIMEOUT_QUEUE = "ORDER_TIMEOUT_QUEUE";//订单超时的队列
    public static final String SECKILL_OVER_SIGN_PUBSUB_EX = "SECKILL_OVER_SIGN_PUBSUB_EX";//发布订阅的交换机

}