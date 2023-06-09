package com.enation.app.javashop.core.goodssearch.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;

/**
 * 商品分词po
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-03-16 16:32:45
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsWords {

	private String words;
	private int goodsNum;
	
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	@Override
	public String toString() {
		return "GoodsWords{" +
				"words='" + words + '\'' +
				", goodsNum=" + goodsNum +
				'}';
	}
}
