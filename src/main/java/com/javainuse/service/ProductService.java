package com.javainuse.service;

import java.util.List;

import com.javainuse.bean.ProductBean;
import com.javainuse.entity.Product;

public interface ProductService {

	public List<Product> getAllSellerProducts(int id);

	public List<Product> showAllProducts();

	public Product createProduct(ProductBean productBean);

	public Product updateProduct(Product product);

	public Product getProductById(int id);

	public void removeProduct(int id);

}
