package com.enation.app.javashop.core.client.system;

import java.util.List;

/**
 * @author fk
 * @version v2.0
 * @Description: 敏感词client
 * @date 2018/8/10 14:51
 * @since v7.0.0
 */
public interface SensitiveWordsClient {

    /**
     * 获取敏感词列表
     * @return
     */
    List<String> listWords();

}
