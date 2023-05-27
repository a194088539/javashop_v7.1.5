package com.enation.app.javashop.core.distribution.service;

import com.enation.app.javashop.core.distribution.model.dos.ShortUrlDO;

/**
 * 短链接Manager接口
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018/5/23 上午8:37
 * @Description:
 *
 */
public interface ShortUrlManager {


	/**
	 * 上线人员
	 */
    String PREFIX="{DISTRIBUTION_UP}_";
	/**
	 * 生成一个短链接
	 * @param memberId
	 * @param goodsId
	 * @return
	 */
    ShortUrlDO createShortUrl(Integer memberId, Integer goodsId);
	
	/**
	 * 根据短链接获得长链接
	 * @param shortUrl 短链接 （可带前缀 即：http:xxx/）
	 * @return 所对应的长链接
	 */
    ShortUrlDO getLongUrl(String shortUrl);
	
}
