package com.zhenwei.common.listener;

import com.zhenwei.common.annotation.CiphertextOperation;
import com.zhenwei.common.annotation.DataDecrypt;
import com.zhenwei.common.annotation.DataEncrypt;
import com.zhenwei.common.util.AopTargetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhuzhenwei
 * @Date: 2020/5/27
 * @Version 1.0
 */
@Component
public class CiphertextOperationAnnotationListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 加密方法
     */
    public static List<String> encryptMethods = new ArrayList<>();

    /**
     * 解密方法
     */
    public static List<String> decryptMethods = new ArrayList<>();

    @Value("${server.servlet.context-path:}")
    private String servletPath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String,Object> beans = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(CiphertextOperation.class);
        for (Object bean : beans.values()) {
            try {
                bean = AopTargetUtils.getTarget(bean);
            } catch (Exception ex){
                //异常处理
            }
            RequestMapping beanRequestMapping = bean.getClass().getAnnotation(RequestMapping.class);
            String beanRequestUrl = "";
            if (null != beanRequestMapping) {
                String[] beanUriValues = beanRequestMapping.value();
                beanRequestUrl = beanUriValues.length > 0 ? beanUriValues[0] : null;
            }
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                String methodUri = returnMethodUriValues(method);
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    buildMethodListData(beanRequestUrl,annotation,methodUri);
                }
            }
        }
    }

    private String returnMethodUriValues(Method method){
        String methodUri = "";
        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if (null != methodRequestMapping) {
            String[] methodUriValues = methodRequestMapping.value();
            methodUri = methodUriValues.length > 0 ? methodUriValues[0] : "";
            return methodUri;
        }
        PostMapping methodPostMapping = method.getAnnotation(PostMapping.class);
        if (null != methodPostMapping){
            String[] methodUriValues = methodPostMapping.value();
            methodUri = methodUriValues.length > 0 ? methodUriValues[0] : "";
            return methodUri;
        }
        GetMapping methodGetMapping = method.getAnnotation(GetMapping.class);
        if (null != methodGetMapping) {
            String[] methodUriValues = methodGetMapping.value();
            methodUri = methodUriValues.length > 0 ? methodUriValues[0] : "";
            return methodUri;
        }
        PutMapping methodPutMapping = method.getAnnotation(PutMapping.class);
        if (null != methodPutMapping) {
            String[] methodUriValues = methodPutMapping.value();
            methodUri = methodUriValues.length > 0 ? methodUriValues[0] : "";
            return methodUri;
        }
        DeleteMapping methodDeleteMapping = method.getAnnotation(DeleteMapping.class);
        if (null != methodDeleteMapping) {
            String[] methodUriValues = methodDeleteMapping.value();
            methodUri = methodUriValues.length > 0 ? methodUriValues[0] : "";
            return methodUri;
        }
        return methodUri;
    }

    private List<String> returnMethodList(Annotation annotation){
        if (DataEncrypt.class.equals(annotation.annotationType())){
            return encryptMethods;
        }
        if (DataDecrypt.class.equals(annotation.annotationType())) {
            return decryptMethods;
        }
        return null;
    }

    private void buildMethodListData(String beanUriValue,Annotation annotation,String methodUri){
        List<String> methodList = returnMethodList(annotation);
        if (null == methodList){
            return;
        }
        String url = servletPath + beanUriValue + methodUri;
        if (StringUtils.isNotBlank(url)) {
            methodList.add(url);
        }
    }
}
