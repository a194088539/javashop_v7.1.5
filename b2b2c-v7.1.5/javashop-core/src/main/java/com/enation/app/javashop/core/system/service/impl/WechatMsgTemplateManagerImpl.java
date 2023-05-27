package com.enation.app.javashop.core.system.service.impl;

import com.enation.app.javashop.core.payment.plugin.weixin.signaturer.WechatSignaturer;
import com.enation.app.javashop.core.payment.plugin.weixin.signaturer.WechatTypeEnmu;
import com.enation.app.javashop.core.system.SystemErrorCode;
import com.enation.app.javashop.core.system.enums.WechatMsgTemplateTypeEnum;
import com.enation.app.javashop.core.system.model.vo.WechatMsgData;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.HttpUtils;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.system.model.dos.WechatMsgTemplate;
import com.enation.app.javashop.core.system.service.WechatMsgTemplateManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信服务消息模板业务类
 *
 * @author fk
 * @version v7.1.4
 * @since vv7.1.0
 * 2019-06-14 16:42:35
 */
@Service
public class WechatMsgTemplateManagerImpl implements WechatMsgTemplateManager {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @Autowired
    private WechatSignaturer wechatSignaturer;

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public Page list(int page, int pageSize) {

        String sql = "select * from es_wechat_msg_template  ";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, WechatMsgTemplate.class);

        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public WechatMsgTemplate add(WechatMsgTemplate wechatMsgTemplate) {
        this.daoSupport.insert(wechatMsgTemplate);

        return wechatMsgTemplate;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public WechatMsgTemplate edit(WechatMsgTemplate wechatMsgTemplate, Integer id) {
        this.daoSupport.update(wechatMsgTemplate, id);
        return wechatMsgTemplate;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {

        //查询模板
        WechatMsgTemplate tmp = this.getModel(id);
        if (tmp == null) {
            return;
        }
        //获取tocken
        String tocken = wechatSignaturer.getCgiAccessToken(WechatTypeEnmu.WAP);
        //删除微信的模板
        String url = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=" + tocken;
        Map map = new HashMap();
        map.put("template_id", tmp.getTemplateId());
        //向微信添加相应编号模板
        String content = HttpUtils.doPostWithJson(url, map);
        if (logger.isDebugEnabled()) {
            logger.debug(content);
        }
        JSONObject json = JSONObject.fromObject(content);
        //删除成功
        if ("ok".equals(json.getString("errmsg"))) {
            this.daoSupport.delete(WechatMsgTemplate.class, id);
        }

    }

    @Override
    public WechatMsgTemplate getModel(Integer id) {
        return this.daoSupport.queryForObject(WechatMsgTemplate.class, id);
    }

    @Override
    public boolean sycn() {

        //获取tocken
        String tocken = wechatSignaturer.getCgiAccessToken(WechatTypeEnmu.WAP);
        checkWeChatTemplateNum(tocken);

        String url = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=" + tocken;
        //循环枚举
        WechatMsgTemplateTypeEnum[] values = WechatMsgTemplateTypeEnum.values();
        Map map = new HashMap<>();
        for (WechatMsgTemplateTypeEnum value : values) {

            map.put("template_id_short", value.getSn());
            //向微信添加相应编号模板
            String content = HttpUtils.doPostWithJson(url, map);
            if (logger.isDebugEnabled()) {
                logger.debug(content);
            }
            //获取模板id
            JSONObject json = JSONObject.fromObject(content);
            Object templateId = json.get("template_id");
            if (templateId == null) {
                throw new ServiceException(SystemErrorCode.E927.code(), "请在微信公众平台删除商城模板消息后，再次同步");
            }
            WechatMsgTemplate tmp = new WechatMsgTemplate();
            tmp.setIsOpen(1);
            tmp.setMsgTmpName(value.getTmpName());
            tmp.setMsgTmpSn(value.getSn());
            tmp.setTmpType(value.value());
            tmp.setTemplateId(templateId.toString());
            tmp.setMsgFirst(value.getFirst());
            tmp.setMsgRemark(value.getRemark());
            this.daoSupport.insert(tmp);
        }

        return true;
    }

    @Override
    public boolean isSycn() {

        //查询表中是否有数据，有数据则说明已经同步
        String sql = "select * from es_wechat_msg_template ";

        List list = this.daoSupport.queryForList(sql);

        if (StringUtil.isNotEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public void send(String openId, WechatMsgTemplateTypeEnum messageType, Object... keyword) {

        if(StringUtil.isEmpty(openId)){
            //为空，则不发送
            return;
        }
        //使用枚举查看模板
        String sql = "select * from es_wechat_msg_template where tmp_type = ?";
        WechatMsgTemplate template = this.daoSupport.queryForObject(sql, WechatMsgTemplate.class, messageType.value());
        //判断模板是否开启，开启则发消息
        if (template == null || template.getIsOpen().equals(0)) {
            return;
        }

        //获取tocken
        String tocken = wechatSignaturer.getCgiAccessToken(WechatTypeEnmu.WAP);
        //拼接发送内容
        WechatMsgData data = new WechatMsgData();
        data.first(template.getMsgFirst()).remark(template.getMsgRemark());
        for (Object key : keyword) {

            data.keywords(key.toString());
        }
        //发送url
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + tocken;

        Map map = new HashMap<>();
        map.put("touser", openId);
        map.put("template_id", template.getTemplateId());
//        map.put("url", "http://weixin.qq.com/download");
        map.put("data", JSONObject.fromObject(data.createData()));
        if (logger.isDebugEnabled()) {
            logger.debug(map.toString());
        }
        //向微信添加相应编号模板
        String content = HttpUtils.doPostWithJson(url, map);

        JSONObject json = JSONObject.fromObject(content);
        String errmsg = json.get("errmsg").toString();
        //发送失败
        if (!"ok".equals(errmsg) && logger.isDebugEnabled()) {
            logger.debug("发送消息错误，返回内容：" + content);
        }

    }


    /**
     * 检测模板数量
     * @param tocken
     */
    private void checkWeChatTemplateNum(String tocken ){
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=" + tocken;
        String content = HttpUtils.doPost(url, new HashMap<>());
        if(logger.isDebugEnabled()){
            logger.debug(content);
        }
        JSONObject json = JSONObject.fromObject(content);
        List<Map<String, Object>> list = JsonUtil.toList(json.getString("template_list"));

        //每个账号可以同时使用25个模板。平台使用7个模板  如果原有模板>=18条，则同步模板会异常
        if(StringUtil.isNotEmpty(list) && list.size() + 7 > 25){
            throw new ServiceException(SystemErrorCode.E927.code(), "微信公众号消息模板超出数量限制，请到微信公众号后台进行删除（每个微信公众号只能同时使用25个模板，请至少保留7个模板数量，平台使用7个模板）！");
        }
    }


}
