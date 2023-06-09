package com.enation.app.javashop.core.distribution.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 短链接
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-06-04 上午8:59
 */
@Table(name="es_short_url")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShortUrlDO {
    @Id(name="id")
    @ApiModelProperty(hidden=true)
    private Integer id;

    @Column()
    @ApiModelProperty(value="跳转地址")
    private String url;
    @Column()
    @ApiModelProperty(value="短链接key")
    private String su;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSu() {
        return su;
    }

    public void setSu(String su) {
        this.su = su;
    }

    @Override
    public String toString() {
        return "ShortUrlDO{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", su='" + su + '\'' +
                '}';
    }
}
