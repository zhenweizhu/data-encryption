package com.zhenwei.common.annotation;

import java.lang.annotation.*;

/**
 * mybatis加密注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface MybatisEncrypt {
}
