package com.yy.system.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.yy.common.bean.Result;
import com.yy.common.constant.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * @author YY
 * @date 2019/9/2
 * @description
 */
@Slf4j
public class CORSAuthenticationFilter extends FormAuthenticationFilter {

    public CORSAuthenticationFilter() {
       // super();
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(request instanceof HttpServletRequest){
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")){
                System.out.println("OPTIONS请求");
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setStatus(HttpServletResponse.SC_OK);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");

        Subject subject = getSubject(request, response);
        String username = (String) subject.getPrincipal();
        String authorization = ((HttpServletRequest) request).getHeader("Authorization");
        PrintWriter out = res.getWriter();
        if (StringUtils.isBlank(username)){
            if (StringUtils.isBlank(authorization)){
                if (((HttpServletRequest) request).getCookies()!=null){
                    for (Cookie cookie : ((HttpServletRequest) request).getCookies()) {
                        if ("TY-AI-Admin-Token".equals(cookie.getName())){
                            authorization = cookie.getValue();
                       /* cookie.setMaxAge(60*120);
                        ((HttpServletResponse) response).addCookie(cookie);*/
                        }
                    }
                }else {
                    log.debug("未获取到Cookies");
                }
            }
            if (StringUtils.isNotBlank(authorization)){
                //out.write(JSONObject.toJSON(new Result(ResponseCode.SHIRO_TOKEN_EXCEPTION.getCode(),ResponseCode.SHIRO_TOKEN_EXCEPTION.getMsg())).toString());
                out.print(JSONObject.toJSONString(new Result(ResponseCode.SHIRO_TOKEN_EXCEPTION.getCode(),ResponseCode.SHIRO_TOKEN_EXCEPTION.getMsg())));
                out.close();
                return false;
            }
            // 抛出未登录信息
            out.print(JSONObject.toJSONString(new Result(ResponseCode.SHIRO_UNAUTHENTICATEDEXCEPTION_EXCEPTION.getCode(),ResponseCode.SHIRO_UNAUTHENTICATEDEXCEPTION_EXCEPTION.getMsg())));
            out.close();
            return false;
        }
        Serializable id = subject.getSession().getId();
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(authorization) && !authorization.equals(id)){
            out.print(JSONObject.toJSONString(new Result(ResponseCode.SHIRO_TOKEN_EXCEPTION.getCode(),ResponseCode.SHIRO_TOKEN_EXCEPTION.getMsg())));
            out.close();
            return false;
        }
        return true;
    }

}
