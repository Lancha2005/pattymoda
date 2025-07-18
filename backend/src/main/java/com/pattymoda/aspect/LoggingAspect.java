package com.pattymoda.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.pattymoda.service.*.*(..))")
    public void serviceLayer() {}

    @Pointcut("execution(* com.pattymoda.controller.*.*(..))")
    public void controllerLayer() {}

    @Around("serviceLayer()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.debug("Ejecutando método: {}.{} con argumentos: {}", 
                className, methodName, Arrays.toString(joinPoint.getArgs()));
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            
            logger.debug("Método {}.{} ejecutado exitosamente en {} ms", 
                    className, methodName, (endTime - startTime));
            
            return result;
        } catch (Exception e) {
            logger.error("Error en método {}.{}: {}", className, methodName, e.getMessage());
            throw e;
        }
    }

    @Before("controllerLayer()")
    public void logControllerEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info("Entrada al endpoint: {}.{}", className, methodName);
    }

    @AfterReturning(pointcut = "controllerLayer()", returning = "result")
    public void logControllerExit(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info("Salida exitosa del endpoint: {}.{}", className, methodName);
    }

    @AfterThrowing(pointcut = "controllerLayer()", throwing = "exception")
    public void logControllerException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.error("Excepción en endpoint {}.{}: {}", className, methodName, exception.getMessage());
    }
}