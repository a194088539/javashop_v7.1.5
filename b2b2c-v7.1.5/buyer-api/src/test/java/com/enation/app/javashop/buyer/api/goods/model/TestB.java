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
 * @description: 测试子类
 * @author: liuyulei
 * @create: 2019-12-12 14:30
 * @version:1.0
 * @since:7.1.4
 **/
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TestB extends TestA implements Serializable {

    @Column()
    @ApiModelProperty(value="排序",required=true)
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestB that = (TestB) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(sort, that.sort)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(sort)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TestB{" +
                "sort=" + sort +
                "} " + super.toString();
    }
}
