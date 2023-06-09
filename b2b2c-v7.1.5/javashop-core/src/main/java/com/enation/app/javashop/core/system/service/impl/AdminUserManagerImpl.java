package com.enation.app.javashop.core.system.service.impl;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.framework.util.*;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.core.system.model.dos.RoleDO;
import com.enation.app.javashop.core.system.model.dto.AdminUserDTO;
import com.enation.app.javashop.core.system.model.vo.AdminLoginVO;
import com.enation.app.javashop.core.system.model.vo.AdminUserVO;
import com.enation.app.javashop.core.system.service.RoleManager;
import com.enation.app.javashop.framework.JavashopConfig;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.ThreadContextHolder;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Admin;
import com.enation.app.javashop.framework.security.model.JWTConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.system.model.dos.AdminUser;
import com.enation.app.javashop.core.system.service.AdminUserManager;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 平台管理员业务类
 *
 * @author zh
 * @version v7.0
 * @since v7.0.0
 * 2018-06-20 20:38:26
 */
@Service
public class AdminUserManagerImpl implements AdminUserManager {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private JavashopConfig javashopConfig;
    @Autowired
    private Cache cache;

    @Override
    public Page list(int page, int pageSize, String keyword) {
        StringBuffer sqlBuffer = new StringBuffer("select u.*,r.role_name from es_admin_user u left join es_role r ON u.role_id=r.role_id where u.user_state=0 ");
        List<Object> term = new ArrayList<>();

        //按关键字查询
        if (StringUtil.notEmpty(keyword)) {
            sqlBuffer.append(" and (username like ? or department like ? or real_name like ?) ");
            term.add("%" + keyword + "%");
            term.add("%" + keyword + "%");
            term.add("%" + keyword + "%");
        }

        sqlBuffer.append(" order by date_line desc");

        Page webPage = this.systemDaoSupport.queryForPage(sqlBuffer.toString(), page, pageSize, AdminUserDTO.class, term.toArray());
        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AdminUser add(AdminUserVO adminUserVO) {
        boolean bool = Pattern.matches("[a-fA-F0-9]{32}", adminUserVO.getPassword());
        if (!bool) {
            throw new ServiceException(SystemErrorCode.E917.code(), "密码格式不正确");
        }
        //校验用户名称是否重复
        AdminUser user = this.systemDaoSupport.queryForObject("select * from es_admin_user where username = ? and user_state = 0", AdminUser.class, adminUserVO.getUsername());
        if (user != null) {
            throw new ServiceException(SystemErrorCode.E915.code(), "管理员名称重复");
        }
        //不是超级管理员的情况下再校验权限是否存在
        if (!adminUserVO.getFounder().equals(1)) {
            RoleDO roleDO = roleManager.getModel(adminUserVO.getRoleId());
            if (roleDO == null) {
                throw new ResourceNotFoundException("所属权限不存在");
            }
        }
        String password = adminUserVO.getPassword();
        AdminUser adminUser = new AdminUser();
        BeanUtil.copyProperties(adminUserVO, adminUser);
        adminUser.setPassword(StringUtil.md5(password + adminUser.getUsername().toLowerCase()));
        adminUser.setDateLine(DateUtil.getDateline());
        adminUser.setUserState(0);
        this.systemDaoSupport.insert(adminUser);
        adminUser.setId(systemDaoSupport.getLastId("es_admin_user"));
        return adminUser;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AdminUser edit(AdminUserVO adminUserVO, Integer id) {
        //对要修改的管理员是否存在进行校验
        AdminUser adminUser = this.getModel(id);
        if (adminUser == null) {
            throw new ResourceNotFoundException("当前管理员不存在");
        }
        //如果修改的是从超级管理员到普通管理员 需要校验此管理员是否是最后一个超级管理员
        if (adminUser.getFounder().equals(1) && !adminUserVO.getFounder().equals(1)) {
            String sql = "select * from es_admin_user where founder = 1 and user_state = 0";
            List<AdminUser> adminUsers = this.systemDaoSupport.queryForList(sql, AdminUser.class);
            if (adminUsers.size() <= 1) {
                throw new ServiceException(SystemErrorCode.E916.code(), "必须保留一个超级管理员");
            }
        }
        if (!adminUserVO.getFounder().equals(1)) {
            RoleDO roleDO = roleManager.getModel(adminUserVO.getRoleId());
            if (roleDO == null) {
                throw new ResourceNotFoundException("所属权限不存在");
            }
        } else {
            adminUserVO.setRoleId(0);
        }
        //管理员原密码
        String password = adminUser.getPassword();
        //对管理员是否修改密码进行校验
        if (!StringUtil.isEmpty(adminUserVO.getPassword())) {
            boolean bool = Pattern.matches("[a-fA-F0-9]{32}", adminUserVO.getPassword());
            if (!bool) {
                throw new ServiceException(SystemErrorCode.E917.code(), "密码格式不正确");
            }
            adminUserVO.setPassword(StringUtil.md5(adminUserVO.getPassword() + adminUser.getUsername().toLowerCase()));
        } else {
            adminUserVO.setPassword(password);
        }
        adminUserVO.setUsername(adminUser.getUsername());
        BeanUtil.copyProperties(adminUserVO, adminUser);
        this.systemDaoSupport.update(adminUser, id);
        return adminUser;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        //校验当前管理员是否存在
        AdminUser adminUser = this.getModel(id);
        if (adminUser == null) {
            throw new ResourceNotFoundException("当前管理员不存在");
        }
        //校验要删除的管理员是否是最后一个超级管理员
        String sql = "select * from es_admin_user where founder = 1 and user_state = 0";
        List<AdminUser> adminUsers = this.systemDaoSupport.queryForList(sql, AdminUser.class);
        if (adminUsers.size() <= 1 && adminUser.getFounder().equals(1)) {
            throw new ServiceException(SystemErrorCode.E916.code(), "必须保留一个超级管理员");
        }
        this.systemDaoSupport.execute("update es_admin_user set user_state = -1 where id = ?", id);
        cache.put(UserTokenPrefix.ADMIN_DISABLED.getPrefix()+id, -1,javashopConfig.getAccessTokenTimeout() + 60);
    }

    @Override
    public AdminUser getModel(Integer id) {
        return this.systemDaoSupport.queryForObject("select * from es_admin_user where user_state = 0 and id = ?", AdminUser.class, id);
    }

    @Override
    public AdminLoginVO login(String name, String password) {
        String sql = "select * from es_admin_user where username = ? and password = ? and user_state = 0";
        AdminUser adminUser = this.systemDaoSupport.queryForObject(sql, AdminUser.class, name, StringUtil.md5(password + name.toLowerCase()));
        if (adminUser == null || !StringUtil.equals(adminUser.getUsername(), name)) {
            throw new ServiceException(SystemErrorCode.E918.code(), "管理员账号密码错误");
        }
        AdminLoginVO adminLoginVO = new AdminLoginVO();
        adminLoginVO.setUid(adminUser.getId());
        adminLoginVO.setUsername(name);
        adminLoginVO.setDepartment(adminUser.getDepartment());
        adminLoginVO.setFace(adminUser.getFace());
        adminLoginVO.setRoleId(adminUser.getRoleId());
        adminLoginVO.setFounder(adminUser.getFounder());
        String accessToken = this.createToken(adminUser, javashopConfig.getAccessTokenTimeout());
        String refreshToken = this.createToken(adminUser, javashopConfig.getRefreshTokenTimeout());
        adminLoginVO.setAccessToken(accessToken);
        adminLoginVO.setRefreshToken(refreshToken);
        String uuid = ThreadContextHolder.getHttpRequest().getHeader("uuid");
        cache.put(TokenKeyGenerate.generateAdminAccessToken(uuid, adminUser.getId()), accessToken, javashopConfig.getAccessTokenTimeout() + 60);
        cache.put(TokenKeyGenerate.generateAdminRefreshToken(uuid, adminUser.getId()), refreshToken, javashopConfig.getRefreshTokenTimeout() + 60);
        cache.put(UserTokenPrefix.ADMIN_DISABLED.getPrefix() + adminUser.getId(), adminUser.getUserState(),javashopConfig.getAccessTokenTimeout() + 60);
        return adminLoginVO;
    }

    @Override
    public String exchangeToken(String refreshToken) {
        if (refreshToken != null) {
            Claims claims
                    = Jwts.parser()
                    .setSigningKey(JWTConstant.SECRET)
                    .parseClaimsJws(refreshToken).getBody();
            Integer uid = claims.get("uid", Integer.class);
            //获取uuid
            String uuid = ThreadContextHolder.getHttpRequest().getHeader("uuid");
            //根据id获取管理员 校验当前管理员是否存在
            AdminUser adminUser = this.getModel(uid);
            if (adminUser == null) {
                throw new ResourceNotFoundException("当前管理员不存在");
            }
            //从缓存中获取refreshToken

            String key = TokenKeyGenerate.generateAdminRefreshToken(uuid, uid);
            String token = StringUtil.toString(cache.get(key));
            if (StringUtil.isEmpty(token)) {
                throw new ServiceException(MemberErrorCode.E110.code(), "当前管理员已经退出");
            }
            //判断是否过期
            long tokenDate = DateUtil.getDateline(claims.getExpiration().toString(),"EEE MMM dd HH:mm:ss z yyyy");
            long currTime = DateUtil.getDateline();
            if (currTime > tokenDate) {
                throw new ServiceException(MemberErrorCode.E109.code(), "当前token已经失效");
            }
            if (adminUser != null) {
                //判断权限
                String newAccessToken = this.createToken(adminUser, javashopConfig.getAccessTokenTimeout());
                String newRefreshToken = this.createToken(adminUser, javashopConfig.getRefreshTokenTimeout());
                cache.put(TokenKeyGenerate.generateAdminRefreshToken(uuid, uid), newRefreshToken, javashopConfig.getRefreshTokenTimeout() + 60);
                cache.put(TokenKeyGenerate.generateAdminAccessToken(uuid, uid), newAccessToken, javashopConfig.getAccessTokenTimeout() + 60);
                cache.put(UserTokenPrefix.ADMIN_DISABLED.getPrefix() + adminUser.getId(), adminUser.getUserState(),javashopConfig.getAccessTokenTimeout() + 60);
                Map map = new HashMap(16);
                map.put("accessToken", newAccessToken);
                map.put("refreshToken", newRefreshToken);
                return JsonUtil.objectToJson(map);
            }
            throw new ResourceNotFoundException("当前管理员不存在");
        }
        throw new ResourceNotFoundException("当前管理员不存在");
    }

    /**
     * 创建token
     *
     * @param adminUser 管理员
     * @param time      失效时间
     * @return
     */
    private String createToken(AdminUser adminUser, int time) {
        ObjectMapper oMapper = new ObjectMapper();
        Admin admin = new Admin();
        admin.setUid(adminUser.getId());
        admin.setUsername(adminUser.getUsername());
        if (adminUser.getFounder().equals(1)) {
            admin.setRole("SUPER_ADMIN");
        } else {
            RoleDO roleDO = this.roleManager.getModel(adminUser.getRoleId());
            admin.setRole(roleDO.getRoleName());
        }
        Map buyerMap = oMapper.convertValue(admin, HashMap.class);
        return Jwts.builder()
                .setClaims(buyerMap)
                .setSubject("ADMIN")
                .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                .signWith(SignatureAlgorithm.HS512, JWTConstant.SECRET)
                .compact();

    }

    @Override
    public void logout(Integer uid) {
        String uuid = ThreadContextHolder.getHttpRequest().getHeader("uuid");
        cache.remove(TokenKeyGenerate.generateAdminRefreshToken(uuid, uid));
        cache.remove(TokenKeyGenerate.generateAdminAccessToken(uuid, uid));
        cache.remove(UserTokenPrefix.ADMIN_DISABLED.getPrefix() + uid);
    }
}
