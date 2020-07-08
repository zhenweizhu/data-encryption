package com.zhenwei.common.wrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhuzhenwei
 * @Date: 2020/5/26
 * @Version 1.0
 */
public class WrapperedRequest extends HttpServletRequestWrapper {
    /**
     * 请求报文
     */
    private String requestBody = null;
    private Map<String, String> paramMap = new HashMap<>();

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    HttpServletRequest req = null;

    public WrapperedRequest(HttpServletRequest request) {
        super(request);
        this.req = request;
    }

    public WrapperedRequest(HttpServletRequest request, String requestBody) {
        super(request);
        this.requestBody = requestBody;
        this.req = request;
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            private InputStream in = new ByteArrayInputStream(
                    requestBody.getBytes(req.getCharacterEncoding()));

            @Override
            public int read() throws IOException {
                return in.read();
            }
        };
    }

    @Override
    public String[] getParameterValues(String name) {
        if (paramMap.containsKey(name)) {
            return new String[] { getParameter(name) };
        }
        return super.getParameterValues(name);
    }

    @Override
    public String getParameter(String name) {
        return this.paramMap.get(name);
    }
}
