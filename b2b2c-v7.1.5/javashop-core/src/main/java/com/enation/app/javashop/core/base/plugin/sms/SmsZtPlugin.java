package com.enation.app.javashop.core.base.plugin.sms;

import com.enation.app.javashop.core.base.model.vo.ConfigItem;
import com.enation.app.javashop.framework.logs.Debugger;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.HttpUtils;
import com.enation.app.javashop.framework.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 助通短信 插件
 *
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2018年3月25日 下午2:42:20
 */
@Component
public class SmsZtPlugin implements SmsPlatform {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private Debugger debugger;

    @Override
    public boolean onSend(String phone, String content, Map param) {
        try {
            debugger.log("调起SmsZtPlugin","参数为：",param.toString());
            // 创建StringBuffer对象用来操作字符串
            StringBuffer sb = new StringBuffer("http://www.ztsms.cn/sendNSms.do?");

            // 用户名
            sb.append("username=" + param.get("name"));

            // 产品id
            sb.append("&productid=" + param.get("id"));

            // 包装密码
            String password = "";
            String md5Psd = SmsZtPlugin.string2MD5(param.get("password").toString());
            String time = DateUtil.toString(DateUtil.getDateline(), "yyyyMMddHHmmss");

            password = SmsZtPlugin.string2MD5(md5Psd + time);

            // 密码
            sb.append("&password=" + password);

            // tkey
            sb.append("&tkey=" + time);

            // 向StringBuffer追加手机号码
            sb.append("&mobile=" + phone);

            // 向StringBuffer追加消息内容转URL标准码
            sb.append("&content=" + URLEncoder.encode(content, "UTF-8"));

            String xh = StringUtil.toString(param.get("trumpet"));

            if (!StringUtil.isEmpty(xh)) {
                sb.append("&xh=" + xh);
            }

            debugger.log("向ztsms.cn发出请求，请求url为：",sb.toString());
            // 返回发送结果
            String result =  HttpUtils.doPost(sb.toString(),null);
            debugger.log("收到返回结果：",result);
            if (!result.startsWith("1,")) {
                throw new RuntimeException(result);
            } else {
                return true;
            }

        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }


    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }


    @Override
    public String getPluginId() {
        return "smsZtPlugin";
    }


    @Override
    public String getPluginName() {
        return "助通网关短信";
    }


    @Override
    public List<ConfigItem> definitionConfigItem() {
        List<ConfigItem> list = new ArrayList();


        ConfigItem name = new ConfigItem();
        name.setType("text");
        name.setName("name");
        name.setText("用户名");

        ConfigItem password = new ConfigItem();
        password.setType("text");
        password.setName("password");
        password.setText("密码");

        ConfigItem id = new ConfigItem();
        id.setType("text");
        id.setName("id");
        id.setText("产品id");

        ConfigItem trumpet = new ConfigItem();
        trumpet.setType("text");
        trumpet.setName("trumpet");
        trumpet.setText("扩展的小号（没有请留空）");

        list.add(name);
        list.add(password);
        list.add(id);
        list.add(trumpet);

        return list;
    }


    @Override
    public Integer getIsOpen() {
        return 0;
    }

}
