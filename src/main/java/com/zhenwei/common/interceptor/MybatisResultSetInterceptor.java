package com.zhenwei.common.interceptor;

import com.zhenwei.common.util.BeanCryptUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)})
@Component
@ConditionalOnProperty(value = "mybatis.bean.decrypt", havingValue = "true")
public class MybatisResultSetInterceptor implements Interceptor {

    @Value("${ciphertext.operation.key:qwerty@1234!}")
    private String secretKey;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (Objects.isNull(result)) {
            return null;
        }
        if (result instanceof ArrayList) {
            ArrayList resultList = (ArrayList) result;
            if (!CollectionUtils.isEmpty(resultList)) {
                for (Object object : resultList) {
                    BeanCryptUtil.decryptField(object,secretKey);
                }
            }
        } else {
            BeanCryptUtil.decryptField(result,secretKey);
        }
        return result;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
