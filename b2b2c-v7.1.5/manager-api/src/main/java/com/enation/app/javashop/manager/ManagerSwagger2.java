package com.enation.app.javashop.manager;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.framework.swagger.AbstractSwagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingapex on 2018/3/10.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/10
 */

@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class ManagerSwagger2 extends AbstractSwagger2 {

    @Bean
    public Docket distributionApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("分销")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/distribution/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket baseApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("基础")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/index/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket afterSaleApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("售后")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/after-sales/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket goodsApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("商品")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/goods/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket goodsSearchApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("商品搜索设置")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/goodssearch/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket membersApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("会员")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/members/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket orderBillApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("结算")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/order/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket pageCreateApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("静态页")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/page-create/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket pageDataApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("楼层")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.regex("(/admin/focus-pictures.*)|(/admin/pages.*)"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket paymentApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("支付")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/payment/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket promotionApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("促销")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/promotion/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket shopApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("店铺")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/shops/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket statisticsApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("统计")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.regex("(/admin/statistics.*)|(/admin/index/page.*)"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket systemApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("系统设置")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.regex("(/admin/task.*)|(/admin/systems.*)|(/admin/settings.*)|(/admin/sensitive-words.*)"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    @Bean
    public Docket tradeApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("交易")
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.enation.app.javashop.manager.api"))
                .paths(PathSelectors.ant("/admin/trade/**"))
                .build().globalOperationParameters(this.buildParameter()).directModelSubstitute(Region.class, Integer.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("管理中心Api文档")
                .description("管理中心API接口")
                .version("7.0")
                .contact(new Contact("Javashop", "http://www.javamall.com.cn", "service@javashop.cn"))
                .build();
    }
}
