package cn.wolfcode.cloud.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Setter
@Getter
public class LoginVo implements Serializable {
    @Pattern(regexp = "1\\d{10}",message = "手机号格式不合法")
    private String username;
    private String password;

}
