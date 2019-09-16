package cn.wolfcode.cloud.seckill.web.advice;

import cn.wolfcode.cloud.common.exception.BusinessException;
import cn.wolfcode.cloud.common.result.CodeMsg;
import cn.wolfcode.cloud.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class SeckillGoodAdvice {

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Result<Boolean> handleBusinessException(BusinessException businessException){
        CodeMsg codeMsg = businessException.getCodeMsg();
        Result result = Result.error(codeMsg);
        return result;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result<Boolean> handleException(Exception exception){
        Result result = Result.defaultError();
        return result;
    }

}
