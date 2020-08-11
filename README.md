### 前后端数据传输加密和解密
通过添加注解，实现请求参数加密
在需要加解密的controller层添加@CiphertextOperation注解，然后在方法上面，添加@DataDecrypt注解进行解密，添加@DataEncrypt注解进行加密。
前端也有配套的配置支持。
### mybatis实现字段加密解密
通过添加注解，实现mybatis与mysql字段映射参数加密解密
在需要加解密的bean对象添加@HandleBean注解，然后在字段上面，添加@MybatisDecrypt注解进行解密，添加@MybatisEncrypt注解进行加密。
