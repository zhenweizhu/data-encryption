package com.zhenwei.common.interceptor;

import com.zhenwei.common.util.BeanCryptUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.sql.PreparedStatement;
import java.util.Properties;
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)})
@Component
@ConditionalOnProperty(value = "mybatis.bean.encrypt", havingValue = "true")
public class MybatisParameterInterceptor implements Interceptor {

    @Value("${ciphertext.operation.key:qwerty@1234!}")
    private String secretKey;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof ParameterHandler) {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();

            //获取参数对象
            Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
            parameterField.setAccessible(true);
            Object parameterObject = parameterField.get(parameterHandler);

            if (null != parameterObject) {
                BeanCryptUtil.encryptField(parameterObject,secretKey);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
