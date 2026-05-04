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

import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;

    @PostMapping("/login")
    public Result Login(@RequestBody Emp emp) {
        log.info("登录：{}", emp);
        LoginInfo info = empService.login(emp);
        if(info != null){
            return Result.success(info);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/refresh")
    public Result refresh(@RequestBody Map<String, String> params) {
        String refreshToken = params.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Result.error("refreshToken 不能为空");
        }
        String newAccessToken = empService.refreshAccessToken(refreshToken);
        if (newAccessToken != null) {
            return Result.success(Map.of("accessToken", newAccessToken));
        }
        return Result.error("refreshToken 无效或已过期");
    }

    @PostMapping("/logout")
    public Result logout(@RequestBody Map<String, String> params) {
        String refreshToken = params.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Result.error("refreshToken 不能为空");
        }
        empService.logout(refreshToken);
        return Result.success();
    }
}
