package cn.wolfcode.cloud.member.mapper;

import cn.wolfcode.cloud.member.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from t_user where id = #{id}")
    User selectByPrimaryKey(Long id);
}