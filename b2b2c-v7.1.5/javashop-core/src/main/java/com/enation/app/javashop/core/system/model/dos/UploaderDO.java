package com.enation.app.javashop.core.system.model.dos;

import java.io.Serializable;

import com.enation.app.javashop.core.system.model.vo.UploaderVO;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.PrimaryKeyField;
import com.enation.app.javashop.framework.database.annotation.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.Gson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

/**
 * 存储方案实体
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-22 09:31:56
 */
@Table(name="es_uploader")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UploaderDO implements Serializable {

	private static final long serialVersionUID = 3081675229152673L;

	/**储存id*/
	@Id(name = "id")
	@ApiModelProperty(hidden=true)
	private Integer id;
	/**存储名称*/
	@Column(name = "name")
	@NotEmpty(message="存储名称不能为空")
	@ApiModelProperty(name="name",value="存储名称",required=true)
	private String name;
	/**是否开启*/
	@Column(name = "open")
	@ApiModelProperty(name="open",value="是否开启",required=false)
	private Integer open;
	/**存储配置*/
	@Column(name = "config")
	@NotEmpty(message="存储配置不能为空")
	@ApiModelProperty(name="config",value="存储配置",required=true)
	private String config;
	/**存储插件id*/
	@Column(name = "bean")
	@NotEmpty(message="存储插件id不能为空")
	@ApiModelProperty(name="bean",value="存储插件id",required=true)
	private String bean;

	public UploaderDO(UploaderVO uploaderVO) {
		this.id = uploaderVO.getId();
		this.name = uploaderVO.getName();
		this.open = uploaderVO.getOpen();
		this.bean = uploaderVO.getBean();
		Gson gson = new Gson();
		this.config = gson.toJson(uploaderVO.getConfigItems());
	}
	public UploaderDO() {

	}

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getOpen() {
		return open;
	}
	public void setOpen(Integer open) {
		this.open = open;
	}

	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}

	public String getBean() {
		return bean;
	}
	public void setBean(String bean) {
		this.bean = bean;
	}
	@Override
	public String toString() {
		return "UploaderDO [id=" + id + ", name=" + name + ", open=" + open + ", config=" + config + ", bean=" + bean
				+ "]";
	}



}