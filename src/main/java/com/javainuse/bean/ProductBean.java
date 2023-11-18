package com.javainuse.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.javainuse.entity.Product;
import com.javainuse.entity.User;

public class ProductBean {

	private int id;
	private String prodName;
	private String prodDesc;
	private String prodImg;
	private Double prodSellPrice;
	private Double prodCostPrice;
	private int stockUnit;
	private byte isDeleted;
	private String createdDate;
	private int user;

	public ProductBean() {
		super();
	}

	public ProductBean(int id, String prodName, String prodDesc, String prodImg, Double prodSellPrice,
			Double prodCostPrice, int stockUnit, byte isDeleted, String createdDate, int user) {
		super();
		this.id = id;
		this.prodName = prodName;
		this.prodDesc = prodDesc;
		this.prodImg = prodImg;
		this.prodSellPrice = prodSellPrice;
		this.prodCostPrice = prodCostPrice;
		this.stockUnit = stockUnit;
		this.isDeleted = isDeleted;
		this.createdDate = createdDate;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public String getProdImg() {
		return prodImg;
	}

	public void setProdImg(String prodImg) {
		this.prodImg = prodImg;
	}

	public Double getProdSellPrice() {
		return prodSellPrice;
	}

	public void setProdSellPrice(Double prodSellPrice) {
		this.prodSellPrice = prodSellPrice;
	}

	public Double getProdCostPrice() {
		return prodCostPrice;
	}

	public void setProdCostPrice(Double prodCostPrice) {
		this.prodCostPrice = prodCostPrice;
	}

	public int getStockUnit() {
		return stockUnit;
	}

	public void setStockUnit(int stockUnit) {
		this.stockUnit = stockUnit;
	}

	public byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}
	
	public Product convertProduct() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Product product = new Product();
		product.setId(id);
		product.setProdName(prodName);
		product.setProdDesc(prodDesc);
		product.setProdCostPrice(prodCostPrice);
		product.setProdSellPrice(prodSellPrice);
		product.setStockUnit(stockUnit);
		product.setIsDeleted(isDeleted);
		
		try {
			product.setCreatedDate(sdf.parse(createdDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		User u = new User();
		u.setId(user);
		product.setUser(u);
		return product;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "ProductBean [id=" + id + ", prodName=" + prodName + ", prodDesc=" + prodDesc + ", prodImg=" + prodImg
				+ ", prodSellPrice=" + prodSellPrice + ", prodCostPrice=" + prodCostPrice + ", stockUnit=" + stockUnit
				+ ", isDeleted=" + isDeleted + ", createdDate=" + createdDate + ", user=" + user + "]";
	}
	
	

}
