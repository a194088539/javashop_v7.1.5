package com.enation.app.javashop.core.client.system;

import com.enation.app.javashop.core.base.SceneType;
import com.enation.app.javashop.core.base.model.vo.SmsSendVO;

/**
 * 发短信客户端
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 上午11:48
 * @since v7.0
 */

public interface SmsClient {
    /**
     * 验证手机验证码
     *
     * @param scene  业务场景
     * @param mobile 手机号码
     * @param code   手机验证码
     * @return 是否通过校验 true通过，false不通过
     */
    boolean valid(String scene, String mobile, String code);

    /**
     * 获取验证通过的手机号
     * @param scene
     * @param mobile
     * @return
     */
    String validMobile(String scene,String mobile);


    /**
     * 发送(发送手机短信)消息
     *
     * @param byName    操作，替换内容
     * @param mobile    手机号码
     * @param sceneType 操作类型
     */
    void sendSmsMessage(String byName, String mobile, SceneType sceneType);


    /**
     * 真实发送手机短信
     *
     * @param smsSendVO
     */
    void send(SmsSendVO smsSendVO);

}
