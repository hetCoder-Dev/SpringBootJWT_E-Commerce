package com.javainuse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.bean.JwtStatus;
import com.javainuse.bean.ProductBean;
import com.javainuse.bean.ProductList;
import com.javainuse.dao.UserRepository;
import com.javainuse.entity.Product;
import com.javainuse.entity.User;
import com.javainuse.service.ProductService;

@RestController
@RequestMapping("/seller")
@PreAuthorize("hasAuthority('ROLE_SELLER')")
public class SellerController {

	@Autowired
	private ProductService productService;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/addProduct", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('ROLE_SELLER')")
	public ResponseEntity<JwtStatus> addProduct(@RequestBody ProductBean productBean) {

		JwtStatus msg = new JwtStatus();

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("Username: " + username);
		User user = userRepository.findByEmail(username);
		System.out.println("Product Stock: " + productBean.getStockUnit());

		if (user == null) {
			return new ResponseEntity<JwtStatus>(HttpStatus.NOT_FOUND);
		} else {
			if (productBean.getStockUnit() < 0) {
				msg.setMsg("Invalid stock unit");
				msg.setSuccess(false);
				return new ResponseEntity<JwtStatus>(msg, HttpStatus.BAD_REQUEST);
			} else {
				System.out.println("User Id: " + user.getId());
				productBean.setUser(user.getId());
				productService.createProduct(productBean);
				msg.setMsg("Created Product");
				msg.setSuccess(true);
				return new ResponseEntity<JwtStatus>(msg, HttpStatus.CREATED);
			}
		}
	}

	@RequestMapping(value = "/viewSellerProducts", method = RequestMethod.GET)
	public ProductList showProducts() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("Username: " + username);
		User user = userRepository.findByEmail(username);
		System.out.println("User Id: " + user.getId());

		return new ProductList(productService.getAllSellerProducts(user.getId()));

	}

	@PutMapping("/editProduct/{id}")
	public Product editProduct(@PathVariable("id") Integer id, @RequestBody ProductBean product) {

		System.out.println("Id in edit ctrl: " + id);
		Product prod = productService.getProductById(id);
		System.out.println("Prod: " + prod);
		System.out.println("Product By id: " + product);
		Integer id2 = prod.getUser().getId();
		User u = new User();
		u.setId(id2);

		System.out.println("Seller Id: " + id2);
		
		product.setProdName((product.getProdName() != null) ? product.getProdName() : prod.getProdName());
		product.setProdDesc((product.getProdDesc() != null) ? product.getProdDesc() : prod.getProdDesc());
		product.setProdSellPrice((product.getProdSellPrice() != null) ? product.getProdSellPrice() : prod.getProdSellPrice());
		product.setProdCostPrice((product.getProdCostPrice() != null) ? product.getProdCostPrice() : prod.getProdCostPrice());
		product.setStockUnit((product.getStockUnit() != 0) ? product.getStockUnit() : prod.getStockUnit());
		product.setUser(id2);
		product.setId(id);
		System.out.println("Product Id: "+product.getId());
		
		product.setCreatedDate((product.getCreatedDate() != null) ? product.getCreatedDate() : prod.getCreatedDate().toString());
		System.out.println("Created date: " + prod.getCreatedDate());
		return productService.updateProduct(product.convertProduct());

	}

	@DeleteMapping("/deleteProduct/{id}")
	public ResponseEntity<JwtStatus> deleteProduct(@PathVariable("id") Integer id) {
		System.out.println("Selected Id: " + id);		

		JwtStatus msg = new JwtStatus();
		msg.setMsg("Product Deleted");
		Product productById = productService.getProductById(id);

		if (productById == null) {
			msg.setMsg("Could not delete product");
			msg.setSuccess(false);
			return new ResponseEntity<JwtStatus>(msg, HttpStatus.BAD_REQUEST);
		} else {
			productService.removeProduct(id);
			msg.setSuccess(true);
			return new ResponseEntity<JwtStatus>(msg, HttpStatus.OK);
		}
	}

	@RequestMapping("/sellerSummary")
	public ProductList sellerSummary() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("Username: " + username);
		User user = userRepository.findByEmail(username);
		System.out.println("User Id: " + user.getId());

		return new ProductList(productService.getAllSellerProducts(user.getId()));

	}
}
