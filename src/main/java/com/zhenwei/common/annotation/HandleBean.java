package com.zhenwei.common.annotation;

import java.lang.annotation.*;

/**
 * mybatis加解蜜处理对象
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface HandleBean {
}
