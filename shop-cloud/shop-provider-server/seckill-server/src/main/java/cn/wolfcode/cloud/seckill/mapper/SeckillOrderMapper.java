package cn.wolfcode.cloud.seckill.mapper;

import cn.wolfcode.cloud.seckill.domian.SeckillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SeckillOrderMapper {
    @Select(" select * from t_seckill_order where good_id=#{goodId} and user_id=#{userId} ")
    SeckillOrder findByUserIdAndGoodId(@Param("userId") Long userId, @Param("goodId") Long goodId);

    @Insert("insert into t_seckill_order(user_id,good_id,order_no) values (#{userId},#{goodId},#{orderNo})")
    int createSeckillOrder(SeckillOrder seckillOrder);

    @Select(" select stock_count from  t_seckill_goods  where good_id = #{goodId}")
    int findStock(@Param("goodId")  Long goodId);

    @Update("update t_seckill_goods set status = 3 where order_no=#{orderNo} and status = 0")
    int updateStatus(Long orderNo);
}
