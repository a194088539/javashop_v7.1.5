package com.enation.app.javashop.core.goods.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 运费模板对象
 *
 * @author zh
 * @version v1.0
 * @Description:
 * @since v7.0
 * 2019/9/25 上午12:07
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TemplateScript implements Serializable {


    @ApiModelProperty(value = "sku_id")
    private Integer skuId;

    @ApiModelProperty(name = "template_id", value = "运费模板id")
    private Integer templateId;

    @ApiModelProperty(name = "script", value = "脚本内容")
    private String script;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }



    @Override
    public String toString() {
        return "TemplateScript{" +
                "skuId=" + skuId +
                ", templateId=" + templateId +
                ", script='" + script + '\'' +
                '}';
    }
}


