package com.zhenwei.common.annotation;

import java.lang.annotation.*;

/**
 * mybatis解密
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface MybatisDecrypt {
}
