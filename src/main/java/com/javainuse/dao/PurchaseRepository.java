package com.javainuse.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.javainuse.bean.PurchaseDto;
import com.javainuse.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

	//public PurchaseBean insertOrder(PurchaseBean purchase);

	@Modifying
	@Transactional
	@Query(value="delete from purchase_jwt where purchase_id=?1", nativeQuery=true)
	public void cancelOrder(@Param("purchase_id") Integer id);
	
	@Modifying
	@Query(value="update product_jwt set stock_unit=?1 where prod_id=?2", nativeQuery=true)
	public int updateProduct(@Param("stock_unit") Integer qty, @Param("prod_id") Integer id);
	
	@Modifying
	@Query(value="update user set balance=?1 where u_id=?2", nativeQuery=true)
	public int updateBuyerBal(@Param("balance") Double balance, @Param("u_id") Integer id);

	@Modifying
	@Query(value="update user set balance=?1 where u_id=?2", nativeQuery=true)
	public int updateSellBalance(@Param("balance") Double balance, @Param("u_id") Integer id);
	
	@Query(value="select purchase_id as purchaseId, purchase_dt as purchaseDt, total_cost_price as totalCostPrice, total_unit as totalUnit,"
			+ " buyer_id as buyerId, pr_id as productId from purchase_jwt where buyer_id=?1", nativeQuery = true)
	public List<PurchaseDto> getPurchaseByBuyerId(@Param("buyer_id") Integer id);

	@Query(value="select purchase_id as purchaseId, purchase_dt as purchaseDt, total_cost_price as totalCostPrice, total_unit as totalUnit,"
			+ " seller_id as sellerId, pr_id as productId from purchase_jwt where seller_id=?1", nativeQuery = true)
	public List<PurchaseDto> getPurchaseBySellerId(@Param("seller_id") Integer id);
	
	

}
