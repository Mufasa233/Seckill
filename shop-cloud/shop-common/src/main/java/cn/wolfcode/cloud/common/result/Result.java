package cn.wolfcode.cloud.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code; //状态码
    private String msg; //错误信息
    private T data;// 返回数据

    public static Result<Boolean> success() {
        return  new  Result<Boolean>(200,"",true);
    }
    public static <T>  Result<T> success(T data) {
        return  new  Result<T>(200,"",data);
    }
    public static Result error(CodeMsg codeMsg) {
        return new  Result<Boolean>(codeMsg.getCode(),codeMsg.getMsg(),null);
    }
    public static Result defaultError(){
        return new Result<Boolean> (500,"系统出现异常",null);
    }
}
