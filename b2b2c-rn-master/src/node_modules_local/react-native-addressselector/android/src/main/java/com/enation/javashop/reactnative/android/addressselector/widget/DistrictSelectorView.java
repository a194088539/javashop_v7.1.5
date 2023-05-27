package com.enation.javashop.reactnative.android.addressselector.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.annotation.StyleableRes;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.enation.javashop.reactnative.android.addressselector.R;
import com.enation.javashop.reactnative.android.addressselector.adapter.RegionAdapter;
import com.enation.javashop.reactnative.android.addressselector.model.BaseRagionModel;
import com.enation.javashop.reactnative.android.addressselector.utils.RegionType;
import com.enation.javashop.reactnative.android.addressselector.utils.Utils;
import com.facebook.react.bridge.ReactApplicationContext;

import java.util.ArrayList;

/**
 * 地区选择器
 * @param <T> 数据Model泛型
 */

public class DistrictSelectorView<T extends BaseRagionModel>  extends Dialog {

    /**
     * 背景布局
     */
    private LinearLayout backView;

    /**
     * 根布局
     */
    private View parent;

    /**
     * 标题布局
     */
    private LinearLayout topBar;

    /**
     * 确认按钮
     */
    private TextView  confirm;

    /**
     * 后退按钮
     */
    private TextView back;

    /**
     * 地址选择提示
     */
    private TextView hint;

    /**
     * 数据源
     */
    private ArrayList<T> dataSouce;

    /**
     * 地址列表适配器
     */
    private RegionAdapter adapter;

    /**
     * 监听事件
     */
    private RegionListener<T> listener;

    /**
     * 四级地区
     */
    private T  bean1,bean2,bean3,bean4;

