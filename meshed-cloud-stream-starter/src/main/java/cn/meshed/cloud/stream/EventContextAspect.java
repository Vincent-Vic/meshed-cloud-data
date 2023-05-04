package cn.meshed.cloud.stream;

import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.dto.SecurityEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
public class EventContextAspect {

    @Pointcut("this(cn.meshed.cloud.cqrs.EventExecute)")
    public void pointcut() {
    }
    @Pointcut("@annotation(cn.meshed.cloud.stream.EventContext)")
    public void annotationPointcut() {
    }

    @Before("pointcut()||annotationPointcut()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null){
            for (Object arg : args) {
                if (arg instanceof SecurityEvent){
                    handleOperator(arg);
                    return;
                }
            }
        }
        log.warn("{} 不存在参数或非基础SecurityEvent对象",joinPoint.getTarget().getClass().getName());
    }

    /**
     * 操作人注入
     *
     * @param arg 参数
     */
    private static void handleOperator(Object arg) {
        SecurityEvent securityEvent = (SecurityEvent)arg;
        Operator operator = securityEvent.getOperator();
        if (operator != null){
            SecurityContext.setOperator(operator);
            log.debug("{}",operator);
        } else {
            log.warn("{} 事件缺失操作人上下文", arg.getClass().getName());
        }
    }
}
