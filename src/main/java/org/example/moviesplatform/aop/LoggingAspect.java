package org.example.moviesplatform.aop; // Sənin paket adın bura uyğun olmalıdır

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // 1. Hansı metodları izləyəcəyimizi təyin edirik (Bütün Service və Controller-lər)
    @Pointcut("within(@org.springframework.stereotype.Service *)"
            + " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    // 2. Metod başlamazdan əvvəl loq yazır
    @Before("springBeanPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("===> AOP BEFORE: {}() başladı. Arqumentlər: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // 3. Metod bitəndən sonra loq yazır
    @After("springBeanPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("<=== AOP AFTER: {}() bitdi.", joinPoint.getSignature().getName());
    }

    // 4. (ƏLAVƏ) Metodun nə qədər vaxt apardığını ölçür (Mükəmməl bir xüsusiyyət)
    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed(); // Metodu icra et
            long executionTime = System.currentTimeMillis() - start;
            log.info("⏱️ AOP PERFORMANCE: {}() icra müddəti: {} ms",
                    joinPoint.getSignature().getName(), executionTime);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Xətalı arqument: {} in {}()",
                    Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }
}