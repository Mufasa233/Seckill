package cn.wolfcode.cloud.common.exception;

import cn.wolfcode.cloud.common.result.CodeMsg;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class BusinessException extends RuntimeException {
    private CodeMsg codeMsg;

    public BusinessException(CodeMsg codeMsg){
        this.codeMsg=codeMsg;
    }


}
