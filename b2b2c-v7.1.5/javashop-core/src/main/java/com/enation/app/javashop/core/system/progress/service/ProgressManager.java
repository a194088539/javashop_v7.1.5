package com.enation.app.javashop.core.system.progress.service;

import com.enation.app.javashop.core.system.progress.model.TaskProgress;

/**
 * 进度管理接口
 *
 * @author chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/22 下午3:10
 */
public interface ProgressManager {
    /**
     * 获取进度信息
     *
     * @param id 唯一标识
     * @return 进度
     */
    TaskProgress getProgress(String id);

    /**
     * 写入进度
     *
     * @param id
     * @param progress
     */
    void putProgress(String id, TaskProgress progress);

    /**
     * 移除任务
     *
     * @param id 唯一标识
     */
    void remove(String id);

    /**
     * 任务开始
     *
     * @param key   任务id
     * @param count 任务数
     */
    void taskBegin(String key, Integer count);

    /**
     * 任务结束
     *
     * @param key     任务id
     * @param message 消息
     */
    void taskEnd(String key, String message);

    /**
     * 任务异常
     *
     * @param key     任务id
     * @param message 消息
     */
    void taskError(String key, String message);

    /**
     * 更新任务消息
     *
     * @param key
     * @param message
     */
    void taskUpdate(String key, String message);


}
