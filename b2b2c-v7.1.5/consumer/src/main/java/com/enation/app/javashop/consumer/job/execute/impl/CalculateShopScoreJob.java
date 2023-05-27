package com.enation.app.javashop.consumer.job.execute.impl;

import com.enation.app.javashop.consumer.job.execute.EveryDayExecute;
import com.enation.app.javashop.core.client.member.ShopClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 计算店铺评分
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-03-29 15:10:38
 */
@Component
public class CalculateShopScoreJob implements EveryDayExecute {

	@Autowired
	private ShopClient shopClient;

    @Override
	public void everyDay() {
		shopClient.calculateShopScore();
	}

}
