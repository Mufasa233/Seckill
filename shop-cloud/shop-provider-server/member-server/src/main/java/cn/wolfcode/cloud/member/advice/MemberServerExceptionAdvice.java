package cn.wolfcode.cloud.member.advice;

import cn.wolfcode.cloud.common.exception.BusinessException;
import cn.wolfcode.cloud.common.result.CodeMsg;
import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.member.result.MemberServerCodeMsg;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MemberServerExceptionAdvice {
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handlerBussnessException(BusinessException businessException){
        CodeMsg codeMsg = businessException.getCodeMsg();
        Result error = Result.error(codeMsg);
        return error;
    }
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result handlerBusinessException(BindException bindException){
        String msg = bindException.getAllErrors().get(0).getDefaultMessage();
        MemberServerCodeMsg memberServerCodeMsg = MemberServerCodeMsg.PARAM_CODE_MSG.fillArgs(msg);
        return Result.error(memberServerCodeMsg);
    }
}
