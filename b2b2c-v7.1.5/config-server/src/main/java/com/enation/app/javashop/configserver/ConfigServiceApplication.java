package com.enation.app.javashop.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created by kingapex on 2018/3/9.
 * 配置中心应用
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/9
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}

