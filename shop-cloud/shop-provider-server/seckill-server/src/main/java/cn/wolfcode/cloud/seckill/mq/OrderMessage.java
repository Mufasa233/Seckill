package cn.wolfcode.cloud.seckill.mq;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderMessage implements Serializable {

    private String uuid;
    private Long goodId;
    private Long userId;
}
