package com.enation.app.javashop.core.base.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;


/**
 * 系统设置实体
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-27 18:47:17
 */
@Table(name="es_settings")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SettingsDO implements Serializable {

	private static final long serialVersionUID = 3372606354638487L;

	/**系统设置id*/
	@Id(name = "id")
	@ApiModelProperty(hidden=true)
	private Integer id;
	/**系统配置信息*/
	@Column(name = "cfg_value")
	@NotEmpty(message="系统配置信息不能为空")
	@ApiModelProperty(name="cfg_value",value="系统配置信息",required=true)
	private String cfgValue;
	/**业务设置标识*/
	@Column(name = "cfg_group")
	@NotEmpty(message="业务设置标识不能为空")
	@ApiModelProperty(name="cfg_group",value="业务设置标识",required=true)
	private String cfgGroup;



	@Override
	public String toString() {
		return "SettingsDO [id=" + id + ", cfgValue=" + cfgValue + ", cfgGroup=" + cfgGroup + "]";
	}
	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getCfgValue() {
		return cfgValue;
	}
	public void setCfgValue(String cfgValue) {
		this.cfgValue = cfgValue;
	}

	public String getCfgGroup() {
		return cfgGroup;
	}
	public void setCfgGroup(String cfgGroup) {
		this.cfgGroup = cfgGroup;
	}



}