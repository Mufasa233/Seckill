package cn.wolfcode.cloud.seckill.domian;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by wolfcode-lanxw
 */
@Setter
@Getter
public class SeckillOrder implements Serializable {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long goodId;
}
