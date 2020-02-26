package com.fanggu.stone.config;

import com.alibaba.fastjson.JSONObject;
import com.fanggu.stone.response.BasicResponse;
import org.apache.catalina.filters.RequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.fanggu.stone.constant.ResultCode.NOT_PERMITTED;

@Component
public class CustomFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);

    // 获取url上的请求参数
    private static Map<String, String[]> getRequestParamMap(HttpServletRequest request) {
        Map<String, String[]> paramMap = new HashMap<>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            paramMap.put(paramName, paramValues);
        }
        return paramMap;
    }

    // 日志输出拦截器
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");

        String method = httpServletRequest.getMethod();
        if ("GET".equals(method) || "POST".equals(method)) {
            //请求路径
            String path = httpServletRequest.getRequestURI();
            //请求体参数
            Map<String, String[]> paramMap = getRequestParamMap(httpServletRequest);
            // 请求开始时间
            Long startTime = System.currentTimeMillis();
            //Spring通过DispatchServlet处理请求
            HttpSession httpSession = httpServletRequest.getSession();
            String requestUrl = httpServletRequest.getRequestURI();
            // 基于redis的http session并不适合作为会话判断，因为前端每次请求都是一个不同的session，而且移动端不支持cookie
            if (requestUrl.contains("register") || requestUrl.contains("login") || true) {
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                // 除了register接口和login接口之外，所有接口请求时均需要有cookie
                if ("online".equals(httpSession.getAttribute("status"))) {
                    chain.doFilter(httpServletRequest, httpServletResponse);
                } else {
                    httpServletResponse.setContentType("application/json");
                    httpServletResponse.getOutputStream().write(JSONObject.toJSONBytes(new BasicResponse(NOT_PERMITTED, "请先登录后再访问")));
                }
            }
            //请求结束时间
            Long endTime = System.currentTimeMillis();
            String params = JSONObject.toJSONString(paramMap);
            log.info("{} {} {} {} {}ms", method, path, params, httpServletResponse.getStatus(), endTime - startTime);
        } else {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
}
