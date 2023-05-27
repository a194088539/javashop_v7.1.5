package com.enation.app.javashop.core.trade;

import com.enation.app.javashop.core.promotion.pintuan.model.Participant;
import com.enation.app.javashop.core.trade.order.model.dto.PersonalizedData;
import com.enation.app.javashop.core.trade.order.model.enums.OrderDataKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kingapex on 2019-02-11.
 * 性化数据测试
 * @author kingapex
 * @version 1.0
 * @since 7.1.0
 * 2019-02-11
 */
public class PersonalizedDataTest {

    @Test
    public void test() {
        //模拟要存入到的个性化数据
        Participant participant = new Participant();
        participant.setName("test");

        Participant participant1 = new Participant();
        participant1.setName("test1");

        PersonalizedData order = new PersonalizedData();
        order.setPersonalizedData(OrderDataKey.pintuan, participant);
        order.setPersonalizedData(OrderDataKey.test, participant);

        Gson gson = new GsonBuilder().create();

        Assert.assertEquals("{\"test\":\"{\\\"name\\\":\\\"test\\\"}\",\"pintuan\":\"{\\\"name\\\":\\\"test\\\"}\"}",order.getData() );

        Map map = gson.fromJson(order.getData(), HashMap.class);
        String json  = map.get(OrderDataKey.pintuan.name()).toString();

        participant =  gson.fromJson(json, Participant.class);

        Assert.assertEquals("test", participant.getName());


    }

    @Test
    public void test2() {
        PersonalizedData order = new PersonalizedData();
        order.setPersonalizedData(OrderDataKey.pintuan, 12);
        Gson gson = new GsonBuilder().create();
        Map map = gson.fromJson(order.getData(), HashMap.class);

        System.out.println(map.get(OrderDataKey.pintuan.name()));
    }
}
