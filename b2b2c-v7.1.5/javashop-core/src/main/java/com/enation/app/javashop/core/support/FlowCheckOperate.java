package com.enation.app.javashop.core.support;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ：liuyulei
 * @date ：Created in 2019/9/15 12:47
 * @description：流程设置业务实现
 * @version: v1.0
 * @since: v7.1.4
 */
@Service
public class FlowCheckOperate {

    /**
     *  校验是否允许操作
     * @param type  服务类型
     * @param currentStatus   当前状态
     * @param operate   正在进行的操作
     * @return true 校验通过   false 校验不通过
     */
    public static Boolean checkOperate(String type, String currentStatus, String operate) {
        Map<String, List<String>> status = getFlow(type);

        if(status == null || status.isEmpty()){
           return false;
        }

        List<String> allow =  status.get(currentStatus);
        if(allow != null && allow.contains(operate)){
            return true;
        }
        return false;

    }

    private static Map<String, List<String>> getFlow(String type) {
        return XmlProfileParse.getFlowMap(type);
    }
}
