package com.debug.pmp.server.aspect;

import com.alibaba.fastjson.JSON;
import com.debug.pmp.common.utils.HttpContextUtils;
import com.debug.pmp.common.utils.IPUtil;
import com.debug.pmp.model.entity.SysLog;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.SysLogService;
import com.debug.pmp.server.shiro.ShiroUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.debug.pmp.server.annotation.LogAnnotation)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object object = point.proceed();
        long time = System.currentTimeMillis() - start;

        saveLog(point, time);
        return object;
    }

    private void saveLog(ProceedingJoinPoint point, Long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();

        SysLog sysLog = new SysLog();

        sysLog.setTime(time);

        sysLog.setIp(IPUtil.getIpAddr(HttpContextUtils.getHttpServletRequest()));

        sysLog.setOperation(signature.getMethod().getAnnotation(LogAnnotation.class).value());

        Object[] args = point.getArgs();

        String params = JSON.toJSON(args[0]).toString();
        sysLog.setParams(params);

        sysLog.setUsername(ShiroUtil.getUser().getUsername());

        String className = point.getTarget().getClass().getName();
        String methodName = signature.getMethod().getName();
        sysLog.setMethod(className + "." + methodName);

        sysLogService.save(sysLog);
    }

}
