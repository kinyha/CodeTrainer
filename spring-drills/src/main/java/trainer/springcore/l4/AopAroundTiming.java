package trainer.springcore.l4;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;
import java.util.function.LongConsumer;

// @task springcore.l4.AopAroundTiming
// @tags spring-core,aop,around-advice,aspect,timing
// @time 35m
// @src  new
@Aspect
@Component
public final class AopAroundTiming {

    private final LongConsumer onElapsedNanos;

    public AopAroundTiming(LongConsumer onElapsedNanos) {
        this.onElapsedNanos = Objects.requireNonNull(onElapsedNanos, "onElapsedNanos");
    }

    @Pointcut("@annotation(trainer.springcore.l4.Timed)")
    public void timedMethods() {
    }

    /**
     * @Around оборачивает вызов ЦЕЛИКОМ: proceed() — единственный способ реально выполнить
     * оригинальный метод. Забыть его вызвать — значит молча "проглотить" вызов.
     */
    @Around("timedMethods()")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        // ---8<--- solution
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            onElapsedNanos.accept(System.nanoTime() - start);
        }
        // --->8--- solution
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Timed {
}
