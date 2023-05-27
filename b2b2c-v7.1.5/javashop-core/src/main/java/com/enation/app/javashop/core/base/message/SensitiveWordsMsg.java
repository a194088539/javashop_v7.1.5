package com.enation.app.javashop.core.base.message;

import java.io.Serializable;

/**
 * 敏感词消息
 *
 * @author fk
 * @version 2.0
 * @since 7.1.5
 * 2019-09-07 18：00
 */
public class SensitiveWordsMsg implements Serializable {

    /**
     * 词汇
     */
    private String word;

    /**
     * 操作
     */
    private Integer operation;

    /**
     * 添加操作
     */
    public final static Integer ADD = 1;

    /**
     * 删除操作
     */
    public final static Integer DELETE = 2;

    public SensitiveWordsMsg(String word, Integer operation) {
        this.word = word;
        this.operation = operation;
    }

    public SensitiveWordsMsg() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }
}
