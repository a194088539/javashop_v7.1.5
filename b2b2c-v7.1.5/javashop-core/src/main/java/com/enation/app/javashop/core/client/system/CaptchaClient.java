package com.enation.app.javashop.core.client.system;

/**
 * 验证码客户端
 *
 * @author zh
 * @version v7.0
 * @date 18/7/27 上午11:48
 * @since v7.0
 */

public interface CaptchaClient {
    /**
     * 图片验证
     *
     * @param uuid  uid
     * @param code  验证码
     * @param scene 业务类型
     * @return
     */
    boolean valid(String uuid, String code, String scene);

    /**
     * 清除图片验证码
     *
     * @param uuid
     * @param code
     * @param scene
     */
    void deleteCode(String uuid, String code, String scene);
}
