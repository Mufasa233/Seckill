package cn.wolfcode.cloud.seckill.vo;

import cn.wolfcode.cloud.good.domain.Good;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter@Setter
public class SeckillGoodVo extends Good {
    private BigDecimal seckillPrice;
    private Integer stockCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;
}
