package com.enation.app.javashop.core.member.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员增票资质搜索参数实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-18
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ZpzzQueryParam {

    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(name = "uname", value = "会员登陆用户名")
    private String uname;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status", value = "状态", example = "NEW_APPLY：新申请，AUDIT_PASS：审核通过，AUDIT_REFUSE：审核未通过")
    private String status;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "start_time", value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(name = "end_time", value = "结束时间")
    private String endTime;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ZpzzQueryParam{" +
                "uname='" + uname + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
