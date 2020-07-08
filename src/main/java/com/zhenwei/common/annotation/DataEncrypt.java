package com.zhenwei.common.annotation;

import java.lang.annotation.*;

/**
 * 加密注解
 * @Author: zhuzhenwei
 * @Date: 2020/5/26
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataEncrypt {
}
