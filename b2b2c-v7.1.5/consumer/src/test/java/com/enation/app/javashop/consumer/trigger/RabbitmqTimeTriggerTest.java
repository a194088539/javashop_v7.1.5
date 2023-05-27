package com.enation.app.javashop.consumer.trigger;

import com.enation.app.javashop.core.statistics.util.DateUtil;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.test.BaseTest;
import com.enation.app.javashop.framework.trigger.Interface.TimeTrigger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 延时任务单元测试
 *
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 如果报rabbitmq异常，而不是断言错误，请确定死信是否已经部署，即deploy的rabbitmq ，需要部署，死信设置成功后，这里就可以正常流程了
 * 2019-02-19 下午1:40
 */

public class RabbitmqTimeTriggerTest extends BaseTest {

    @Autowired
    private TimeTrigger timeTrigger;

    @Autowired
    private Cache cache;


    /**
     * 添加定时任务单元测试
     * <p>
     * 测试思路：添加一个延时任务，线程休眠，断言任务是否被执行
     * 添加延时任务，查看不同的参数是不是会影响
     *
     * @throws Exception
     */
    @Test
    public void addTimeTrigger() throws Exception {
//      添加任务
        timeTrigger.add("testTimeTrigger", "1", DateUtil.getDateline() + 10, null);
        Assert.assertNull(cache.get("rabbitmq_test_value"));
        Thread.sleep(15000);
        Assert.assertEquals(1, cache.get("rabbitmq_test_value"));
        cache.remove("rabbitmq_test_value");


//      添加任务 带有标识的任务
        timeTrigger.add("testTimeTrigger", "1", DateUtil.getDateline() + 10, "testTimeTrigger1");
        Assert.assertNull(cache.get("rabbitmq_test_value"));
        Thread.sleep(15000);
        Assert.assertEquals(1, cache.get("rabbitmq_test_value"));
        cache.remove("rabbitmq_test_value");
    }
    /**
     * 交叉任务
     * <p>
     * 测试思路：添加一个延时任务，10后执行，修改值为A，<br/>
     *  添加第二个任务，延时5秒执行，修改值为B，<br/>
     *  秒线程休眠 6秒，判断B值，休眠11秒，判断A值
     *
     * @throws Exception
     */
    @Test
    public void overlapping() throws Exception {
//      添加任务
        timeTrigger.add("testTimeTrigger", "1", DateUtil.getDateline() + 10, null);
        timeTrigger.add("testTimeTrigger", "2", DateUtil.getDateline() + 5, null);

        Assert.assertNull(cache.get("rabbitmq_test_value"));
        Thread.sleep(6000);
        Assert.assertEquals("2", cache.get("rabbitmq_test_value"));
        Thread.sleep(11000);
        Assert.assertEquals("1", cache.get("rabbitmq_test_value"));
        cache.remove("rabbitmq_test_value");

    }

    /**
     * 编辑定时任务单元测试
     * <p>
     * <p>
     * 测试思路：
     * 1、添加任务，10秒后执行。
     * 2、修改任务，修改到20秒后执行。
     * 3、15秒后进行未执行任务判定
     * 4、25秒后进行修改后的任务执行判定
     *
     * @throws Exception
     */
    @Test
    public void editTimeTrigger() throws Exception {
        Long startTime = DateUtil.getDateline();
        timeTrigger.add("testTimeTrigger", "1", startTime + 10, "testTimeTrigger1");
        timeTrigger.edit("testTimeTrigger", "1", startTime + 10, startTime + 20, "testTimeTrigger1");
        Assert.assertNull(cache.get("rabbitmq_test_value"));
        Thread.sleep(15000);
        Assert.assertNull(cache.get("rabbitmq_test_value"));
        Thread.sleep(10000);

        Assert.assertEquals(1, cache.get("rabbitmq_test_value"));
        cache.remove("rabbitmq_test_value");
    }

    /**
     * 删除定时任务单元测试
     * <p>
     * 1、添加任务，10秒后执行。
     * 2、5秒后进行未执行任务判定
     * 3、20秒后进行修改后的任务执行判定
     *
     * @throws Exception
     */
    @Test
    public void deleteTimeTrigger() throws Exception {
        Long startTime = DateUtil.getDateline();
        timeTrigger.add("testTimeTrigger", "1", startTime + 10, "testTimeTrigger1");
        timeTrigger.delete("testTimeTrigger", startTime + 10, "testTimeTrigger1");


        Assert.assertNull(cache.get("rabbitmq_test_value"));
        Thread.sleep(5000);
        Assert.assertNull(cache.get("rabbitmq_test_value"));
        Thread.sleep(15000);

        Assert.assertNull(cache.get("rabbitmq_test_value"));
        cache.remove("rabbitmq_test_value");
    }


}
