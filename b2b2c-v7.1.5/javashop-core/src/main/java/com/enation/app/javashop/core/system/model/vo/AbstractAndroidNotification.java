package com.enation.app.javashop.core.system.model.vo;

import java.util.Arrays;
import java.util.HashSet;

import com.enation.app.javashop.core.system.enums.AfterOpenEnum;
import com.enation.app.javashop.core.system.enums.DisplayTypeEnum;
import net.sf.json.JSONObject;

/**
 * Android推送Model基类
 * 拼接Android推送需要的字符串
 *
 * @author zh
 * @version v7.0
 * @date 18/5/30 下午7:25
 * @since v7.0
 */
public abstract class AbstractAndroidNotification extends AbstractUmengNotification {

    /**
     * 发送策略字段组
     */
    protected static final HashSet<String> PAYLOAD_KEYS = new HashSet<String>(Arrays.asList("display_type"));

    /**
     * Body字段组
     */
    protected static final HashSet<String> BODY_KEYS = new HashSet<String>(Arrays.asList("ticker", "title", "text", "builder_id", "icon", "largeIcon", "img", "play_vibrate", "play_lights", "play_sound",
            "sound", "after_open", "url", "activity", "custom"));

    /**
     * 设置字段值
     *
     * @param key   索引
     * @param value 值
     * @return 是否添加成功
     * @author LDD
     * @since v6.4
     */
    @Override
    public boolean setPredefinedKeyValue(String key, Object value) throws Exception {
        //如果是根部的字段，直接写入
        if (ROOT_KEYS.contains(key)) {
            rootJson.put(key, value);
        } else if (PAYLOAD_KEYS.contains(key)) {
            JSONObject payloadJson = null;
            //判断rootJson中是否有payload，有则取出没有则创建
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            payloadJson.put(key, value);
        } else if (BODY_KEYS.contains(key)) {
            JSONObject bodyJson = null;
            JSONObject payloadJson = null;
            //判断rootJson中是否有payload，有则取出没有则创建
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
            }
            //判断rootJson中是否有body，有则取出没有则创建
            if (payloadJson.has("body")) {
                bodyJson = payloadJson.getJSONObject("body");
                bodyJson.put(key, value);
                payloadJson.put("body", bodyJson);
            } else {
                bodyJson = new JSONObject();
                bodyJson.put(key, value);
                payloadJson.put("body", bodyJson);
            }
            rootJson.put("payload", payloadJson);
            //判断是否是发错策略组中的字段
        } else if (POLICY_KEYS.contains(key)) {
            JSONObject policyJson = null;
            if (rootJson.has("policy")) {
                policyJson = rootJson.getJSONObject("policy");
            } else {
                policyJson = new JSONObject();
                rootJson.put("policy", policyJson);
            }
            policyJson.put(key, value);
        } else {
            //如果属于以下字段 抛出空值异常 如果不输入抛出无效key异常
            if (key == "payload" || key == "body" || key == "policy" || key == "extra") {
                throw new Exception("You don't need to set value for " + key + " , just set values for the sub keys in it.");
            } else {
                throw new Exception("Unknown key: " + key);
            }
        }
        return true;
    }

    /**
     * 设置自定义值
     *
     * @param key   索引
     * @param value 值
     * @return 是否添加成功
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public boolean setExtraField(String key, String value) throws Exception {
        JSONObject payloadJson = null;
        JSONObject extraJson = null;
        if (rootJson.has("payload")) {
            payloadJson = rootJson.getJSONObject("payload");
        } else {
            payloadJson = new JSONObject();
            rootJson.put("payload", payloadJson);
        }

        if (payloadJson.has("extra")) {
            extraJson = payloadJson.getJSONObject("extra");
            extraJson.put(key, value);
            payloadJson.put("extra", extraJson);
        } else {
            extraJson = new JSONObject();
            extraJson.put(key, value);
            payloadJson.put("extra", extraJson);
        }
        return true;
    }

    /**
     * 设置消息类型
     *
     * @param displayType 消息类型
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setDisplayType(DisplayTypeEnum displayType) throws Exception {
        setPredefinedKeyValue("display_type", displayType.getValue());
    }

    /**
     * 设置通知栏提示文字
     *
     * @param ticker 通知栏提示文字
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setTicker(String ticker) throws Exception {
        setPredefinedKeyValue("ticker", ticker);
    }

    /**
     * 设置通知标题
     *
     * @param title 通知标题
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setTitle(String title) throws Exception {
        setPredefinedKeyValue("title", title);
    }

    /**
     * 设置通知文字描述
     *
     * @param text 通知文字描述
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setText(String text) throws Exception {
        setPredefinedKeyValue("text", text);
    }

    /**
     * 用于标识该通知采用的样式。使用该参数时, 必须在AndroidSDK里面实现自定义通知栏样式。
     *
     * @param builderId 样式id
     * @throws Exception 空值异常
     */
    public void setBuilderId(Integer builderId) throws Exception {
        setPredefinedKeyValue("builder_id", builderId);
    }

    /**
     * 状态栏图标ID, R.drawable.[smallIcon],如果没有, 默认使用应用图标。
     *
     * @param icon 图标id
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setIcon(String icon) throws Exception {
        setPredefinedKeyValue("icon", icon);
    }

    /**
     * 通知栏拉开后左侧图标ID
     *
     * @param largeIcon 图标id
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setLargeIcon(String largeIcon) throws Exception {
        setPredefinedKeyValue("largeIcon", largeIcon);
    }

    /**
     * 通知栏大图标的URL链接。该字段的优先级大于largeIcon。该字段要求以http或者https开头
     *
     * @param img 图片链接
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setImg(String img) throws Exception {
        setPredefinedKeyValue("img", img);
    }

    /**
     * 收到通知是否震动,默认为"true"
     *
     * @param playVibrate 是否震动
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setPlayVibrate(Boolean playVibrate) throws Exception {
        setPredefinedKeyValue("play_vibrate", playVibrate.toString());
    }

    /**
     * 收到通知是否闪灯,默认为"true"
     *
     * @param playLights 是否闪灯
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setPlayLights(Boolean playLights) throws Exception {
        setPredefinedKeyValue("play_lights", playLights.toString());
    }

    /**
     * 收到通知是否发出声音,默认为"true"
     *
     * @param playSound 是否发出声音
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setPlaySound(Boolean playSound) throws Exception {
        setPredefinedKeyValue("play_sound", playSound.toString());
    }

    /**
     * 自定义通知声音，R.raw.[sound]. 如果该字段为空，采用SDK默认的声音
     *
     * @param sound 声音id
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setSound(String sound) throws Exception {
        setPredefinedKeyValue("sound", sound);
    }

    /**
     * app收到通知后播放指定的声音文件
     *
     * @param sound 文件路径
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setPlaySound(String sound) throws Exception {
        setPlaySound(true);
        setSound(sound);
    }

    /**
     * app点击"通知"的后续行为，默认为打开app。
     *
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void goAppAfterOpen() throws Exception {
        setAfterOpenAction(AfterOpenEnum.go_app);
    }

    /**
     * app点击通知后 打开制定链接
     *
     * @param url 链接url
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void goUrlAfterOpen(String url) throws Exception {
        setAfterOpenAction(AfterOpenEnum.go_url);
        setUrl(url);
    }

    /**
     * app点击通知后打开指定Activity
     *
     * @param activity activity全包名
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void goActivityAfterOpen(String activity) throws Exception {
        setAfterOpenAction(AfterOpenEnum.go_activity);
        setActivity(activity);
    }

    /**
     * 自定义点已通知后操作
     *
     * @param custom 自定义参数值
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void goCustomAfterOpen(String custom) throws Exception {
        setAfterOpenAction(AfterOpenEnum.go_custom);
        setCustomField(custom);
    }

    /**
     * 自定义点已通知后操作
     *
     * @param custom 自定义参数Jsonobject
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void goCustomAfterOpen(JSONObject custom) throws Exception {
        setAfterOpenAction(AfterOpenEnum.go_custom);
        setCustomField(custom);
    }

    /**
     * 点击"通知"的后续行为，默认为打开app。原始接口
     *
     * @param action 行为枚举参数
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setAfterOpenAction(AfterOpenEnum action) throws Exception {
        setPredefinedKeyValue("after_open", action.toString());
    }

    /**
     * 设置跳转url
     *
     * @param url 链接url
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setUrl(String url) throws Exception {
        setPredefinedKeyValue("url", url);
    }

    /**
     * 设置跳转Activity
     *
     * @param activity activity全报名
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setActivity(String activity) throws Exception {
        setPredefinedKeyValue("activity", activity);
    }

    /**
     * 设置自定义参数值
     *
     * @param custom 参数
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setCustomField(String custom) throws Exception {
        setPredefinedKeyValue("custom", custom);
    }

    /**
     * 设置自定义参数值 JsonObejct类型重写方法
     *
     * @param custom 参数
     * @throws Exception 空值异常
     * @author LDD
     * @since v6.4
     */
    public void setCustomField(JSONObject custom) throws Exception {
        setPredefinedKeyValue("custom", custom);
    }

}
