package com.enation.app.javashop.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kingapex on 2018/3/28.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.enation.app.javashop")
@Transactional
@Rollback()
public class ApplicationTests {
    @Test
    public void contextLoads() {
    }
}
