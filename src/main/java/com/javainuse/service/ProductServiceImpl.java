package com.javainuse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javainuse.bean.ProductBean;
import com.javainuse.dao.ProductRepository;
import com.javainuse.dao.UserRepository;
import com.javainuse.entity.Product;
import com.javainuse.entity.User;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Product> getAllSellerProducts(int id) {
		
		User findUserById = userRepository.findUserById(id);
		System.out.println("User Id: "+findUserById);
		//System.out.println("Products: "+productRepository.getAllSellerProducts(id));
		return productRepository.getAllSellerProducts(id);
	}

	@Override
	public List<Product> showAllProducts() {
		return productRepository.showAllProducts();
	}

	@Override
	public Product createProduct(ProductBean product) {
		product.setIsDeleted((byte) 1);
		return productRepository.save(product.convertProduct());
	}

	@Override
	public Product updateProduct(Product product) {
		
		Product prod = new Product();
		
		prod.setProdName(product.getProdName());
		prod.setProdDesc(product.getProdDesc());
		prod.setProdImg(product.getProdImg());
		prod.setProdCostPrice(product.getProdCostPrice());
		prod.setProdSellPrice(product.getProdSellPrice());
		prod.setStockUnit(product.getStockUnit());
		prod.setId(product.getId());
		prod.setUser(product.getUser());
		prod.setIsDeleted((byte) 1);
		prod.setCreatedDate(product.getCreatedDate());
		Product save = productRepository.save(prod);
		return save;
	}

	@Override
	public Product getProductById(int id) {
		return productRepository.findById(id).get();
	}

	@Override
	public void removeProduct(int id) {
		productRepository.removeProduct(id);
		
	}

}
