package cn.wolfcode.cloud.seckill.result;

import cn.wolfcode.cloud.common.result.CodeMsg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeckillServerCodeMsg extends CodeMsg {
    public SeckillServerCodeMsg(int code, String msg){
        super(code,msg);
    }
    public static final SeckillServerCodeMsg USER_NO_LOGIN=new SeckillServerCodeMsg(500201,"亲,请先登录再来操作");
    public static final SeckillServerCodeMsg EMPTY_GOOD_LIST = new SeckillServerCodeMsg(500200,"商品信息为空");
    public static final SeckillServerCodeMsg OP_ERROR=new SeckillServerCodeMsg(500202,"非法操作, 请注意");
    public static final SeckillServerCodeMsg SALE_OUT_ERROR=new SeckillServerCodeMsg(500203,"你来晚了, 已经抢购完");
    public static final SeckillServerCodeMsg REPEAT_ORDER_ERROR=new SeckillServerCodeMsg(500204,"重复下单");
    public static final SeckillServerCodeMsg SECKILL_ERROR=new SeckillServerCodeMsg(500205,"抢购失败!!");
}
