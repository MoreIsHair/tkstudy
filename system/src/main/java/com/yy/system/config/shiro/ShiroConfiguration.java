package com.yy.system.config.shiro;

/**
 * @author YY
 * @date 2019/9/2
 * @description
 */
/*@Configuration
@Component
public class ShiroConfiguration {

    @Bean("myShiroRealm")
    public ShiroRealm shiroDatabaseRealm(){
        ShiroRealm shiroRealm = new ShiroRealm();
        // 密码加密加盐
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        return shiroRealm;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map filterChainDefinitionMap = new LinkedHashMap();
        //filterChainDefinitionMap.put("/user/logout", "logout");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/security", "anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/ui", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/citycode.js", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/file/app/**", "anon");
        //filterChainDefinitionMap.put("/formTemplate/importExcel", "anon");
        filterChainDefinitionMap.put("/**", "corsAuthenticationFilter");
        Map<String, Filter> filterMap = new LinkedHashMap<>();

        filterMap.put("corsAuthenticationFilter", new CORSAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    @Bean("mySessionManager")
    public SessionManager sessionManager(@Qualifier("mySimpleCookie")SimpleCookie simpleCookie){
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
    }

    *//**
     * 创建DefaultWebSecurityManager
     *//*
    @Bean(name="defaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("myShiroRealm") ShiroRealm userRealm,@Qualifier("mySessionManager") SessionManager sessionManager){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setSessionManager(sessionManager);
        return defaultWebSecurityManager;
    }

    *//**
     *   指定本系统SESSIONID, 默认为: JSESSIONID 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT 等默认JSESSIONID,
     *   当跳出SHIRO SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失!
     * @return
     *//*
    @Bean("mySimpleCookie")
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("tydesin.session.id");
        simpleCookie.setMaxAge(-1);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }
}*/
