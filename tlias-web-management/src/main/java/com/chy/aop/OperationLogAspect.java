package com.chy.aop;

import com.chy.mapper.OperateLogMapper;
import com.chy.pojo.OperateLog;
import com.chy.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    private static final String[] SENSITIVE_FIELDS = {"password", "pwd", "secret", "token"};

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.chy.anno.Log)")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        OperateLog olog = new OperateLog();
        olog.setOperateEmpId(CurrentHolder.getCurrentId());
        olog.setOperateTime(LocalDateTime.now());
        olog.setClassName(joinPoint.getTarget().getClass().getName());
        olog.setMethodName(joinPoint.getSignature().getName());
        olog.setMethodParams(sanitizeArgs(joinPoint.getArgs()));
        olog.setReturnValue(result != null ? result.toString() : "void");
        olog.setCostTime(costTime);

        log.info("记录操作日志: {}", olog);
        operateLogMapper.insert(olog);

        return result;
    }

    private String sanitizeArgs(Object[] args) {
        if (args == null) return "[]";
        return Arrays.toString(Arrays.stream(args)
                .map(this::sanitizeObject)
                .toArray());
    }

    private Object sanitizeObject(Object arg) {
        if (arg == null) return null;
        String str = arg.toString().toLowerCase();
        for (String field : SENSITIVE_FIELDS) {
            if (str.contains(field)) {
                return "***";
            }
        }
        return arg;
    }
}