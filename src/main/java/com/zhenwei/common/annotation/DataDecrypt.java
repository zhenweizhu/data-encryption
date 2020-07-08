package com.zhenwei.common.annotation;

import java.lang.annotation.*;

/**
 * 解密注解
 * @Author: zhuzhenwei
 * @Date: 2020/5/27
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataDecrypt {
}
