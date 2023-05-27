package com.enation.app.javashop.base;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author fk
 * @version v2.0
 * @Description: ${todo}
 * @date 2018/11/514:50
 * @since v7.0.0
 */
@RestController
@RequestMapping("/test")
@Api(description = "test")
public class TestRedisController {


    @Autowired
    private  MyTest myTest;

    @GetMapping("/get")
    public String get() {
        myTest.get();
        return "ok";
    }

    @GetMapping("/reset")
    public String rest() {
        myTest.reset();
        return "ok";
    }


    @GetMapping("/test1")
    public String test1() {
        myTest.test1();
        return "ok";
    }


    @GetMapping("/test2")
    public String test2() {
        myTest.test2();
        return "ok";
    }


    @GetMapping("/sku")
    public String sku() throws IOException {
        myTest.testSku();
        return "ok";
    }

    @GetMapping("/batch")
    public String batch() throws IOException {
        myTest.testBatch();
        return "ok";
    }

    @GetMapping("/valid")
    public String valid() {
        myTest.valid();
        return "ok";
    }




}
