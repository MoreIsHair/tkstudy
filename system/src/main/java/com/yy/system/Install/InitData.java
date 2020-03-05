package com.yy.system.Install;


import com.yy.system.config.shiro.ShiroUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YY
 * @date 2019/10/17
 * @description 容器启动时初始化数据（菜单，角色，用户）需要数据库中创建响应的表，以及导入菜单数据
 */
@Component
@Slf4j
public class InitData implements ApplicationListener<ContextRefreshedEvent> {
/*
    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IPrivilegeService privilegeService;

    @Autowired
    UserMapper userMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
       log.debug("初始化容器后正在调用方法尝试初始化数据......");
        // 用来判断非springboot项目中会存在两个容器（spring，springMVC）
       // event.getApplicationContext().getParent() ==null
        SystemUser admin = userService.findByUsername("admin");
        log.debug("查找用户[admin]完成......");
        if (admin==null){
            log.debug("系统中不存在初始值用户[admin]......");
            List<SystemUser> systemUsers = userService.find();
            if (systemUsers==null || systemUsers.size()==0){
                log.debug("系统中无有效用户");
                log.debug("执行初始化数据操作.....");
                log.debug("即将创建用户[admin/admin0920],角色[admin]......");
                // 用户
                UserBo bo = new UserBo();
                long userId = IDWorker.getId();
                bo.setEnabled(true);
                bo.setUsername("admin");
                bo.setCreateTime(new Date());
                bo.setTelephone("020-85549595");
                bo.setPassword(ShiroUtil.md5("admin0920","admin"));
                bo.setName("系统初始用户");
                bo.setSex("2");
                bo.setId(userId);
                userService.add(bo);
                // 角色
                RoleBo bo1 = new RoleBo();
                long roleId = IDWorker.getId();
                bo1.setId(roleId);
                bo1.setCreateTime(new Date());
                bo1.setName("系统初始角色");
                bo1.setDescription("系统初始角色......");
                roleService.add(bo1);
                // 建立用户和角色的关系
                userMapper.insertUserAndRole(userId,roleId);
                log.debug("角色,用户初始化完成......");
                // 菜单
                PrivilegeBo bo2 = new PrivilegeBo();
                log.debug("获取系统中所有的菜单......");
                List<PrivilegeBo> select = privilegeService.select(bo2);
                if (select.size()!=0){
                    log.debug("建立角色与菜单的关系......");
                    List<Long> collect = select.stream().map(PrivilegeBo::getId).collect(Collectors.toList());
                    RoleAndPrivilegesParam param = new RoleAndPrivilegesParam();
                    param.setPrivilegeIds(collect);
                    param.setRoleId(roleId);
                    privilegeService.givePrivilegeToRole(param);
                    log.debug("菜单,角色初始化完成......");
                }else {
                    log.error("系统中不存在菜单......");
                }
               *//*ArrayList<Long> privilegeIds = new ArrayList<>();
                PrivilegeBo bo2 = new PrivilegeBo();
                long p1 = IDWorker.getId();
                privilegeIds.add(p1);
                bo2.setId(p1);
                privilegeService.add(bo2);*//*
                // 建立角色与菜单的关系
                log.debug("菜单,角色，用户初始化工作结束......");
            }
        }
    }*/
}
