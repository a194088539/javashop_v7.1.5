package com.enation.app.javashop.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by kingapex on 2018/3/21.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/21
 */
@SpringBootApplication
@ComponentScan("com.enation.app.javashop")
public class JavashopApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavashopApplication.class, args);
    }

}
