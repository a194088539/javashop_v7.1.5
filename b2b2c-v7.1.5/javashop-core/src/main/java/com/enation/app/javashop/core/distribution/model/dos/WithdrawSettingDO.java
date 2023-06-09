package com.enation.app.javashop.core.distribution.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 提现设置实体
 *
 * @author Chopper
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2018/5/21 下午3:01
 */
@Table(name = "es_withdraw_setting")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WithdrawSettingDO {

    /**
     * 主键id
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 用户id
     **/
    @Column(name = "member_id")
    @ApiModelProperty(value = "用户id", required = true)
    private Integer memberId;
    /**
     * 参数
     **/
    @Column()
    @ApiModelProperty(value = "参数", required = true)
    private String param;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "WithdrawSettingDO{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", param='" + param + '\'' +
                '}';
    }
}
