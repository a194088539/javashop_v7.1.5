package com.enation.app.javashop.buyer.api.debugger;

import com.enation.app.javashop.framework.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019/12/16
 */
@RestController
@RequestMapping("/debugger/validator")
public class Validator {

    @Autowired
    Config config;

    @GetMapping("/test")
    public String valid(HttpServletRequest request) {
        String domain = request.getServerName();
        String str = "";
        List<String> license = config.getLicense();
        for (String s : license) {
            str += "<br/>";

            Map map = new HashMap<>();
            map.put("license", s);
            map.put("domain", domain);

            String result = HttpUtils.doPost("http://crm.javamall.com.cn/license/valid", map);
            str += result;
        }
        return str;
    }

    @Configuration
    @ConfigurationProperties(prefix = "javashop")
    @SuppressWarnings("ConfigurationProperties")
    public class Config {
        private List<String> license = new ArrayList<String>();

        public List<String> getLicense() {
            return license;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
    }


}
