package com.enation.app.javashop.core.member.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 会员电子发票附件vo
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-24
 */
public class ReceiptFileVO implements Serializable {

    @ApiModelProperty(value = "开票历史记录ID")
    private Integer historyId;

    @ApiModelProperty(value = "电子发票附件集合")
    private List<String> files;

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "ReceiptFileVO{" +
                "historyId=" + historyId +
                ", files=" + files +
                '}';
    }
}
