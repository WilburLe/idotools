package com.toolbox.framework.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.StringUtility;

@Component
@Aspect
public class ApiControllerAspect {
    private final static Log log = LogFactory.getLog(ApiControllerAspect.class);
    @Value("${log.view.time}")
    private String           log_view_time;

    //execution(* cpm.pixshow.*Service*.*(..))
    @Pointcut("execution(* *..*Controller.*(..))")
    public void pointCut() {
    }

    @After("pointCut()")
    public void after(JoinPoint joinPoint) {
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
    }

    @AfterReturning(pointcut = "pointCut()", returning = "returnVal")
    public void afterReturning(JoinPoint joinPoint, Object returnVal) {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) {
        long start = System.currentTimeMillis();
        String className = getRequestUrl();
        Object obj = null;
        boolean error = false;
        try {
            //继续执行
            obj = pjp.proceed();
        } catch (Throwable ex) {
            //将错误输出到控制台
            ex.printStackTrace();
            error = true;
        }
        long end = System.currentTimeMillis();
        long useTime = end - start;
        if (error) {
            JSONObject rs = new JSONObject();
            rs.put("status", -999);
            rs.put("data", "error");
            obj = rs;
            log.error((end - start) + "ms " + className);
        } else {
            if (StringUtility.isEmpty(log_view_time) || useTime > Long.parseLong(log_view_time)) {
                log.info((end - start) + "ms " + className);
            }
        }
        return obj;

    }

    //这一步将永远都不会走，因为上面不会抛出错误异常
    @AfterThrowing(pointcut = "pointCut()", throwing = "error")
    public void afterThrowing(JoinPoint point, Throwable error) {
        String className = getRequestUrl();
        log.error(className + " :" + error);
    }

    /**
     * 获取Request Url
     * @param joinPoint
     * @return
     * @throws Exception
     *
     */
    public static String getRequestUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuffer url = request.getRequestURL();
        return url.toString();
    }

    public static String getRequestClassName(JoinPoint joinPoint) {
        String targetName = joinPoint.getTarget().getClass().getName(); //类名
        String methodName = joinPoint.getSignature().getName(); //方法名
        Object[] arguments = joinPoint.getArgs(); //请求的参数
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method[] methods = targetClass.getMethods();
        StringBuilder result = new StringBuilder();

        for (Method method : methods) {
            //方法名一致
            if (!method.getName().equals(methodName)) {
                continue;
            }
            //携带参数数量一致
            Class[] clazzs = method.getParameterTypes();
            if (clazzs.length != arguments.length) {
                continue;
            }
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> handlerType = annotation.annotationType();
                String handlerName = handlerType.getName();
                if (handlerName.lastIndexOf("RequestMapping") > 0) {
                    Map<String, Object> map = AnnotationUtils.getAnnotationAttributes(annotation);
                    result.append("[" + targetName.split("\\.")[targetName.split("\\.").length - 1] + "]").append("[" + methodName + "]");
                    result.append("[" + StringUtils.arrayToDelimitedString((Object[]) map.get("value"), ",") + "]");
                    break;
                }
            }
        }
        return result.toString();
    }
}