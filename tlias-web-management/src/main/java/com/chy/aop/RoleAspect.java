package com.chy.aop;

import com.chy.pojo.Result;
import com.chy.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RoleAspect {

    @Around("@annotation(com.chy.anno.RequireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        String role = CurrentHolder.getCurrentRole();
        if (!"admin".equals(role)) {
            log.warn("权限不足, 当前角色: {}", role);
            return Result.error("权限不足，仅管理员可执行此操作");
        }
        return joinPoint.proceed();
    }
}
