package com.zhenwei.common.filter;

import com.alibaba.fastjson.JSONObject;
import com.zhenwei.common.listener.CiphertextOperationAnnotationListener;
import com.zhenwei.common.util.DesCiphertextUtil;
import com.zhenwei.common.wrapper.WrapperedRequest;
import com.zhenwei.common.wrapper.WrapperedResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhuzhenwei
 * @Date: 2020/5/26
 * @Version 1.0
 */
@Component
public class ResponseEncryptFilter implements Filter {

    private final static String ENCRYPT_DATA = "encryptData";

    private final static String SYMBOL_AND = "&";

    private final static String SYMBOL_EQUAL = "=";

    @Value("${ciphertext.operation.key:ruoyi20200528code!}")
    private String secretKey;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //加密方法集合
        List<String> encryptAnnotationMethods = CiphertextOperationAnnotationListener.encryptMethods;
        //解密方法集合
        List<String> decryptAnnotationMethods = CiphertextOperationAnnotationListener.decryptMethods;

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUri = httpServletRequest.getRequestURI();

        if (!encryptAnnotationMethods.contains(requestUri) && !decryptAnnotationMethods.contains(requestUri)){
            chain.doFilter(request, response);
            return;
        }

        Map<String, String> origalParamMap = new HashMap<>();
        if (RequestMethod.GET.name().equals(httpServletRequest.getMethod())) {
            Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String paramValue = httpServletRequest.getParameter(paramName);
                origalParamMap.put(paramName, paramValue);
            }
        }
        String requestBody = getRequestBody(httpServletRequest);
        WrapperedRequest wrapRequest = new WrapperedRequest(httpServletRequest,requestBody);
        wrapRequest.setParamMap(origalParamMap);
        //解密
        if (decryptAnnotationMethods.contains(requestUri)) {
            //GET请求
            if (RequestMethod.GET.name().equals(httpServletRequest.getMethod())) {
                String paramValue = httpServletRequest.getParameter(ENCRYPT_DATA);
                String requestBodyMw = DesCiphertextUtil.decryptDES(paramValue,secretKey);
                Map<String, String> paramMap = new HashMap<>();
                String[] paramArr = requestBodyMw.split(SYMBOL_AND);
                if (paramArr.length > 0){
                    for (String str : paramArr){
                        String[] param = str.split(SYMBOL_EQUAL);
                        if (param.length > 1) {
                            paramMap.put(param[0],param[1]);
                        }
                    }
                }
                wrapRequest.setParamMap(paramMap);
            }
            else {
                JSONObject jsonObject = JSONObject.parseObject(requestBody);
                String paramBody = String.valueOf(jsonObject.get(ENCRYPT_DATA));
                //解密请求报文
                String requestBodyMw = DesCiphertextUtil.decryptDES(paramBody.trim(),secretKey);
                wrapRequest = new WrapperedRequest(httpServletRequest, requestBodyMw);
            }
        }

        WrapperedResponse wrapResponse = new WrapperedResponse((HttpServletResponse) response);
        chain.doFilter(wrapRequest, wrapResponse);

        //加密
        byte[] data = wrapResponse.getResponseData();
        String responseBodyMw = new String(data);
        if (encryptAnnotationMethods.contains(requestUri)) {
            // 加密返回报文
            responseBodyMw =DesCiphertextUtil.encryptDES(new String(data),secretKey);
        }
        writeResponse(response, responseBodyMw);
    }

    private void writeResponse(ServletResponse response, String responseString)
            throws IOException {
        PrintWriter out = response.getWriter();
        out.print(responseString);
        out.flush();
        out.close();
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @param req
     * @return
     */
    private String getRequestBody(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return json;
        } catch (IOException e) {
        }
        return "";
    }

}
