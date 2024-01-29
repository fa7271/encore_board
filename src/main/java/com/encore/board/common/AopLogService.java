package com.encore.board.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
@Slf4j
// log 처리 AOP 사용
public class AopLogService {

//     위치 저장 문법, 컨트롤러 안에 있는 모든 메소드들
//    @Pointcut("execution(* com.encore.board..controller..*.*(..))")

    //    어노테이션 방식
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointcut() {

    }

//    방식 1. before, after 사용

//    @Before("controllerPointcut()")
//    public void beforService(JoinPoint joinPoint) {
//        log.info("Before Controller");
////        메소드가 실행되기 전에 인증, 입력값 검증등을 수행하는 용도로 사용하는 사전단계
//
//
////        사용자의 요청값을  출력하기 위해 HttpServletRequest 객체를 꺼내는 로직
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest req = servletRequestAttributes.getRequest();
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode objectNode = new ObjectMapper().createObjectNode();
//        objectNode.put("Method Name ", joinPoint.getSignature().getName());
//        objectNode.put("CRUD Name", req.getMethod());
//
//        Map<String, String[]> paramMap = req.getParameterMap();
//
//        ObjectNode objectNodeDetail = new ObjectMapper().valueToTree(paramMap);
//        objectNode.set("user inputs", objectNodeDetail);
//
//        log.info("user request info " + objectNode);
//    }
//
//
//    @After("controllerPointcut()")
//    public void afterController(JoinPoint joinPoint) {
//        log.info("afterController");
//    }
//}




//    방식 2. @Around 사용 (가장 빈번하게 사용)
    @Around("controllerPointcut()") // join point 란 Aop 대상으로 하는 컨트롤러의 특정 메서드를 의미
    public Object controllerLogeer(ProceedingJoinPoint proceedingJoinPoint) {
//        log.info("request method " + proceedingJoinPoint.getSignature().toString());

//        사용자의 요청값을  출력하기 위해 HttpServletRequest 객체를 꺼내는 로직
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = servletRequestAttributes.getRequest();
//        json 형태로 사용자의 요청을 조립하기 위한 로직
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("Method Name ", proceedingJoinPoint.getSignature().getName());
        objectNode.put("CRUD Name", req.getMethod());

        Map<String, String[]> paramMap = req.getParameterMap();

        ObjectNode objectNodeDetail = new ObjectMapper().valueToTree(paramMap);
        objectNode.set("user inputs", objectNodeDetail);

        log.info("user request info " + objectNode);

        try {
//            본래의 컨트롤러 메서드 호출하는 부분
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }finally {
            log.info("end controller");
        }
    }
}
