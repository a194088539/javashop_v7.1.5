package com.enation.app.javashop.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by kingapex on 2018/3/18.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/18
 */
@SpringBootApplication
@ComponentScan("com.enation.app.javashop")
public class BaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApiApplication.class, args);
    }
}
