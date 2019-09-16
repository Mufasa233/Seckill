package cn.wolfcode.cloud.member.web.frontend;

import cn.wolfcode.cloud.common.result.Result;
import cn.wolfcode.cloud.member.domain.User;
import cn.wolfcode.cloud.member.server.IUserService;
import cn.wolfcode.cloud.member.util.CookieUtil;
import cn.wolfcode.cloud.member.util.DBUtil;
import cn.wolfcode.cloud.member.util.MD5Util;
import cn.wolfcode.cloud.member.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/token")
public class TokenController {


    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public Result<Boolean> login(@Valid LoginVo loginVo, HttpServletResponse response){
        String token = userService.login(loginVo);
        CookieUtil.addCookie(token,response);
        return Result.success();
    }

    /*@RequestMapping("/getCurrentUser")
    public Result<User> getCurrentUser(HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token");
        if(StringUtils.isEmpty(token)){
            return Result.success(null);
        }
        //有cookie信息, 调用service的方法去查询
        User user = userService.getCurrentUser(token);
        return Result.success(user);
    }*/
    @RequestMapping("/getCurrentUser")
    public Result<User> getCurrentUser(User user){
        System.err.println(user);

        return Result.success(user);
    }

    @RequestMapping("/initData")
    public Result<String> initData() throws Exception {
        List<User> users = initUser(500);
        insertToDb(users);
        createToken(users);
        return Result.success("");
    }

    private void createToken(List<User> users) throws Exception {
        File file = new File("D:/tokens.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0;i<users.size();i++) {
            LoginVo vo = new LoginVo();
            vo.setUsername(users.get(i).getId()+"");
            vo.setPassword(MD5Util.passwordForm("123456"));
            String token = userService.login(vo);
            String row = users.get(i).getId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
        }
        raf.close();
    }

    private void insertToDb(List<User> users) throws Exception {
        Connection conn = DBUtil.getConn();
        String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(int i=0;i<users.size();i++) {
            User user = users.get(i);
            pstmt.setInt(1, user.getLoginCount());
            pstmt.setString(2, user.getNickname());
            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setString(4, user.getSalt());
            pstmt.setString(5, user.getPassword());
            pstmt.setLong(6, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
    }
    private List<User> initUser(int count){
        List<User> users = new ArrayList<>();
        for(int i=0;i<count;i++){
            User user = new User();
            user.setId(13000000000L+i);
            user.setLoginCount(1);
            user.setNickname("user"+i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c4");
            user.setPassword(MD5Util.password2Db(MD5Util.passwordForm("123456"),user.getSalt()));
            users.add(user);
        }
        return users;
    }

}