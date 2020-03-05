package com.yy.system.config.shiro;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


/**
 * @author YY
 * @date 2019/9/2
 * @description
 */
@Slf4j
public class SessionManager extends DefaultWebSessionManager {
    public SessionManager() {
        super();
        setGlobalSessionTimeout(DEFAULT_GLOBAL_SESSION_TIMEOUT * 48);
        //setGlobalSessionTimeout(1000 * 60 * 120);
        setSessionIdUrlRewritingEnabled(false);
        setDeleteInvalidSessions(true);
        setSessionValidationSchedulerEnabled(true);
        setSessionIdCookieEnabled(true);
        setSessionValidationInterval(DEFAULT_GLOBAL_SESSION_TIMEOUT * 48);
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String token = httpServletRequest.getHeader("Authorization");
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
                if (((HttpServletRequest) request).getCookies() != null) {
                    for (Cookie cookie : ((HttpServletRequest) request).getCookies()) {
                        if ("TY-AI-Admin-Token".equals(cookie.getName())) {
                            token = cookie.getValue();
                        }
                    }
                } else {
                    log.debug("未获取到Cookies");
                }
            }
            log.debug("Authorization：{}", token);
            if (!StringUtils.isEmpty(token)) {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "Stateless request");
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
                return token;
            } else {
                return super.getSessionId(request, response);
            }
        }
    }
