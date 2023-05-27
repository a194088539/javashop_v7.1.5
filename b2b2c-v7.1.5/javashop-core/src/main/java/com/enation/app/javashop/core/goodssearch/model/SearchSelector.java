package com.enation.app.javashop.core.goodssearch.model;

import java.util.List;

import com.enation.app.javashop.framework.util.StringUtil;

/**
 * 选器实体
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 16:32:45
 */
public class SearchSelector {
	
	private String name;
	private String url;
	private boolean isSelected;
	private String value;
	private List<SearchSelector> otherOptions;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		if(!StringUtil.isEmpty(url) && url.startsWith("/")){
			url= url.substring(1, url.length());
		}
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	public boolean getIsSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<SearchSelector> getOtherOptions() {
		return otherOptions;
	}
	public void setOtherOptions(List<SearchSelector> otherOptions) {
		this.otherOptions = otherOptions;
	}

	@Override
	public String toString() {
		return "SearchSelector{" +
				"name='" + name + '\'' +
				", url='" + url + '\'' +
				", isSelected=" + isSelected +
				", value='" + value + '\'' +
				", otherOptions=" + otherOptions +
				'}';
	}
}
