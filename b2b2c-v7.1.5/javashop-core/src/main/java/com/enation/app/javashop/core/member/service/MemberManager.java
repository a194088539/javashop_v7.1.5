package com.enation.app.javashop.core.member.service;

import com.enation.app.javashop.core.member.model.dos.Member;
import com.enation.app.javashop.core.member.model.dto.MemberQueryParam;
import com.enation.app.javashop.core.member.model.dto.MemberStatisticsDTO;
import com.enation.app.javashop.core.member.model.vo.BackendMemberVO;
import com.enation.app.javashop.core.member.model.vo.MemberPointVO;
import com.enation.app.javashop.core.member.model.vo.MemberVO;
import com.enation.app.javashop.framework.database.Page;

import java.util.List;

/**
 * 会员业务层
 *
 * @author zh
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 11:33:56
 */
public interface MemberManager {

    /**
     * 查询会员列表
     *
     * @param memberQueryParam 查询条件
     * @return
     */
    Page list(MemberQueryParam memberQueryParam);

    /**
     * 修改会员登录次数
     * @param memberId
     * @param now
     */
    void updateLoginNum(Integer memberId, Long now);
    /**
     * 修改会员
     *
     * @param member 会员
     * @param id     会员主键
     * @return Member 会员
     */
    Member edit(Member member, Integer id);

    /**
     * 删除会员
     *
     * @param clazz Member类
     * @param id    会员主键
     */
    void delete(Class<Member> clazz, Integer id);

    /**
     * 获取会员
     *
     * @param id 会员表主键
     * @return Member  会员
     */
    Member getModel(Integer id);

    /**
     * 根据用户名查询会员
     *
     * @param uname 用户名
     * @return 会员信息
     */
    Member getMemberByName(String uname);

    /**
     * 根据用户手机号码查询会员
     *
     * @param mobile 手机号码
     * @return 会员信息
     */
    Member getMemberByMobile(String mobile);

    /**
     * 根据邮箱获取用户
     *
     * @param email
     * @return
     */
    Member getMemberByEmail(String email);

    /**
     * 查询当前会员的积分
     *
     * @return 会员vo
     */
    MemberPointVO getMemberPoint();

    /**
     * 生成用户名
     *
     * @param uname 用户填写的用户名
     * @return 用户名数组
     */
    String[] generateMemberUname(String uname);

    /**
     * 会员注册
     *
     * @param member 会员
     * @return 会员信息
     */
    Member register(Member member);

    /**
     * 会员用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @param memberOrSeller 会员还是卖家，1 会员  2 卖家
     * @return 是否登录成功
     */
    MemberVO login(String username, String password,Integer memberOrSeller);

    /**
     * 动态登录
     *
     * @param phone 手机号码
     * @return 是否登录成功
     */
    MemberVO mobileLogin(String phone,Integer memberOrSeller);

    /**
     * 会员退出
     *
     * @param uid 会员id
     */
    void logout(Integer uid);

    /**
     * 登录会员后的处理
     *
     * @param member
     * @param memberOrSeller 会员还是卖家，1 会员  2 卖家
     * @return
     */
    MemberVO loginHandle(Member member,Integer memberOrSeller);

    /**
     * 联合登录后处理
     *
     * @param member 会员信息
     * @param uuid   uuid
     * @return 会员信息
     */
    MemberVO connectLoginHandle(Member member, String uuid);


    /**
     * 验证会员账号密码的正确性(只验证不登录)
     *
     * @param username 用户名/手机号/邮箱
     * @param password 密码
     * @return 正确 true 错误 false
     */
    Member validation(String username, String password);

    /**
     * 根据账号获取当前会员的一些信息，供找回密码使用
     *
     * @param account 用户名/手机号/邮箱
     * @return 会员信息
     */
    Member getMemberByAccount(String account);

    /**
     * 获取当前会员的一些附属统计数
     * 比如：会员订单数、会员商品收藏数、会员店铺收藏数
     *
     * @return 会员统计信息
     */
    MemberStatisticsDTO getMemberStatistics();

    /**
     * 查询新的会员
     *
     * @param length
     * @return
     */
    List<BackendMemberVO> newMember(Integer length);

    /**
     * 根据会员ids获取会员的集合
     *
     * @param memberIds 会员id数组
     * @return 会员信息
     */
    List<Member> getMemberByIds(Integer[] memberIds);


    /**
     * 登录次数归零
     */
    void loginNumToZero();


    void memberLoginout(Integer memberId);
}