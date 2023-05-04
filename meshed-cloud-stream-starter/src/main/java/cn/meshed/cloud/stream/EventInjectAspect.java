package cn.meshed.cloud.stream;

import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.dto.SecurityEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */

@Aspect
@Slf4j
public class EventInjectAspect {

    @Pointcut("@annotation(cn.meshed.cloud.stream.EventInject)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void fill(JoinPoint joinPoint) throws Throwable {
        if (joinPoint.getArgs().length <= 0){
            return;
        }
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof SecurityEvent){
                SecurityEvent securityEvent = (SecurityEvent) arg;
                securityEvent.setOperator(SecurityContext.getOperator());
            }
        }
    }
}
