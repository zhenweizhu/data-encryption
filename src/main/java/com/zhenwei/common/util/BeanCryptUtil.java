package com.zhenwei.common.util;

import com.zhenwei.common.annotation.HandleBean;
import com.zhenwei.common.annotation.MybatisDecrypt;
import com.zhenwei.common.annotation.MybatisEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;

/**
 * 对象加解密
 */
public class BeanCryptUtil {

    /**
     * 对象字段加密
     * @param t
     * @param <T>
     */
    public static <T> void encryptField(T t,String secretKey){
        HandleBean handleBean = AnnotationUtils.findAnnotation(t.getClass(), HandleBean.class);
        if (null == handleBean) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MybatisEncrypt.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String value = (String)field.get(t);
                        if (StringUtils.isNotBlank(value)) {
                            field.set(t,DesCiphertextUtil.encryptDES(value,secretKey));
                        }
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("对象字段加密失败,原因:" + ex.getMessage());
            }
        }
    }

    /**
     * 对象字段解密
     * @param t
     * @param secretKey
     * @param <T>
     */
    public static <T> void decryptField(T t,String secretKey){
        HandleBean handleBean = AnnotationUtils.findAnnotation(t.getClass(), HandleBean.class);
        if (null == handleBean) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MybatisDecrypt.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String value = (String)field.get(t);
                        if (StringUtils.isNotBlank(value)) {
                            field.set(t,DesCiphertextUtil.decryptDES(value,secretKey));
                        }
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("对象字段解密失败,原因:" + ex.getMessage());
            }
        }
    }
}
