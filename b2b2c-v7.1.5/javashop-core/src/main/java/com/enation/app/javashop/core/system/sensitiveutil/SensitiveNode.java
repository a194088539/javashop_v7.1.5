package com.enation.app.javashop.core.system.sensitiveutil;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * @author fk
 * @version v2.0
 * @Description: 敏感词节点，每个节点包含了以相同的2个字符开头的所有词
 * @date 2019/9/3 16:46
 * @since v7.1.5
 */
public class SensitiveNode implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 头两个字符的mix，mix相同，两个字符相同
	 */
	protected final int headTwoCharMix;
	
	/**
	 * 所有以这两个字符开头的词表
	 */
	protected final TreeSet<StringPointer> words = new TreeSet<StringPointer>();
	
	/**
	 * 下一个节点
	 */
	protected SensitiveNode next;
	
	public SensitiveNode(int headTwoCharMix){
		this.headTwoCharMix = headTwoCharMix;
	}
	
	public SensitiveNode(int headTwoCharMix, SensitiveNode parent){
		this.headTwoCharMix = headTwoCharMix;
		parent.next = this;
	}

}
