package com.enation.app.javashop.core.aftersale.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 售后图片实体
 * 用于存放用户申请售后服务上传的相关图片
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-10-15
 */
@Table(name = "es_as_gallery")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AfterSaleGalleryDO implements Serializable {

    private static final long serialVersionUID = -3905020463624194274L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden=true)
    private Integer id;
    /**
     * 售后服务单号
     */
    @Column(name = "service_sn")
    @ApiModelProperty(name = "service_sn", value = "售后服务单号", required = false)
    private String serviceSn;
    /**
     * 图片链接
     */
    @Column(name = "img")
    @ApiModelProperty(name = "img", value = "图片链接", required = false)
    private String img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceSn() {
        return serviceSn;
    }

    public void setServiceSn(String serviceSn) {
        this.serviceSn = serviceSn;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AfterSaleGalleryDO that = (AfterSaleGalleryDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serviceSn, that.serviceSn) &&
                Objects.equals(img, that.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceSn, img);
    }

    @Override
    public String toString() {
        return "AfterSaleGalleryDO{" +
                "id=" + id +
                ", serviceSn='" + serviceSn + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
