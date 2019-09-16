package cn.wolfcode.cloud.member.server;

import cn.wolfcode.cloud.member.domain.User;
import cn.wolfcode.cloud.member.vo.LoginVo;

public interface IUserService {
    User get(Long id);

    String login(LoginVo loginVo);

    boolean refreshCookie(String token);

    User getCurrentUser(String token);
}
