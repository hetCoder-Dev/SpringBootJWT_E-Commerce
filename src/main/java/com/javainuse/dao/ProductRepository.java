package com.javainuse.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.javainuse.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query(value = "Select * from product_jwt where is_deleted=1 and seller_id=?1", nativeQuery= true)
	public List<Product> getAllSellerProducts(int id);

	@Query(value = "Select * from product_jwt where is_deleted=1", nativeQuery= true)
	public List<Product> showAllProducts();

	//public ProductBean createProduct(ProductBean product);

	//public Product updateProduct(Product product);

	public Product getProductById(int id);

	@Modifying
	@Transactional
	@Query(value = "update product_jwt set is_deleted=0 where prod_id=?1", nativeQuery= true)
	public void removeProduct(@Param("prod_id") int id);

}
