package com.enation.app.javashop.core.base.content;

import com.enation.app.javashop.core.goods.service.impl.util.StockCacheKeyUtil;
import com.enation.app.javashop.framework.redis.configure.JedisSetting;
import com.enation.app.javashop.framework.redis.configure.RedisConnectionConfig;
import com.enation.app.javashop.framework.redis.configure.builders.RedisStandaloneBuilder;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingapex on 2019-01-11.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2019-01-11
 */
@SuppressWarnings("Duplicates")
public class RedisTest {


    private  StringRedisTemplate getRedisTemplate(){

        RedisConnectionConfig config = new RedisConnectionConfig();

        JedisSetting.loadPoolConfig(config);
        RedisStandaloneBuilder standaloneBuilder = new RedisStandaloneBuilder();

        config.setConfigType("manual");
        config.setType("standalone");
        config.setHost("192.168.2.5");
        config.setPassword("");
        config.setPort(6379);

        RedisConnectionFactory lettuceConnectionFactory = standaloneBuilder.buildConnectionFactory(config);

        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Test
    public void testGet() {
        StringRedisTemplate redisTemplate = this.getRedisTemplate();
        List keys = new ArrayList<>();
        Integer[] ar = new Integer[]{1366, 1371, 1387};

        int j=0;
        for (int i = 0; i < ar.length; i++) {
            redisTemplate.opsForValue().set("test_"+ StockCacheKeyUtil.skuEnableKey(ar[i]),""+j);
            j++;
            redisTemplate.opsForValue().set("test_"+ StockCacheKeyUtil.skuActualKey(ar[i]),""+j);
            j++;
        }
        for (int i = 0; i < ar.length; i++) {
            keys.add("test_"+StockCacheKeyUtil.skuEnableKey(ar[i]));
            keys.add("test_"+StockCacheKeyUtil.skuActualKey(ar[i]));

        }

        List values = redisTemplate.opsForValue().multiGet(keys);
        System.out.println(values);

          j = 0;
        for (int i = 0; i < ar.length; i++) {
            System.out.println( ar[i]+ " enable : " + values.get(j));
            j++;
            System.out.println( ar[i]+ " actual : " + values.get(j));
            j++;
        }
    }

    @Test
    public void test1() throws IOException {
        StringRedisTemplate redisTemplate = this.getRedisTemplate();
        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("test.lua"));
        String str =scriptSource.getScriptAsString();

        RedisScript<Boolean> redisScript = RedisScript.of(str, Boolean.class);
        List keys = new ArrayList<>();
        List values =  new ArrayList<>();
        Boolean result  = redisTemplate.execute(redisScript,keys,values.toArray());
        System.out.println(result);
        String value = redisTemplate.opsForValue().get("mytest");
        System.out.println(value);


    }

    @Test
    public void test() throws IOException {
        StringRedisTemplate redisTemplate = this.getRedisTemplate();
        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("sku_quantity.lua"));
        String str =scriptSource.getScriptAsString();

        RedisScript<Boolean> redisScript = RedisScript.of(str, Boolean.class);

        List keys = new ArrayList<>();
        List values =  new ArrayList<>();


        //初始化库存，2个商品，每个商品10个库存
        for (int i = 1; i <=20; i++){
//            redisTemplate.opsForValue().set("sku_quantity_"+i,""+10);
            keys.add("sku_quantity_"+i);
            //每商品减1
            values.add("-1");
        }

        Boolean result  = redisTemplate.execute(redisScript,keys,values.toArray());

        if (!result) {
            System.out.println(result);
        }

        view(redisTemplate);
    }

    private  void view( StringRedisTemplate redisTemplate ){

        for (int i = 1; i <=20; i++){
            String value  = redisTemplate.opsForValue().get("sku_quantity_"+i );
            System.out.println(i+"->>"+ value);

        }
    }


}
