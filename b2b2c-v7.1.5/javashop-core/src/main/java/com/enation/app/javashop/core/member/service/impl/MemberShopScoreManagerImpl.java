package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.member.model.dos.MemberShopScore;
import com.enation.app.javashop.core.member.model.dto.MemberShopScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.core.member.service.MemberShopScoreManager;

import java.util.List;

/**
 * 店铺评分业务类
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 10:38:00
 */
@Service
public class MemberShopScoreManagerImpl implements MemberShopScoreManager{

	@Autowired
	@Qualifier("memberDaoSupport")
	private	DaoSupport	daoSupport;
	
	@Override
	public Page list(int page,int pageSize){
		
		String sql = "select * from es_member_shop_score  ";
		Page  webPage = this.daoSupport.queryForPage(sql,page, pageSize ,MemberShopScore.class );
		
		return webPage;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	MemberShopScore  add(MemberShopScore	memberShopScore)	{
		this.daoSupport.insert(memberShopScore);
		
		return memberShopScore;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	MemberShopScore  edit(MemberShopScore	memberShopScore,Integer id){
		this.daoSupport.update(memberShopScore, id);
		return memberShopScore;
	}
	
	@Override
	@Transactional(value = "memberTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete( Integer id)	{
		this.daoSupport.delete(MemberShopScore.class,	id);
	}
	
	@Override
	public	MemberShopScore getModel(Integer id)	{
		return this.daoSupport.queryForObject(MemberShopScore.class, id);
	}

	@Override
	public List<MemberShopScoreDTO> queryEveryShopScore() {

		String sql = "select seller_id,round(AVG(delivery_score),2) AS delivery_score,round(AVG(description_score),2) AS description_score,round(AVG(service_score),2) AS service_score " +
				" from es_member_shop_score group by seller_id ";
		List<MemberShopScoreDTO> shopScoreList = this.daoSupport.queryForList(sql,MemberShopScoreDTO.class);

		return shopScoreList;
	}

	@Override
	public MemberShopScore getModel(Integer memberId, String orderSn) {
		String sql = "select * from es_member_shop_score where member_id = ? and order_sn = ?";
		return this.daoSupport.queryForObject(sql, MemberShopScore.class, memberId, orderSn);
	}
}
