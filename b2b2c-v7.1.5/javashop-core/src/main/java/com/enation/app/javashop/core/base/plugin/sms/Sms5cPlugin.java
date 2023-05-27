//package com.enation.app.javashop.core.base.plugin.sms;
//
//import com.enation.app.javashop.core.base.model.vo.ConfigItem;
//import com.enation.app.javashop.framework.logs.Debugger;
//import com.enation.app.javashop.framework.util.HttpUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
///**
// *
// * 5c短信组件
// * @author zh
// * @version v1.0
// * @since v1.0
// * 2018年3月25日 下午2:40:54
// */
//@Component
//public class Sms5cPlugin implements SmsPlatform {
//
//
//	@Autowired
//	private Debugger debugger;
//
//	@Override
//	public boolean onSend(String phone, String content,Map param) {
//		try {
//			debugger.log("调起Sms5cPlugin","参数为：",param.toString());
//
//			// 创建StringBuffer对象用来操作字符串
//			StringBuffer sb = new StringBuffer("http://m.5c.com.cn/api/add/?");
//
//			// APIKEY
//			sb.append("apikey=" + param.get("apikey"));
//
//			// 用户名
//			sb.append("&username=" + param.get("username"));
//
//			// 向StringBuffer追加密码
//			sb.append("&password=" + param.get("password"));
//
//			// 向StringBuffer追加手机号码
//			sb.append("&mobile=" + phone);
//
//			// 向StringBuffer追加消息内容转URL标准码
//			sb.append("&context=" + URLEncoder.encode(content, "GBK"));
//
//			debugger.log("向5c发出请求，请求url为：",sb.toString());
//
//			// 返回发送结果
//			String result =  HttpUtils.doPost(sb.toString(),null);
//
//			debugger.log("收到返回结果：",result);
//
//			if(!result.startsWith("success")){
//				throw new RuntimeException(result);
//			}else{
//				return true;
//			}
//
//		} catch (Exception e) {
//
//		}
//		return false;
//	}
//
//	@Override
//	public String getPluginId() {
//		return "sms5cPlugin";
//	}
//
//	@Override
//	public String getPluginName() {
//		return "5c网关短信";
//	}
//
//	@Override
//	public List<ConfigItem> definitionConfigItem() {
//		List<ConfigItem> list = new ArrayList();
//
//		ConfigItem apike = new ConfigItem();
//		apike.setType("text");
//		apike.setName("apike");
//		apike.setText("APIKEY");
//
//		ConfigItem name = new ConfigItem();
//		name.setType("text");
//		name.setName("name");
//		name.setText("用户名");
//
//		ConfigItem password = new ConfigItem();
//		password.setType("text");
//		password.setName("password");
//		password.setText("密码");
//
//		list.add(apike);
//		list.add(name);
//		list.add(password);
//
//		return list;
//	}
//
//	@Override
//	public Integer getIsOpen() {
//		return 0;
//	}
//
//}
