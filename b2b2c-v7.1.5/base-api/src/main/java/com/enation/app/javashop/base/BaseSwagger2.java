package com.enation.app.javashop.base;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.framework.swagger.AbstractSwagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by kingapex on 2018/3/10.
 * Swagger2配置
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/10
 */

@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class BaseSwagger2 extends AbstractSwagger2 {

}
