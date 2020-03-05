package com.yy.system.config.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author YY
 * @date 2019/9/2
 * @description

/*@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    /*@Autowired
    private IUserService userService;

    @Autowired
    UserUtils userUtils;

    /**
     * 授权处理
     * @param principalCollection
     * @return
     *//*
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
       // String username = (String)principalCollection.getPrimaryPrincipal();
        UserInfo currentUserInfo = userUtils.getCurrentUserInfo();
        List<SystemRole> roles = currentUserInfo.getUserAndRoles().getRoles();
        List<SystemPrivilege> privilegesNotTree = currentUserInfo.getPrivilegesNotTree();
        // SystemUser byUsername = userService.findByUsername(username);
       // List<SystemRole> allRoleByUserId = userService.findAllRoleByUserId(byUsername.getId());
        Set<String> roleNameSet = roles.stream().map(SystemRole::getName).collect(Collectors.toSet());
       // List<SystemPrivilege> allPrivilegeByUserId = userService.findAllPrivilegeByUserId(byUsername.getId());
        Set<String> privilegeNameSet = privilegesNotTree.stream().map(SystemPrivilege::getUrl).collect(Collectors.toSet());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleNameSet);
        simpleAuthorizationInfo.addStringPermissions(privilegeNameSet);
        return simpleAuthorizationInfo;
    }

    *//**
     * 认证处理
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     *//*
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        SystemUser byUsername = userService.findByUsername(username);
        if (byUsername == null){
            log.debug("用户不存在{}",username);
            throw new BizException("用户不存在");
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, byUsername.getPassword(),
                ByteSource.Util.bytes(username), this.getName());
        return simpleAuthenticationInfo;
    }
}*/
