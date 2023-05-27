package com.enation.javashop.reactnative.android.addressselector.model;

/**
 * Created by LDD on 2017/11/3.
 */

public class Region extends Object implements BaseRagionModel {

    private String  name;
    private Double type;
    private Double parentId;
    private Double id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Double type) {
        this.type = type;
    }

    public Double getParentId() {
        return parentId;
    }

    public void setParentId(Double parentId) {
        this.parentId = parentId;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    @Override
    public String getPickerName() {
        return name;
    }

    @Override
    public int getType() {
        return type.intValue();
    }
}
