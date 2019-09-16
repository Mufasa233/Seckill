package cn.wolfcode.cloud.seckill.mapper;

import cn.wolfcode.cloud.seckill.domian.SeckillGood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SeckillGoodMapper {

    @Select(" select * from  t_seckill_goods ")
    public List<SeckillGood> query();

    @Select(" select * from  t_seckill_goods  where good_id = #{id}")
    SeckillGood findById(@Param("id") Long id);

    @Update(" update t_seckill_goods set stock_count=stock_count-1 where good_id=#{goodId} and stock_count>0")
    int reduce(@Param("goodId") Long goodId);

    @Update("update t_seckill_goods set stock_count=stock_count+1 where good_id=#{goodId}")
    void incr(@Param("goodId") Long goodId);
}
