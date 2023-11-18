package com.javainuse.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.javainuse.bean.PurchaseBean;
import com.javainuse.bean.PurchaseDto;
import com.javainuse.entity.Purchase;

public interface PurchaseService {
	
	public Purchase insertOrder(PurchaseBean purchase);
	
	public Purchase getPurchaseById(int id);
	
	public void cancelOrder(Integer id);
	
	public int updateProduct(Integer qty, @Param("prod_id") Integer id);
	
	public int updateBuyerBal(Double balance, @Param("u_id") Integer id);
	
	public int updateSellBalance(Double balance, Integer id);
	
	public List<PurchaseDto> getPurchaseByBuyerId(Integer id);
	
	public List<PurchaseDto> getPurchaseBySellerId(Integer id);

}
