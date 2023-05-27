package com.enation.app.javashop.seller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by kingapex on 2018/3/10.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/10
 */
@SpringBootApplication
@ComponentScan({"com.enation.app.javashop","com.chinapay.secss.util", "com.alipay.api.util"})
@ServletComponentScan
public class SellerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellerApiApplication.class, args);
    }

}
