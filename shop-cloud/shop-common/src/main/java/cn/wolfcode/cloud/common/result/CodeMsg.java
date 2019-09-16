package cn.wolfcode.cloud.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Setter@Getter@NoArgsConstructor@AllArgsConstructor
public class CodeMsg implements Serializable {
    private static final int SUCCESS_CODE=200;
    private static final int ERROR_CODE=500;
    private static final String ERROR_MSG="系统繁忙，稍后再试";
    private int code;
    private String msg;

}
