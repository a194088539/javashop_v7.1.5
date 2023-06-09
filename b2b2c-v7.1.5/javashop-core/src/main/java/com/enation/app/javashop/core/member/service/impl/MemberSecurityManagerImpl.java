package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.base.CachePrefix;
import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.base.service.EmailManager;
import com.enation.app.javashop.core.base.service.SmsManager;
import com.enation.app.javashop.core.member.MemberErrorCode;
import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.service.MemberManager;
import com.enation.app.javashop.core.member.service.MemberSecurityManager;
import com.enation.app.javashop.core.passport.service.PassportManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Buyer;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.framework.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员安全业务实现
 *
 * @author zh
 * @version v7.0
 * @date 18/4/23 下午3:24
 * @since v7.0
 */
@Service
public class MemberSecurityManagerImpl implements MemberSecurityManager {

    @Autowired
    private MemberManager memberManager;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private Cache cache;
    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private EmailManager emailManager;

    @Override
    public void sendBindSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确");
        }
        //校验会员是否存在
        Member member = memberManager.getMemberByMobile(mobile);
        if (member != null) {
            throw new ServiceException(MemberErrorCode.E111.code(), "此手机号码已经绑定其他用户");
        }
        smsManager.sendSmsMessage("手机绑定操作", mobile, SceneType.BIND_MOBILE);
    }

    @Override
    public void sendBindEmailCode(String email) {
        if (!Validator.isEmail(email)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "电子邮箱格式不正确");
        }

        //校验当前电子邮箱是否已经绑定了其他会员
        Buyer buyer = UserContext.getBuyer();
        this.validEmailBindMember(email, buyer.getUid());

        emailManager.sendEmailMessage("邮箱绑定操作", email, SceneType.BIND_EMAIL);
    }

    @Override
    public void sendValidateSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确");
        }
        //校验会员是否存在
        Member member = memberManager.getMemberByMobile(mobile);
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        smsManager.sendSmsMessage("手机验证码验证", mobile, SceneType.VALIDATE_MOBILE);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePassword(Integer memberId, String password) {
        //校验是否经过手机验证而进行此步骤
        Member member = memberManager.getModel(memberId);
        //校验当前会员是否存在
        if (member == null) {
            throw new ResourceNotFoundException("当前会员不存在");
        }
        //校验当前会员是否被禁用
        if (!member.getDisabled().equals(0)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "当前账号已经禁用");
        }
        //校验密码长度
        String newPassword = StringUtil.md5(password + member.getUname().toLowerCase());
        String sql = "update es_member set password = ? where member_id =? ";
        this.memberDaoSupport.execute(sql, newPassword, memberId);
        //清除步骤标记缓存
        passportManager.clearSign(member.getMobile(), SceneType.VALIDATE_MOBILE.name());
    }

    @Override
    public void bindMobile(String mobile) {
        Buyer buyer = UserContext.getBuyer();
        //校验手机号码是否已经被占用
        Member member = memberManager.getModel(buyer.getUid());
        if (member != null && !StringUtil.isEmpty(member.getMobile())) {
            throw new ServiceException(MemberErrorCode.E111.code(), "当前会员已经绑定手机号");
        }
        List list = memberDaoSupport.queryForList("select * from es_member where mobile = ?", mobile);
        if (list.size() > 0) {
            throw new ServiceException(MemberErrorCode.E111.code(), "当前手机号已经被占用");
        }
        String sql = "update es_member set mobile = ? where member_id = ?";
        this.memberDaoSupport.execute(sql, mobile, buyer.getUid());
    }

    @Override
    public void bindEmail(String email) {
        Buyer buyer = UserContext.getBuyer();

        this.validEmailBindMember(email, buyer.getUid());

        String sql = "update es_member set email = ? where member_id = ?";
        this.memberDaoSupport.execute(sql, email, buyer.getUid());
    }

    @Override
    public void changeBindMobile(String mobile) {
        Buyer buyer = UserContext.getBuyer();
        if (buyer != null) {
            //校验是否经过手机验证而进行此步骤
            Member member = memberManager.getModel(buyer.getUid());
            String str = StringUtil.toString(cache.get(CachePrefix.MOBILE_VALIDATE.getPrefix() + "_" + SceneType.VALIDATE_MOBILE.name() + "_" + member.getMobile()));
            if (StringUtil.isEmpty(str)) {
                throw new ServiceException(MemberErrorCode.E115.code(), "对已绑定手机校验失效");
            }
            List list = memberDaoSupport.queryForList("select * from es_member where mobile = ?", mobile);
            if (list.size() > 0) {
                throw new ServiceException(MemberErrorCode.E111.code(), "当前手机号已经被占用");
            }
            String sql = "update es_member set mobile = ? where member_id = ?";
            this.memberDaoSupport.execute(sql, mobile, buyer.getUid());
            //清除步骤标记缓存
            passportManager.clearSign(member.getMobile(), SceneType.VALIDATE_MOBILE.name());
        }
    }

    /**
     * 判断电子邮箱是否已被绑定(不包含当前登录会员)
     * @param email
     * @param memberId
     */
    private void validEmailBindMember(String email, Integer memberId) {
        List list = memberDaoSupport.queryForList("select * from es_member where email = ? and member_id != ?", email, memberId);
        if (list.size() > 0) {
            throw new ServiceException(MemberErrorCode.E111.code(), "当前电子邮箱已经被绑定");
        }
    }
}
