package cn.wolfcode.cloud.member.util;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    //定义一个固定的盐
    public static final String FORM_SALT="1a2b3c4";
    //第一个：前端对于表单数据密码的加密
    public static String passwordForm(String password){
        String saltPass = "" + FORM_SALT.charAt(0) + FORM_SALT.charAt(1) + password + FORM_SALT.charAt(4) + FORM_SALT.charAt(5);
        String fromPass = DigestUtils.md5Hex(saltPass );
        return fromPass;
    }
    public static String password2Db(String password,String salt){
        String saltPass = salt + password + salt;
        String dbPass = DigestUtils.md5Hex(saltPass);
        return dbPass;
    }
    public static void main(String[] args){
        String fromPass = passwordForm("123456");
        String s = password2Db(fromPass, MD5Util.FORM_SALT);
        System.out.println(fromPass);
        System.out.println(s);
    }
}
