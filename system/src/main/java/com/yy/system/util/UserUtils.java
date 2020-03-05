package com.yy.system.util;

import com.yy.common.cache.utils.EhCacheUtil;
import com.yy.common.constant.ResponseCode;
import com.yy.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * @author YY
 * @date 2019/9/9
 * @description 获取当前登录的用户信息
 */
@Component
@Slf4j
public class UserUtils {

    /*@Autowired
    IUserService userService;

    @Autowired
    IPrivilegeService privilegeService;*/

    public  UserInfo getCurrentUserInfo(){
        Subject subject = SecurityUtils.getSubject();
        String username= (String) subject.getPrincipal();
        if (StringUtils.isBlank(username)){
            throw new BizException(ResponseCode.SHIRO_UNAUTHENTICATEDEXCEPTION_EXCEPTION.getCode(),
                    ResponseCode.SHIRO_UNAUTHENTICATEDEXCEPTION_EXCEPTION.getMsg());
        }
        log.debug("UserUtils getCurrentUserInfo 尝试拿取缓存中的内容key为{}......","CurrentUserInfo:username"+username);
        UserInfo currentUserInfo = EhCacheUtil.get("CurrentUserInfo:username"+username, UserInfo.class);
        if (currentUserInfo == null){
            log.debug("UserUtils getCurrentUserInfo 缓存中内容为空......");
           /* UserVo userAndRolesByUserName = userService.getUserAndRolesByUserName(username);
            if (userAndRolesByUserName!=null){
                userAndRolesByUserName.setPassword(null);
            }
            UserInfo userInfo = new UserInfo();
            Long id = userAndRolesByUserName.getId();
            List<PrivilegeBo> privilegeBos = privilegeService.allPrivilegeTreeByUserId(id);
            List<SystemPrivilege> privilegesNotTree = userService.findAllPrivilegeByUserId(id);
            userInfo.setPrivilegeBos(privilegeBos);
            userInfo.setUserAndRoles(userAndRolesByUserName);
            userInfo.setPrivilegesNotTree(privilegesNotTree);
            log.debug("UserUtils getCurrentUserInfo 数据库中查询数据放入缓存中key为{}......","CurrentUserInfo:username"+username);
            EhCacheUtil.put("CurrentUserInfo:username"+username,userInfo);*/
            return null;
        }
        log.debug("UserUtils getCurrentUserInfo 返回拿到的缓存数据......");
        return currentUserInfo;
    }
}
