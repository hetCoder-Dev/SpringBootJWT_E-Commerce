package com.javainuse.bean;

import java.util.List;

import com.javainuse.entity.Product;

public class ProductList {

	List<Product> products;

	public ProductList() {
		super();
	}

	public ProductList(List<Product> products) {
		super();
		this.products = products;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	
}
