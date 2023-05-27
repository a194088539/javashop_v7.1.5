package com.enation.app.javashop.core.support;

import com.enation.app.javashop.framework.profile.ProfileParse;
import com.enation.app.javashop.framework.util.FileUtil;
import org.apache.commons.collections.map.HashedMap;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：liuyulei
 * @date ：Created in 2019/9/11 16:21
 * @description：流程控制XML文件解析
 * @version: v1.0
 * @since: v7.1.4
 */
@Component
public class XmlProfileParse implements ProfileParse {

    private Logger logger = LoggerFactory.getLogger(XmlProfileParse.class);

    private static List<String> paths = new ArrayList<>();

    private static Map<String,Object> flowMap = new HashedMap();


    static{
        paths.add("file:after_sale_flow.xml");
        paths.add("file:order_flow.xml");
    }

    @Override
    public void parseProfile() {
        for (String path:paths) {
            this.parse(path);
            if(logger.isDebugEnabled()){
                logger.debug("解析文件："+path);
            }

        }
    }

    /**
     * 解析xml文件
     * @param filePath
     */
    private void parse(String filePath) {

        //加载流程 xml文档
        Document xmlDoc = this.loadXml(filePath);
        Map<String,Map<String,List<String>>> result = new HashedMap();
        //解析数据
        this.readyFlow(xmlDoc.getRootElement().elements(),result);

        //将数据放入缓存中
        for (String key:result.keySet()) {
            flowMap.put(key,result.get(key));
        }
    }


    /**
     * 递归整合数据
     * @param flows
     * @return
     */
    private Map readyFlow(List<Element> flows,Map<String,Map<String,List<String>>> result ){

        Map map = new HashedMap();
        for (Element element:flows) {
            String key = element.attributeValue("name");
            List<Element> allows = element.elements("allow");
            List<String> allowOperate = new ArrayList<>();
            if(allows != null && allows.size() > 0){
                    allows.forEach(el -> {
                        allowOperate.add(el.getTextTrim());
                    });
                map.put(key,allowOperate);
                continue;
            }
            if(element.elements().size() > 0){
                Map status = readyFlow(element.elements(),result);
                result.put(key,status);
            }


        }
        return map;

    }





    /**
     * 加载xml文件
     * @param filePath
     * @return
     */
    private Document loadXml(String filePath){
        Document xmlDoc = null;
        try {

            if (filePath.startsWith("file:")) {
                filePath = FileUtil.readFile(filePath.replaceAll("file:", ""));
                xmlDoc = DocumentHelper.parseText(filePath);
            } else if (filePath.startsWith("<?xml version")) {
                xmlDoc = DocumentHelper.parseText(filePath);
            }
            else {
                SAXReader saxReader = new SAXReader();
                File file = new File(filePath);
                if (file.exists()){
                    xmlDoc = saxReader.read(new File(filePath));
                }
            }
        } catch (Exception e) {
            logger.error("xml加载失败",e);
        }

        return xmlDoc;
    }


    public static Map<String,List<String>> getFlowMap(String flow) {
        return (Map<String, List<String>>) flowMap.get(flow);
    }
}
