package cn.wolfcode.cloud.member.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class User implements Serializable {

    private Long id;
    private String nickname;//昵称
    private String password;//密码
    private String salt;//盐
    private String head;//头像
    private Date registerDate;//注册时间
    private Date lastLoginDate;//最后登录时间
    private Integer loginCount;//登录次数

}
