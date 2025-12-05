package com.chy.controller;

import com.chy.pojo.Emp;
import com.chy.pojo.LoginInfo;
import com.chy.pojo.Result;
import com.chy.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;
    @Autowired
    private View error;

    @PostMapping("/login")
    public Result Login(@RequestBody Emp emp) {
        log.info("登录：{}",emp);
        LoginInfo info = empService.login(emp);
        if(info != null){
            return Result.success(info);
        }
        return Result.error("用户名或密码错误");
    }
}
