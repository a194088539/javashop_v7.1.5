package com.enation.app.javashop.core.trade.complain.service;

import com.enation.app.javashop.core.trade.complain.model.dto.ComplainDTO;
import com.enation.app.javashop.core.trade.complain.model.dto.ComplainQueryParam;
import com.enation.app.javashop.core.trade.complain.model.vo.OrderComplainVO;
import com.enation.app.javashop.core.trade.order.model.vo.OrderFlowNode;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.trade.complain.model.dos.OrderComplain;

import java.util.List;

/**
 * 交易投诉表业务层
 * @author fk
 * @version v2.0
 * @since v2.0
 * 2019-11-27 16:48:27
 */
public interface OrderComplainManager	{

	/**
	 * 查询交易投诉表列表
	 * @param param
	 * @return Page
	 */
	Page list(ComplainQueryParam param);
	/**
	 * 添加交易投诉表
	 * @param complain 交易投诉表
	 * @return OrderComplain 交易投诉表
	 */
	OrderComplain add(ComplainDTO complain);

	/**
	* 修改交易投诉表
	* @param orderComplain 交易投诉表
	* @param id 交易投诉表主键
	* @return OrderComplain 交易投诉表
	*/
	OrderComplain edit(OrderComplain orderComplain, Integer id);
	
	/**
	 * 删除交易投诉表
	 * @param id 交易投诉表主键
	 */
	void delete(Integer id);
	
	/**
	 * 获取交易投诉表
	 * @param id 交易投诉表主键
	 * @return OrderComplain  交易投诉表
	 */
	OrderComplain getModel(Integer id);

	/**
	 * 撤销某个交易投诉
	 * @param id
	 * @return
	 */
    OrderComplain cancel(Integer id);

	/**
	 * 审核并交由商家申诉
	 * @param id
	 * @return
	 */
	OrderComplain auth(Integer id);

	/**
	 * 管理员仲裁结束流程
	 * @param id
	 * @param arbitrationResult
	 * @return
	 */
	OrderComplain complete(Integer id, String arbitrationResult);

	/**
	 * 商家申诉
	 * @param id
	 * @param appealContent
	 * @param images
	 * @return
	 */
	OrderComplain appeal(Integer id, String appealContent, String[] images);

	/**
	 * 提交仲裁
	 * @param id
	 * @return
	 */
	OrderComplain arbitrate(Integer id);

	/**
	 * 获取交易投诉及对话信息
	 * @param id
	 * @return
	 */
    OrderComplainVO getModelAndCommunication(Integer id);

	/**
	 * 查询交易投诉的流程图
	 * @param id
	 * @return
	 */
	List<OrderFlowNode> getComplainFlow(Integer id);
}