package com.enation.app.javashop.core.shop;

/**
 * 店铺异常码
 * Created by kingapex on 2018/3/13.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/13
 */
public enum ShopErrorCode {
	/**
	 * 会员尚未拥有店铺
	 */
    E200("未拥有店铺"),
	E201("模版类型不匹配"),
	E202("模版不存在"),
	E203("店铺名称重复"),
	E204("店铺在申请中，不允许次操作"),
	E205("默认模版不能删除"),
	E206("店铺不存在"),
	E207("已存在店铺"),
	E208("幻灯片不存在"),
	E209("导航不存在"),
	E210("运费模版不存在"),
	E211("物流名称重复"),
	E212("快递鸟公司代码重复"),
	E213("物流公司代码重复"),
	E214("物流公司不存在"),
	E215("物流公司已开启"),
	E216("物流公司已关闭"),
	E217("结束时间不能小于开始时间"),
	E218("店铺分组不存在"),
	E219("顶级分类不可修改上级分类"),
	E220("当前分组存在子分组"),
	E221("当前分组存在商品"),
	E222("分组不存在"),
	E223("父分组不存在"),
	E224("完成上一步才可进行此步操作"),
	E225("店铺模版标识重复"),
	E226("运费模版被使用"),
	E227("非法参数"),
	E228("角色名称已经存在"),
	E229("此角色已经被使用"),
	E230("此用户为店铺店员"),
	E231("店铺导航数量超出上限");

	

    private String describe;

    ShopErrorCode(String des){
        this.describe =des;
    }

    /**
     * 获取异常码
     * @return
     */
    public String code(){
        return this.name().replaceAll("E","");
    }

	public String getDescribe() {
    	return describe;

	}

}
