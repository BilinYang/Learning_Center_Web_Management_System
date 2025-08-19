package com.bilin.aop;

import com.bilin.entity.OperateLog;
import com.bilin.mapper.OperateLogMapper;
import com.bilin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(com.bilin.annotation.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // Create instance of OperateLog class
        OperateLog operateLog = new OperateLog();

        // Get token, claims, empId from request
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJwt(token);
        Object empId = claims.get("empId");
        operateLog.setOperateEmpId((Integer) empId);

        // Set parameters of operateLog object
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));

        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        operateLog.setReturnValue(result.toString());

        long end = System.currentTimeMillis();
        operateLog.setCostTime(end - begin);

        operateLogMapper.insert(operateLog);

        return result;
    }
}
