package cn.wolfcode.cloud.member.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void addCookie(String token, HttpServletResponse response){
        Cookie cookie = new Cookie("token",token);
        cookie.setMaxAge(1800);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    public static String getCookieValue(HttpServletRequest request, String token){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if(name.equals(token)){
                    String value = cookie.getValue();
                    return value;
                }
            }
        }
        return "";
    }
}
