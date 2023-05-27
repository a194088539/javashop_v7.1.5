package com.enation.app.javashop.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by kingapex on 2018/3/8.
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4.0
 * 2018/3/8
 */
@SpringBootApplication
@ComponentScan("com.enation.app.javashop")
@EnableTransactionManagement
@ServletComponentScan
public class ManagerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApiApplication.class, args);
    }

}
