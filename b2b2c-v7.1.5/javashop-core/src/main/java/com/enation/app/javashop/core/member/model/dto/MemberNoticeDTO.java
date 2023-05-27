package com.enation.app.javashop.core.member.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 会员站内消息DTO
 * @author duanmingyu
 * @version v1.0
 * @since V7.1.5
 * 2019-09-24
 */
public class MemberNoticeDTO implements Serializable {

    private static final long serialVersionUID = 2958965751114746887L;

    @ApiModelProperty(name = "total", value = "消息总数")
    private Integer total;
    @ApiModelProperty(name = "system_num", value = "系统消息总数")
    private Integer systemNum;
    @ApiModelProperty(name = "ask_num", value = "问答消息总数")
    private Integer askNum;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(Integer systemNum) {
        this.systemNum = systemNum;
    }

    public Integer getAskNum() {
        return askNum;
    }

    public void setAskNum(Integer askNum) {
        this.askNum = askNum;
    }

    @Override
    public String toString() {
        return "MemberNoticeDTO{" +
                "total=" + total +
                ", systemNum=" + systemNum +
                ", askNum=" + askNum +
                '}';
    }
}
