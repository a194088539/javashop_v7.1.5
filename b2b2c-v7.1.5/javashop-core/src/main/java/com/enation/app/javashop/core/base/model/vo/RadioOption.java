package com.enation.app.javashop.core.base.model.vo;

/**
 * 单选选项vo
 *
 * @author zh
 * @version v7.0
 * @date 18/7/25 下午10:22
 * @since v7.0
 */
public class RadioOption {

    /**
     * 选项
     */
    private String label;

    /**
     * 选项值
     */
    private Object value;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RadioOption{" +
                "label='" + label + '\'' +
                ", value=" + value +
                '}';
    }
}
