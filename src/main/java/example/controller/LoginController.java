package example.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import example.dto.LoginDto;
import example.entity.UserInfo;
import example.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("login")
@Slf4j
public class LoginController {

    @Autowired
    UserInfoMapper userInfoMapper;

    @PostMapping("login")
    public String login(@RequestBody LoginDto loginDto ) throws IllegalAccessException {
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUsername, loginDto.getUsername()).eq(UserInfo::getPassword, loginDto.getPassword()));
        if (Objects.isNull(userInfo)){
            throw new IllegalAccessException("校验错误");
        }
        return "成功登录";
    }


    @PostMapping("register")
    public String register(@RequestBody LoginDto loginDto ) throws IllegalAccessException {
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUsername, loginDto.getUsername()).last("limit 1"));
        if (Objects.isNull(userInfo)){
            UserInfo userInfoInsert = new UserInfo( );
            userInfoInsert.setUsername(loginDto.getUsername());
            userInfoInsert.setPassword(loginDto.getPassword());
            userInfoMapper.insert(userInfoInsert);
            throw new IllegalAccessException("注册成功");
        }
        return "该用户名已经被使用了 请重新输入";
    }
}
