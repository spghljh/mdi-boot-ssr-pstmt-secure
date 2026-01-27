package kr.co.mdi.common.aop;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.logstash.logback.argument.StructuredArguments;

@Aspect
@Component
public class ExecutionLogger {

    // 콘솔 및 텍스트 로그용 로거
    private static final Logger consoleLogger = LoggerFactory.getLogger(ExecutionLogger.class);

    // JSON 로그 전용 로거 (logback 설정에서 JSON_FILE에만 연결)
    private static final Logger jsonLogger = LoggerFactory.getLogger("jsonLogger");

    @Pointcut("execution(* kr.co.mdi..*(..)) && !execution(* kr.co.mdi.common.config.TraceIdFilter.*(..))")
    public void applicationPackagePointcut() {}

    @Before("applicationPackagePointcut()")
    public void logMethodEntry(JoinPoint joinPoint) {
        logStructured(joinPoint, "enter");
    }

    @After("applicationPackagePointcut()")
    public void logMethodExit(JoinPoint joinPoint) {
        logStructured(joinPoint, "exit");
    }

    private void logStructured(JoinPoint joinPoint, String direction) {
        String fullClassName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String layer = detectLayer(fullClassName);
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        String threadName = Thread.currentThread().getName();
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String level = "INFO";
        String loggerName = ExecutionLogger.class.getName();

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("timestamp", timestamp);
        logMap.put("level", level);
        logMap.put("logger", loggerName);
        logMap.put("thread", threadName);
        logMap.put("direction", direction);
        logMap.put("layer", layer);
        logMap.put("class", simpleClassName);
        logMap.put("method", methodName);

        // 콘솔용: key=value 형식으로 변환
        StringBuilder kvLog = new StringBuilder();
        logMap.forEach((k, v) -> kvLog.append(k).append("=").append(v).append(" "));

        // 콘솔 및 텍스트 로그 출력
        consoleLogger.info(kvLog.toString().trim());

        // JSON 로그 전용 출력 (StructuredArguments로 필드 분리)
        jsonLogger.info("structured log", StructuredArguments.entries(logMap));
    }

    private String detectLayer(String className) {
        if (className.contains("Controller")) return "Controller";
        if (className.contains("Service")) return "Service";
        if (className.contains("Repository") || className.contains("Dao")) return "DAO";
        return "App";
    }
}
