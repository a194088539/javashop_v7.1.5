package com.enation.app.javashop.core.member.model.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

/**
 *
 * 会员积分查询
 * @author zh
 * @version v70
 * @since v7.0
 * 2018-04-004 15:44:12
 */
public class MemberPointVO {

    /**
     * 等级积分
     */
    @Min(message="必须为数字", value = 0)
    @ApiModelProperty(name="grade_point",value="等级积分",required=false)
    private Long gradePoint;

    /**消费积分*/
    @Min(message="必须为数字", value = 0)
    @ApiModelProperty(name="consum_point",value="消费积分",required=false)
    private Long consumPoint;

    public Long getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(Long gradePoint) {
        this.gradePoint = gradePoint;
    }

    public Long getConsumPoint() {
        return consumPoint;
    }

    public void setConsumPoint(Long consumPoint) {
        this.consumPoint = consumPoint;
    }

    @Override
    public String toString() {
        return "MemberPointVO{" +
                "gradePoint=" + gradePoint +
                ", consumPoint=" + consumPoint +
                '}';
    }
}
