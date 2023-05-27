package com.enation.app.javashop.core.shop.service.impl;

import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.shop.model.dos.Clerk;
import com.enation.app.javashop.core.shop.model.dos.ShopRole;
import com.enation.app.javashop.core.shop.model.dto.ClerkDTO;
import com.enation.app.javashop.core.shop.model.vo.ClerkShowVO;
import com.enation.app.javashop.core.shop.model.vo.ClerkVO;
import com.enation.app.javashop.core.shop.service.ClerkManager;
import com.enation.app.javashop.core.shop.service.ShopRoleManager;
import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.UserTokenPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 店员业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-04 18:53:52
 */
@Service
public class ClerkManagerImpl implements ClerkManager {


    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private ShopRoleManager shopRoleManager;

    @Override
    public Page list(int page, int pageSize, int disabled, String keyword) {
        StringBuffer sql = new StringBuffer("select c.* from es_clerk c left join es_member m on c.member_id = m.member_id where c.shop_id = ? and c.user_state = ?");

        List termList = new ArrayList();

        termList.add(UserContext.getSeller().getSellerId());
        termList.add(disabled);

        if (StringUtil.notEmpty(keyword)) {
            sql.append(" and (c.clerk_name like ? or m.mobile like ? or m.email like ?) ");
            termList.add("%" + keyword + "%");
            termList.add("%" + keyword + "%");
            termList.add("%" + keyword + "%");
        }

        sql.append(" order by create_time desc");

        Page webPage = this.memberDaoSupport.queryForPage(sql.toString(), page, pageSize, Clerk.class, termList.toArray());

        List<ClerkShowVO> clerks = new ArrayList<>();
        for (Clerk clerk : (List<Clerk>) webPage.getData()) {
            ClerkShowVO clerkShowVO = new ClerkShowVO();
            clerkShowVO.setMemberId(clerk.getMemberId());
            clerkShowVO.setRoleId(clerk.getRoleId());
            if (clerk.getFounder().equals(1) && clerk.getRoleId().equals(0)) {
                clerkShowVO.setRole("超级店员");
            } else {
                ShopRole shopRole = shopRoleManager.getModel(clerk.getRoleId());
                clerkShowVO.setRole(shopRole.getRoleName());
            }
            Member member = memberManager.getModel(clerk.getMemberId());
            clerkShowVO.setEmail(member.getEmail());
            clerkShowVO.setMobile(member.getMobile());
            clerkShowVO.setFounder(clerk.getFounder());
            clerkShowVO.setClerkId(clerk.getClerkId());
            clerkShowVO.setUname(clerk.getClerkName());
            clerkShowVO.setShopId(clerk.getShopId());
            clerkShowVO.setUserState(clerk.getUserState());
            clerks.add(clerkShowVO);
        }
        webPage.setData(clerks);
        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Clerk addOldMemberClerk(ClerkDTO clerkDTO) {
        Member member = memberManager.getMemberByMobile(clerkDTO.getMobile());
        if (member != null) {
            Clerk clerk = new Clerk();
            clerk.setClerkName(member.getUname());
            clerk.setMemberId(member.getMemberId());
            clerk.setRoleId(clerkDTO.getRoleId());
            return this.add(clerk);
        }
        return null;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Clerk addNewMemberClerk(ClerkVO clerkVO) {
        Member member = new Member();
        member.setUname(clerkVO.getUname());
        member.setMobile(clerkVO.getMobile());
        member.setPassword(clerkVO.getPassword());
        member.setEmail(clerkVO.getEmail());
        member.setNickname(clerkVO.getUname());
        member.setSex(0);
        member = this.memberManager.register(member);
        //添加店员信息
        Clerk clerk = new Clerk();
        clerk.setClerkName(clerkVO.getUname());
        clerk.setRoleId(clerkVO.getRoleId());
        clerk.setMemberId(member.getMemberId());
        return this.add(clerk);
    }

    /**
     * 添加店员
     *
     * @param clerk 店员信息
     * @return
     */
    private Clerk add(Clerk clerk) {
        if (clerk.getRoleId().equals(0)) {
            throw new ServiceException(MemberErrorCode.E139.code(), "无法添加超级管理员");
        }
        clerk.setFounder(0);
        ShopRole shopRole = shopRoleManager.getModel(clerk.getRoleId());
        if (shopRole != null && shopRole.getShopId().equals(UserContext.getSeller().getSellerId())) {
            clerk.setUserState(0);
            clerk.setCreateTime(DateUtil.getDateline());
            clerk.setShopId(UserContext.getSeller().getSellerId());
            this.memberDaoSupport.insert(clerk);
            clerk.setClerkId(this.memberDaoSupport.getLastId("es_clerk"));
            return clerk;
        }
        throw new ResourceNotFoundException("当前角色不存在");
    }

    @Override
    public Clerk addSuperClerk(Clerk clerk) {
        clerk.setCreateTime(DateUtil.getDateline());
        clerk.setUserState(0);
        this.memberDaoSupport.insert(clerk);
        clerk.setClerkId(this.memberDaoSupport.getLastId("es_clerk"));
        return clerk;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Clerk edit(Clerk clerk, Integer id) {
        this.memberDaoSupport.update(clerk, id);
        return clerk;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        Clerk clerk = this.getModel(id);
        if (clerk == null || !clerk.getShopId().equals(UserContext.getSeller().getSellerId())) {
            throw new NoPermissionException("无权限");
        }
        if (clerk.getFounder().equals(1)) {
            throw new ServiceException(MemberErrorCode.E138.code(), "无法删除超级管理员");
        }
        String sql = "update es_clerk set user_state = ? where clerk_id = ?";
        this.memberDaoSupport.execute(sql, -1, id);
    }

    @Override
    public Clerk getModel(Integer id) {
        return this.memberDaoSupport.queryForObject(Clerk.class, id);
    }

    @Override
    public void recovery(Integer id) {
        Clerk clerk = this.getModel(id);
        //校验权限
        if (clerk == null || !clerk.getShopId().equals(UserContext.getSeller().getSellerId())) {
            throw new NoPermissionException("无权限");
        }
        //校验当前会员是否存在
        Member member = this.memberManager.getModel(clerk.getMemberId());
        if (member == null || !member.getDisabled().equals(0)) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员已经失效，无法恢复此店员");
        }
        String sql = "update es_clerk set user_state = ? where clerk_id = ?";
        this.memberDaoSupport.execute(sql, 0, id);
    }

    @Override
    public Clerk getClerkByMemberId(Integer memberId) {
        String sql = "select * from es_clerk where member_id = ? and user_state = ?";
        return this.memberDaoSupport.queryForObject(sql, Clerk.class, memberId, 0);
    }


}
