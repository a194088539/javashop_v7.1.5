package com.enation.app.javashop.core.system.progress.model;

/**
 * 进度枚举
 *
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年9月6日 下午8:44:42
 */
public enum ProgressEnum {


//  PROGRESS STATUS ENUM
    DOING("进行中"), SUCCESS("成功"), EXCEPTION("异常");

    String status;

    ProgressEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}