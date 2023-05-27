package com.enation.app.javashop.buyer.api.goods.model;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * @description: 测试
 * @author: liuyulei
 * @create: 2019-12-12 15:00
 * @version:1.0
 * @since:7.1.4
 **/
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestC extends TestB implements Serializable {

    @Column()
    @ApiModelProperty(value="名称",required=true)
    private String mark;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestC that = (TestC) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(mark, that.mark)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(mark)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TestC{" +
                "mark='" + mark + '\'' +
                "} " + super.toString();
    }
}
