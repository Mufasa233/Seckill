package cn.wolfcode.cloud.seckill.mapper;

import cn.wolfcode.cloud.seckill.domian.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    @Insert("insert into t_order_info " +
            "(order_no,user_id,good_id,good_img,delivery_addr_id,good_name,good_count,good_price,seckill_price,status,create_date,pay_date)" +
            " values " +
            "(#{orderNo},#{userId},#{goodId},#{goodImg},#{deliveryAddrId},#{goodName},#{goodCount},#{goodPrice},#{seckillPrice},#{status},#{createDate},#{payDate})")
    int createOrder(OrderInfo orderInfo);

    @Select("select * from t_order_info where order_no =#{orderNo}")
    OrderInfo findByNo(@Param("orderNo") String orderNo);
}
