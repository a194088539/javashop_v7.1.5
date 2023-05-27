package com.enation.javashop.reactnative.android.addressselector.model;


import com.enation.javashop.reactnative.android.addressselector.utils.RegionType;

/**
 * 基本数据接口  如果要使用该控件必须继承该接口
 */

public interface BaseRagionModel {
    String getPickerName();
    int getType();
}
