package com.javainuse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javainuse.bean.PurchaseBean;
import com.javainuse.bean.PurchaseDto;
import com.javainuse.dao.PurchaseRepository;
import com.javainuse.entity.Purchase;

@Service
public class PurchaseServiceImpl implements PurchaseService{

	@Autowired
	private PurchaseRepository purchaseRepsitory;
	
	@Override
	@Transactional
	public Purchase insertOrder(PurchaseBean purchase) {		
		return purchaseRepsitory.save(purchase.convertToPurchase());
	}
	
	@Override
	public void cancelOrder(Integer id) {
		purchaseRepsitory.cancelOrder(id);
	}

	@Override
	@Transactional
	public int updateProduct(Integer qty, Integer id) {
		return purchaseRepsitory.updateProduct(qty, id);
	}

	@Override
	@Transactional
	public int updateBuyerBal(Double balance, Integer id) {
		return purchaseRepsitory.updateBuyerBal(balance, id);
	}

	@Override
	@Transactional
	public int updateSellBalance(Double balance, Integer id) {
		return purchaseRepsitory.updateSellBalance(balance, id);
	}

	@Override
	public List<PurchaseDto> getPurchaseByBuyerId(Integer id) {
		return purchaseRepsitory.getPurchaseByBuyerId(id);
	}

	@Override
	public List<PurchaseDto> getPurchaseBySellerId(Integer id) {
		return getPurchaseBySellerId(id);
	}

	@Override
	public Purchase getPurchaseById(int id) {
		return purchaseRepsitory.findById(id).get();
	}

	

}
