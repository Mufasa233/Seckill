package cn.wolfcode.cloud.member.web;

import cn.wolfcode.cloud.member.domain.User;
import cn.wolfcode.cloud.member.server.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @RequestMapping
    public User get(Long id){
        return userService.get(id);
    }
}
