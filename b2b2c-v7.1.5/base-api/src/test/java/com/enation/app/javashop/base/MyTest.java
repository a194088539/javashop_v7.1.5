package com.enation.app.javashop.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingapex on 2018/12/11.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/12/11
 */
@SuppressWarnings("ALL")
@Service
public class MyTest {

    @Autowired
    public RedisTemplate redisTemplate;


    @Autowired
    public StringRedisTemplate  stringRedisTemplate;

    public void test1(){
//        stringRedisTemplate.opsForValue().set("test",0);
//        ScriptSource scriptSource =   new StaticScriptSource("eval 'local value = redis.call(\"get\",\"test\"); value=value+1; redis.call(\"set\",\"test\",value); return value;'");
        redisTemplate.execute( RedisScript.of("local value = redis.call(\"get\",\"test\"); value=value+1; redis.call(\"set\",\"test\",value); return value;",Integer.class),new ArrayList());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public  void get(){
        Integer value  = (Integer)redisTemplate.opsForValue().get("test");
        System.out.println(value);

    }



    public void test2(){
        Integer value  = (Integer)redisTemplate.opsForValue().get("test");

        value++;
        redisTemplate.setValueSerializer( new JdkSerializationRedisSerializer());
        redisTemplate.opsForValue().set("test",value);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testSku() throws IOException {
        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("sku_quantity.lua"));
        String str =scriptSource.getScriptAsString();

        RedisScript<Boolean> redisScript = RedisScript.of(str, Boolean.class);
        List keys  =  new ArrayList<>();
        keys.add("sku_quantity_1");
        Boolean result  =(Boolean) redisTemplate.execute(redisScript,new StringRedisSerializer(),new StringRedisSerializer(),keys,"1");

        if (!result) {
            System.out.println(result);
        }


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void testBatch() throws IOException {
        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("sku_quantity.lua"));
        String str =scriptSource.getScriptAsString();

        RedisScript<Boolean> redisScript = RedisScript.of(str, Boolean.class);

        List keys = new ArrayList<>();
        List values =  new ArrayList<>();

        //初始化库存，2个商品，每个商品10个库存
        for (int i = 1; i <=20; i++){
            keys.add("sku_quantity_"+i);
            //每商品减1
            values.add("-1");
        }

        Boolean result  =(Boolean) stringRedisTemplate.execute(redisScript,keys,values.toArray());

        if (!result) {
            System.out.println(result);
        }

    }


    public void reset(){

        //初始化库存，2个商品，每个商品1500个库存
        for (int i = 1; i <=20; i++){
            if (i == 10) {
                stringRedisTemplate.opsForValue().set("sku_quantity_"+i,""+50);

            }else {
                stringRedisTemplate.opsForValue().set("sku_quantity_"+i,""+100);
            }


        }
    }


    public  void valid(){
        for (int i = 1; i <=20; i++){
            String value  = (String)stringRedisTemplate.opsForValue().get("sku_quantity_"+i );
                System.out.println(i+"->>"+ value);

        }
    }
}