    private ReactApplicationContext context;
    /**
     * DistrictSelectorView 初始化方法
     * @param context 上下文
     */
    public DistrictSelectorView(Context context,ReactApplicationContext reactContext) {
        super(context, R.style.Dialog);
        this.context = reactContext;
        getReactContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    /**
     * DistrictSelectorView 初始化方法 可自定义Style
     * @param context       上下文
     * @param themeResId    styleId
     */
    public DistrictSelectorView(Context context,@StyleRes int themeResId) {
        super(context, themeResId);
        getReactContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    /**
     * 设置监听事件
     * @param listener 监听事件
     * @return this
     */
    public DistrictSelectorView setRegionListener(RegionListener<T> listener){
        this.listener = listener;
        return this;
    }

    /**
     * 设置标题栏样式
     * @param rid 资源ID
     * @return    this
     */
    public DistrictSelectorView setTopBarStyle(@DrawableRes int rid){
        topBar.setBackgroundResource(rid);
        return this;
    }

    /**
     * 设置背景样式
     * @param rid 资源ID
     * @return    this
     */
    public DistrictSelectorView setBackGroundStyle(@DrawableRes int rid){
        backView.setBackgroundResource(rid);
        return this;
    }

    /**
     * 设置确认按钮样式
     * @param Rid 资源ID
     * @return this
     */
    public DistrictSelectorView setConfrimStyle(@DrawableRes int Rid){
        confirm.setBackgroundResource(Rid);
        return this;
    }

    /**
     * 设置后退按钮样式
     * @param Rid 资源ID
     * @return this
     */
    public DistrictSelectorView setBackStyle(@DrawableRes int Rid){
        back.setBackgroundResource(Rid);
        return this;
    }

    public DistrictSelectorView setBackText(String text){
        back.setText(text);
        return this;
    }

    /**
     * 设置确认按钮文字
     * @param text 文字
     * @return this
     */
    public DistrictSelectorView setConfirmText(String text){
        confirm.setText(text);
        return this;
    }

    /**
     * 设置数据源
     * @param data 数据源
     * @return this
     */
    public DistrictSelectorView setData(final ArrayList<T> data){

        if (data!=null&&data.size()>0&&adapter!=null){
              getReactContext().runOnUiQueueThread(new Runnable() {
                  @Override
                  public void run() {
                      dataSouce=data;
                      adapter.setData(dataSouce);
                  }
              });
        }
        return this;
    }

    /**
     * 初始化布局
     */
    private void init(){
        /**初始化地址适配器*/
        adapter = new RegionAdapter(context,dataSouce);

        /**初始化根布局*/
        parent =  getLayoutInflater().inflate(R.layout.city_choice,null);

        /**初始化外层布局*/
        backView = (LinearLayout) parent.findViewById(R.id.background);

        /**获取外层布局的layout信息*/
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) backView.getLayoutParams();

        /**修改宽度为屏幕宽度的0.66倍*/
        layoutParams.width = (int) (Utils.getScreenWidth(getContext())*0.7);

        /**修改高度为屏幕高度的0.66倍*/
        layoutParams.height = (int) (Utils.getScreenHeight(getContext())*0.6);

        /**设置回layout信息*/
        backView.setLayoutParams(layoutParams);

        /**填充布局到Dialog*/
        this.setContentView(parent);

        /**初始化控件*/
        hint = (TextView) parent.findViewById(R.id.hint);
        confirm = (TextView) parent.findViewById(R.id.confirm);
        back = (TextView) parent.findViewById(R.id.back);
        topBar = (LinearLayout) parent.findViewById(R.id.topbar);
        final ListView listView = (ListView) parent.findViewById(R.id.citylist);

        /**设置返回事件监听*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        /**设置适配器*/
        listView.setAdapter(adapter);
        /**设置确认事件*/
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.getResult(bean1,bean2,bean3,bean4);
                }
                dismiss();
            }
        });

        /**设置地址选择事件*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                itemSelect((T) adapter.getItem(position));
                if (listener!=null){
                    listener.setPickData((T) adapter.getItem(position));
                }
            }
        });

        /**设置点击Dialog外的界面无法取消*/
        setCanceledOnTouchOutside(false);

        /**点击返回键可以退出*/
        setCancelable(true);
    }

    /**
     * 设置地区选择
     * @param bean 选中的Item
     */
    private void itemSelect(T bean){
        if (bean.getType() == RegionType.First){
            bean1 = bean;
            initResutData(3);
        }
        if (bean.getType() == RegionType.Second){
            bean2 = bean;
            initResutData(2);
        }
        if (bean.getType() == RegionType.Third){
            bean3 = bean;
            initResutData(1);
        }
        if (bean.getType() == RegionType.Fourth){
            bean4 = bean;
        }
        initHint();
    }

    /**
     * 设置提示信息
     */
    private void initHint(){
     getReactContext().runOnUiQueueThread(new Runnable() {
         @Override
         public void run() {
             if (bean1 == null && bean2 == null && bean3 == null && bean4 ==null){
                 hint.setText("当前未选择");
                 return;
             }

             hint.setText(bean1==null?"":bean1.getPickerName()
                     +" "+(bean2==null?"":bean2.getPickerName())
                     +" "+(bean3==null?"":bean3.getPickerName())
                     +" "+(bean4==null?"":bean4.getPickerName()));
         }
     });
    }

    /**
     * 初始化选定地区，防止数据错乱
     * @param index 初始化标记
     */
    private void initResutData(int index){
        if (index>=4) bean1=null;
        if (index>=3) bean2=null;
        if (index>=2) bean3=null;
        if (index>=1) bean4=null;
    }

    /**
     * 返回
     */
    private void back(){
        getReactContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                int type = adapter.getItem(0).getType();
                switch (type){
                    case RegionType.First:
                        DistrictSelectorView.this.dismiss();
                        break;
                    case RegionType.Second:
                        initResutData(3);
                        listener.setPickData(null);
                        break;
                    case RegionType.Third:
                        initResutData(2);
                        listener.setPickData(bean1);
                        break;
                    case RegionType.Fourth:
                        initResutData(1);
                        listener.setPickData(bean2);
                        break;
                }
                initHint();
            }
        });
    }

    /**
     * 显示Dialog
     * @param data 数据源
     */
    public void show(ArrayList<T> data){
        if (!(isShowing())&&data!=null&&adapter!=null){
            initResutData(4);
            initHint();
            dataSouce = data;
            adapter.setData(dataSouce);
            super.show();
            ScaleAnimation sAnima = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );//横向放大5倍，纵向放大5倍
            sAnima.setDuration(500);
            parent.startAnimation(sAnima);
        }else{
            Utils.toastL(getContext(),"DistrictSelectorView配置出错，请检查参数！");
        }
    }

    private ReactApplicationContext getReactContext(){
        return context;
    }

    public void show(){
        if (!isShowing()){
            initResutData(4);
            initHint();
            listener.setPickData(null);
            getReactContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    showSelf();
                    ScaleAnimation sAnima = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );//横向放大5倍，纵向放大5倍
                    sAnima.setDuration(500);
                    parent.startAnimation(sAnima);
                }
            });
        }else{
            Utils.toastL(getContext(),"DistrictSelectorView配置出错，请检查参数！");
        }
    }

    private void showSelf(){
        context.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DistrictSelectorView.super.show();
            }
        });
    }

    /**
     * 销毁
     */
    @Override
    public void dismiss() {
        getReactContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                ScaleAnimation sAnima = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );//横向放大5倍，纵向放大5倍
                sAnima.setDuration(500);
                sAnima.setFillAfter(true);
                sAnima.setFillBefore(false);
                sAnima.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dismissSelf();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                parent.startAnimation(sAnima);
            }
        });
    }

    private  void  dismissSelf(){
        context.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DistrictSelectorView.super.dismiss();
            }
        });
    }

    /**
     * 监听事件
     * @param <T> 数据类
     */
    public interface RegionListener<T extends BaseRagionModel>{
            void setPickData(T previousData);
            void getResult(T per, T city, T region, T town);
    }
}
