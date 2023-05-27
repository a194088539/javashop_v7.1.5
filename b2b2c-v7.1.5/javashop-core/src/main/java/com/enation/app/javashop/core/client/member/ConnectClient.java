package com.enation.app.javashop.core.client.member;

import com.enation.app.javashop.core.member.model.dos.ConnectDO;

/**
 * 第三方连接client
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 下午3:51
 * @since v7.0
 */
public interface ConnectClient {

    /**
     * 获取 联合登录对象
     * @param memberId 会员id
     * @param unionType 类型
     * @return ConnectDO
     */
    ConnectDO getConnect(Integer memberId, String unionType);

    /**
     * 获取会员的openId
     * @param memberId
     * @return
     */
    String getMemberOpenid(Integer memberId);


}
