package com.enation.app.javashop.manager.api.payment;

import com.enation.app.javashop.framework.exception.ErrorMessage;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.util.JsonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author fk
 * @version v2.0
 * @Description: 支付方式单元测试
 * @date 2018/4/24 17:11
 * @since v7.0.0
 */
@Transactional(value = "tradeTransactionManager",propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class PaymentMethodControllerTest extends BaseTest{

    /**
     * 400状态的list
     */
    List<MultiValueMap<String, String>> list1 = null;

    /**
     * 500状态的list
     */
    List<MultiValueMap<String, String>> list2 = null;

    @Before
    public void insertTestData(){

        String[] names = new String[]{"image","is_retrace","enable_client","message"};

        String client = "[\n" +
                "    {\n" +
                "      \"key\": \"pc_config&wap_config\",\n" +
                "      \"name\": \"是否开启PC和WAP\",\n" +
                "      \"config_list\": [\n" +
                "        {\n" +
                "          \n" +
                "      \n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"appid\",\n" +
                "          \"text\": \"APPID\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"key\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"app_secret\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"is_open\": 1\n" +
                "    }]";

        String[] values1 = new String[]{"","",client,"请选择是否支持原路退回"};
        String[] values2 = new String[]{"","3",client,"是否支持原路退回值不正确"};
        String[] values3 = new String[]{"","1","","客户端开启情况不能为空"};
        String[] values4 = new String[]{"","1","[{\"key\": \"\",\"name\": \"是否开启PC和WAP\",\"config_list\": [{},{ \"name\": \"appid\", \"text\": \"APPID\", \"value\": \"1\" }, { \"name\": \"key\", \"text\":  \"商户私钥\", \"value\": \"1\" }, { \"name\": \"app_secret\", \"text\": \"商户私钥\", \"value\": \"1\" } ], \"is_open\": 1 }]","客户端key不能为空"};
        String[] values5 = new String[]{"","1","[{\"key\": \"pc_config&wap_config\",\"name\": \"是否开启PC和WAP\",\"config_list\": [{},{ \"name\": \"appid\", \"text\": \"APPID\", \"value\": \"1\" }, { \"name\": \"key\", \"text\":  \"商户私钥\", \"value\": \"1\" }, { \"name\": \"app_secret\", \"text\": \"商户私钥\", \"value\": \"1\" } ]}]","是否开启某客户端不能为空"};
        String[] values6 = new String[]{"","1","[{\"key\": \"pc_config&wap_config\",\"name\": \"是否开启PC和WAP\",\"config_list\": [{},{ \"name\": \"appid\", \"text\": \"APPID\", \"value\": \"1\" }, { \"name\": \"key\", \"text\":  \"商户私钥\", \"value\": \"1\" }, { \"name\": \"app_secret\", \"text\": \"商户私钥\", \"value\": \"1\" } ], \"is_open\": 3 }]","是否开启某客户端值不正确"};
        //以上是400状态
        list1 = toMultiValueMaps(names,values1,values2,values3,values4,values5,values6);
        //以下是500状态
        String[] values7 = new String[]{"","1","[{\"key\": \"pc_config&wap_config\",\"name\": \"是否开启PC和WAP\",\"config_list\": [{\"name\": \"mchid\",\"text\": \"商户号MCHID\",\"value\": \"1\"},{ \"name\": \"appid\", \"text\": \"APPID\", \"value\": \"1\" }, { \"name\": \"key\", \"text\":  \"商户私钥\", \"value\": \"1\" }, { \"name\": \"app_secret\", \"text\": \"商户私钥\", \"value\": \"1\" },{\"name\": \"p12_path\",\"text\": \"商户私钥\",\"value\": \"1\"} ], \"is_open\": 1 }]","缺少是否开启APP原生相关配置"};
        String[] values8 = new String[]{"","1","[\n" +
                "    {\n" +
                "      \"key\": \"pc_config&wap_config\",\n" +
                "      \"name\": \"是否开启PC和WAP\",\n" +
                "      \"is_open\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"key\": \"app_native_config\",\n" +
                "      \"name\": \"是否开启APP原生\",\n" +
                "      \"config_list\": [\n" +
                "        {\n" +
                "          \"name\": \"mchid\",\n" +
                "          \"text\": \"商户号MCHID\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"appid\",\n" +
                "          \"text\": \"APPID\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"key\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"app_secret\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"p12_path\",\n" +
                "          \"text\": \"证书路径\",\n" +
                "          \"value\": null\n" +
                "        }\n" +
                "      ],\n" +
                "      \"is_open\": 0\n" +
                "    },\n" +
                "    {\n" +
                "      \"key\": \"app_react_config\",\n" +
                "      \"name\": \"是否开启APP-RN\",\n" +
                "      \"config_list\": [\n" +
                "        {\n" +
                "          \"name\": \"mchid\",\n" +
                "          \"text\": \"商户号MCHID\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"appid\",\n" +
                "          \"text\": \"APPID\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"key\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"app_secret\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"p12_path\",\n" +
                "          \"text\": \"证书路径\",\n" +
                "          \"value\": \"1\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"is_open\": 1\n" +
                "    }\n" +
                "  ]","是否开启PC和WAP的配置不能为空"};
        String[] value9 = new String[]{"","1","[\n" +
                "    {\n" +
                "      \"key\": \"pc_config&wap_config\",\n" +
                "      \"name\": \"是否开启PC和WAP\",\n" +
                "      \"config_list\": [\n" +
                "        {\n" +
                "          \n" +
                "      \n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"appid\",\n" +
                "          \"text\": \"APPID\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"key\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"app_secret\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"is_open\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"key\": \"app_native_config\",\n" +
                "      \"name\": \"是否开启APP原生\",\n" +
                "      \"config_list\": [\n" +
                "        {\n" +
                "          \"name\": \"mchid\",\n" +
                "          \"text\": \"商户号MCHID\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"appid\",\n" +
                "          \"text\": \"APPID\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"key\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"app_secret\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": null\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"p12_path\",\n" +
                "          \"text\": \"证书路径\",\n" +
                "          \"value\": null\n" +
                "        }\n" +
                "      ],\n" +
                "      \"is_open\": 0\n" +
                "    },\n" +
                "    {\n" +
                "      \"key\": \"app_react_config\",\n" +
                "      \"name\": \"是否开启APP-RN\",\n" +
                "      \"config_list\": [\n" +
                "        {\n" +
                "          \"name\": \"mchid\",\n" +
                "          \"text\": \"商户号MCHID\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"appid\",\n" +
                "          \"text\": \"APPID\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"key\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"app_secret\",\n" +
                "          \"text\": \"商户私钥\",\n" +
                "          \"value\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"p12_path\",\n" +
                "          \"text\": \"证书路径\",\n" +
                "          \"value\": \"1\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"is_open\": 1\n" +
                "    }\n" +
                "  ]","是否开启PC和WAP的商户号MCHID必填"};
        list2 = toMultiValueMaps(names,values7,values8,value9);

    }

    /**
     * 查询支付方式
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception{

        //状态200
        mockMvc.perform(get("/admin/payment/payment-methods")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }

    /**
     * 查询支付方式
     * @throws Exception
     */
    @Test
    public void testQueryOne() throws Exception{

        ErrorMessage error  = new ErrorMessage("501","支付方式不存在");

        //不存在的插件id
        mockMvc.perform(get("/admin/payment/payment-methods/weixinPayPlugin1")
                .header("Authorization",superAdmin))
                .andExpect(status().is(500))
                .andExpect( objectEquals( error ) );

        //状态200
        mockMvc.perform(get("/admin/payment/payment-methods/weixinPayPlugin")
                .header("Authorization",superAdmin))
                .andExpect(status().is(200));

    }

    /**
     * 修改支付方式
     * @throws Exception
     */
    @Test
    public void testEdit() throws Exception{

        // 必填验证和不正确的值
        for (MultiValueMap<String,String> params  : list1){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("004",message);
            Map map = new HashMap<>();
            for (String key : params.keySet()){

                map.put(key,params.get(key).get(0));
            }
            if("".equals(params.get("enable_client").get(0))){
                map.put("enable_client", null);
            }else{
                map.put("enable_client", JSONArray.fromObject(params.get("enable_client").get(0)));
            }

            mockMvc.perform(put("/admin/payment/payment-methods/weixinPayPlugin")
                    .header("Authorization",superAdmin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.objectToJson(map)))
                    .andExpect(status().is(400))
                    .andExpect(  objectEquals( error ));
        }

        //程序中的不能为空错误值等
        for (MultiValueMap<String,String> params  : list2){
            String message =  params.get("message").get(0);
            ErrorMessage error  = new ErrorMessage("501",message);
            Map map = new HashMap<>();
            for (String key : params.keySet()){

                map.put(key,params.get(key).get(0));
            }
            if("".equals(params.get("enable_client").get(0))){
                map.put("enable_client", null);
            }else{
                map.put("enable_client", JSONArray.fromObject(params.get("enable_client").get(0)));
            }

            mockMvc.perform(put("/admin/payment/payment-methods/weixinPayPlugin")
                    .header("Authorization",superAdmin)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONObject.fromObject(map).toString()))
                    .andExpect(status().is(500))
                    .andExpect(  objectEquals( error ));
        }

        // 不存在的支付方式id
        ErrorMessage error  = new ErrorMessage("501","插件id不正确");
        mockMvc.perform(put("/admin/payment/payment-methods/weixinPayPlugin1")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"enable_client\": [\n" +
                        "    {\n" +
                        "      \"key\": \"pc_config&wap_config\",\n" +
                        "      \"name\": \"是否开启PC和WAP\",\n" +
                        "      \"config_list\": [\n" +
                        "        {\n" +
                        "          \n" +
                        "      \n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"appid\",\n" +
                        "          \"text\": \"APPID\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"key\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"app_secret\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"is_open\": 1\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"key\": \"app_native_config\",\n" +
                        "      \"name\": \"是否开启APP原生\",\n" +
                        "      \"config_list\": [\n" +
                        "        {\n" +
                        "          \"name\": \"mchid\",\n" +
                        "          \"text\": \"商户号MCHID\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"appid\",\n" +
                        "          \"text\": \"APPID\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"key\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"app_secret\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"p12_path\",\n" +
                        "          \"text\": \"证书路径\",\n" +
                        "          \"value\": null\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"is_open\": 0\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"key\": \"app_react_config\",\n" +
                        "      \"name\": \"是否开启APP-RN\",\n" +
                        "      \"config_list\": [\n" +
                        "        {\n" +
                        "          \"name\": \"mchid\",\n" +
                        "          \"text\": \"商户号MCHID\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"appid\",\n" +
                        "          \"text\": \"APPID\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"key\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"app_secret\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"p12_path\",\n" +
                        "          \"text\": \"证书路径\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"is_open\": 1\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"image\": \"string\",\n" +
                        "  \"is_retrace\": 0\n" +
                        "}"))
                .andExpect(status().is(500))
                .andExpect(  objectEquals( error ));

        // 正确添加
        mockMvc.perform(put("/admin/payment/payment-methods/weixinPayPlugin")
                .header("Authorization",superAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"enable_client\": [\n" +
                        "    {\n" +
                        "      \"key\": \"pc_config&wap_config\",\n" +
                        "      \"name\": \"是否开启PC和WAP\",\n" +
                        "      \"config_list\": [\n" +
                        "        {\n" +
                        "          \"name\": \"mchid\",\n" +
                        "          \"text\": \"商户号MCHID\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"appid\",\n" +
                        "          \"text\": \"APPID\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"key\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"app_secret\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"p12_path\",\n" +
                        "          \"text\": \"证书路径\",\n" +
                        "          \"value\": \"123\"\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"is_open\": 1\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"key\": \"app_native_config\",\n" +
                        "      \"name\": \"是否开启APP原生\",\n" +
                        "      \"config_list\": [\n" +
                        "        {\n" +
                        "          \"name\": \"mchid\",\n" +
                        "          \"text\": \"商户号MCHID\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"appid\",\n" +
                        "          \"text\": \"APPID\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"key\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"app_secret\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"p12_path\",\n" +
                        "          \"text\": \"证书路径\",\n" +
                        "          \"value\": null\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"is_open\": 0\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"key\": \"app_react_config\",\n" +
                        "      \"name\": \"是否开启APP-RN\",\n" +
                        "      \"config_list\": [\n" +
                        "        {\n" +
                        "          \"name\": \"mchid\",\n" +
                        "          \"text\": \"商户号MCHID\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"appid\",\n" +
                        "          \"text\": \"APPID\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"key\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"app_secret\",\n" +
                        "          \"text\": \"商户私钥\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"p12_path\",\n" +
                        "          \"text\": \"证书路径\",\n" +
                        "          \"value\": \"1\"\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"is_open\": 1\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"image\": \"string\",\n" +
                        "  \"is_retrace\": 0\n" +
                        "}"))
                .andExpect(status().is(200));
    }

}
