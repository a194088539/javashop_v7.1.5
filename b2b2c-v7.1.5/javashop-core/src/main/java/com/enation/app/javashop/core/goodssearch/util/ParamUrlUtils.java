package com.enation.app.javashop.core.goodssearch.util;

import com.enation.app.javashop.core.goods.util.ParamsUtils;
import com.enation.app.javashop.core.goods.util.Separator;
import com.enation.app.javashop.core.goodssearch.model.SearchSelector;
import com.enation.app.javashop.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 属性url生成工具
 *
 * @author fk
 * 2017年6月30日18:01:50
 */
public class ParamUrlUtils {


    /**
     * 在原有的url基础上根据参数名和值生成新的属性url<br>
     * 如原url为 search.html?cat=1&prop=p1_1，生成新的url:search.html?cat=1&prop=p1_1@name_value
     *
     * @param name
     * @param value
     * @return
     */
    public static String createPropUrl(String name, String value) {
        Map<String, String> params = ParamsUtils.getReqParams();
        String param = params.get("prop");
        if (!StringUtil.isEmpty(param)) {
            param = param + Separator.SEPARATOR_PROP;
        } else {
            param = "";
        }
        param = param + name + Separator.SEPARATOR_PROP_VLAUE + value;
        params.put("prop", param);
        return ParamsUtils.paramsToUrlString(params);
    }

    /**
     * 获取已选择的属性维度
     *
     * @return
     */
    public static List<SearchSelector> getPropDimSelected(String prop) {

        List<SearchSelector> selectorList = new ArrayList();

        if(!StringUtil.isEmpty(prop)){
            String[] propAr = prop.split(Separator.SEPARATOR_PROP);
            for (String p : propAr) {
                String[] onpropAr = p.split(Separator.SEPARATOR_PROP_VLAUE);
                SearchSelector selector = new SearchSelector();
                String name = onpropAr[0];
                String value = onpropAr[1];
                selector.setName(name);
                selector.setValue(value);
                String url = createPropUrlWithoutSome(name, value);
                selector.setUrl(url);
                selectorList.add(selector);
            }
            return selectorList;
        }
        return new ArrayList<>();

    }

    /**
     * 排除某个属性的方式生成属性字串<br>
     * search.html?cat=1&prop=p1_1@p2_2 传入p2和2则返回search.html?cat=1&prop=p1_1<br>
     * 用于生成已经选择的selector的url
     *
     * @param name
     * @param value
     * @return
     */
    private static String createPropUrlWithoutSome(String name, String value) {
        Map<String, String> params = ParamsUtils.getReqParams();
        String prop = params.get("prop");
        if (!StringUtil.isEmpty(prop)) {
            prop = prop.replaceAll("(" + Separator.SEPARATOR_PROP + "?)" + name + Separator.SEPARATOR_PROP_VLAUE + value + "(" + Separator.SEPARATOR_PROP + "?)", "");
        } else {
            prop = "";
        }
        params.put("prop", prop);
        return ParamsUtils.paramsToUrlString(params);
    }

}
