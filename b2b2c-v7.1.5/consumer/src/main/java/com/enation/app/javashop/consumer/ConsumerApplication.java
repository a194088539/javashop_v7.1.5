package com.enation.app.javashop.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by kingapex on 2018/3/10.
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4.0
 * 2018/3/10
 */
@SpringBootApplication
@ComponentScan("com.enation.app.javashop")
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}