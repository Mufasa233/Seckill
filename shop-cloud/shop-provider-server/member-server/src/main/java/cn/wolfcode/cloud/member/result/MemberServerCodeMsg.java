package cn.wolfcode.cloud.member.result;

import cn.wolfcode.cloud.common.result.CodeMsg;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;


@Getter@Setter
public class MemberServerCodeMsg extends CodeMsg{
    public MemberServerCodeMsg(int code,String msg){
        super(code,msg);
    }
    public static  final MemberServerCodeMsg  USER_CODE_MSG=new MemberServerCodeMsg(500100,"用户不存在");
    public static  final MemberServerCodeMsg  PASSWORD_CODE_MSG=new MemberServerCodeMsg(500101,"账户密码错误");
    public static  final MemberServerCodeMsg  PARAM_CODE_MSG=new MemberServerCodeMsg(500102,"参数错误:{0}");

    public MemberServerCodeMsg fillArgs(String msg){
        MemberServerCodeMsg memberServerCodeMsg = new MemberServerCodeMsg(this.getCode(), this.getMsg());
        memberServerCodeMsg.setMsg(MessageFormat.format(memberServerCodeMsg.getMsg(),msg));
        return memberServerCodeMsg;
    }
}
