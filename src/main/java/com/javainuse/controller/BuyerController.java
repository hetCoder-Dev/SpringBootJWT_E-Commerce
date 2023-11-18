package com.javainuse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.bean.JwtStatus;
import com.javainuse.bean.ProductList;
import com.javainuse.bean.PurchaseBean;
import com.javainuse.bean.PurchaseDto;
import com.javainuse.dao.UserRepository;
import com.javainuse.entity.Product;
import com.javainuse.entity.Purchase;
import com.javainuse.entity.User;
import com.javainuse.service.ProductService;
import com.javainuse.service.PurchaseService;

@RestController
@PreAuthorize("hasAuthority('ROLE_BUYER')")
@RequestMapping("/buyer")
public class BuyerController {

	@Autowired
	private ProductService productService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PurchaseService purchaseService;

	@RequestMapping("/viewProducts")
	public ProductList showProducts() {

		return new ProductList(productService.showAllProducts());
	}

//	@GetMapping("/user/{id}")
//	@PreAuthorize("hasAuthority('ROLE_SELLER')")
//	public ResponseEntity<User> getUser(@PathVariable("id") Integer id) {
//
//		User findUserById = userRepository.findUserById(id);
//		System.out.println("User in ctrl: " + findUserById);
//		if (findUserById != null && findUserById.getId() == id) {
//			return new ResponseEntity<User>(findUserById, HttpStatus.OK);
//
//		} else {
//			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
//		}
//	}

	@RequestMapping(value = "/orderNow/{id}", method = RequestMethod.POST)
	public ResponseEntity<JwtStatus> addOrder(@RequestBody PurchaseBean purchaseBean, @PathVariable("id") Integer id) {

		System.out.println("Id: " + id);
		JwtStatus msg = new JwtStatus();
		msg.setMsg("Product Bought");

		// buyer Id
		System.out.println("Purchases: " + purchaseBean);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("Username: " + username);
		User user = userRepository.findByEmail(username);
		System.out.println("Buyer Id: " + user.getId());

		Product productById = productService.getProductById(id);
		System.out.println("Product: " + productById);

		// seller Id
		productById.getUser().getId();
		System.out.println("Seller id: " + productById.getUser().getId());

		User u = new User();
		u.setId(productById.getUser().getId());

		// seller balance
		u.setBalance(u.getBalance() + (purchaseBean.getTotalUnit() * productById.getProdSellPrice()));
		System.out.println("Seller Balance: " + u.getBalance());

		// purchase date
		purchaseBean.setPurchaseDt(purchaseBean.getPurchaseDt().toString());
		System.out.println("Pur Dt: " + purchaseBean.getPurchaseDt());

		productById.setStockUnit(productById.getStockUnit() - purchaseBean.getTotalUnit());
		System.out.println("Qty: " + productById.getStockUnit());

		// buyer balance
		user.setBalance(user.getBalance() - (productById.getProdSellPrice() * purchaseBean.getTotalUnit()));
		System.out.println("Buyer balance: " + user.getBalance());

		// total Unit
		purchaseBean.setTotalUnit(purchaseBean.getTotalUnit());
		System.out.println("Total Unit: " + purchaseBean.getTotalUnit());
		
		if(purchaseBean.getTotalUnit() < 0) {
			msg.setSuccess(false);
			msg.setMsg("Invalid Total Unit");
			return new ResponseEntity<JwtStatus>(msg, HttpStatus.BAD_REQUEST);
		}
		if(purchaseBean.getTotalUnit() > productById.getStockUnit()) {
			msg.setSuccess(false);
			msg.setMsg("Total Unit Exceeds Stock Unit");
			return new ResponseEntity<JwtStatus>(msg, HttpStatus.BAD_REQUEST);
		}
		else {
		// purchase final price
		purchaseBean.setTotalCostPrice(productById.getProdSellPrice() * purchaseBean.getTotalUnit());
		System.out.println("Total cost price: " + purchaseBean.getTotalCostPrice());

		purchaseBean.setBuyer(user.getId());
		purchaseBean.setSeller(u.getId());
		// System.out.println("Buyer Id: "+user.getId());
		System.out.println("Buyer and seller id: " + user.getId() + " " + u.getId());

		purchaseBean.setProduct(productById);
		System.out.println("Updated buyer balance: " + (user.getBalance() - productById.getProdSellPrice()));
		if (user.getBalance() - productById.getProdSellPrice() > 0) {

			purchaseService.insertOrder(purchaseBean);
			purchaseService.updateProduct(productById.getStockUnit(), productById.getId());
			System.out.println("Qty: " + productById.getStockUnit());

			purchaseService.updateBuyerBal(user.getBalance(), user.getId());
			System.out.println("Buyer balance: " + user.getBalance());

			purchaseService.updateSellBalance(u.getBalance(), u.getId());
			System.out.println("Seller Balance: " + u.getBalance());
			msg.setSuccess(true);
			return new ResponseEntity<JwtStatus>(msg, HttpStatus.OK);
		} else {
			return new ResponseEntity<JwtStatus>(HttpStatus.BAD_REQUEST);
		}
		
		}
	}

	@RequestMapping(value = "/viewPurchase", method = RequestMethod.GET)
	public List<PurchaseDto> showPurchses() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("Username: " + username);
		User user = userRepository.findByEmail(username);
		System.out.println("Buyer Id: " + user.getId());
		List<PurchaseDto> list = purchaseService.getPurchaseByBuyerId(user.getId());
		System.out.println("Purchases: "+list);
		return purchaseService.getPurchaseByBuyerId(user.getId());

	}

	@RequestMapping(value = "/cancelOrder/{id}", method= RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('ROLE_BUYER')")
	public ResponseEntity<JwtStatus> cancelorder(@PathVariable("id") Integer id) {

		JwtStatus msg = new JwtStatus();
		msg.setMsg("Cancelled Order");

		try {
			System.out.println("Selected id: " + id);
			Purchase purById = purchaseService.getPurchaseById(id);
			int prodId = purById.getProduct().getId();
			System.out.println("Product id: " + prodId);

			// Update Product on cancellation
			Product product = productService.getProductById(prodId);
			product.setStockUnit(product.getStockUnit() + purById.getTotalUnit());
			System.out.println("Product on cancellation: " + product.getStockUnit());

			// Seller
			product.getUser().getId();
			User u = new User();
			u.setId(product.getUser().getId());
			System.out.println("Seller Id: " + u.getId());

			u.setBalance(u.getBalance() - (purById.getTotalUnit() * product.getProdSellPrice()));
			System.out.println("Seller balance on cancellation: " + u.getBalance());

			// Buyer
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String username = userDetails.getUsername();
			System.out.println("Username: " + username);
			User user = userRepository.findByEmail(username);
			System.out.println("Buyer Id: " + user.getId());

			user.setBalance(user.getBalance() + (purById.getTotalUnit() * product.getProdSellPrice()));
			System.out.println("Buyer bal on cancellation: " + user.getBalance());

			purById.setProduct(product);
			if (purById != null) {
				purchaseService.cancelOrder(id);
				purchaseService.updateProduct(product.getStockUnit(), product.getId());
				purchaseService.updateBuyerBal(user.getBalance(), user.getId());
				purchaseService.updateSellBalance(u.getBalance(), u.getId());
				msg.setSuccess(true);
				return new ResponseEntity<JwtStatus>(msg, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		msg.setSuccess(false);
		return new ResponseEntity<JwtStatus>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/buyerSummary", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ROLE_BUYER')")
	public List<PurchaseDto> buyerSummary() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("Username: " + username);
		User user = userRepository.findByEmail(username);
		System.out.println("Buyer Id: " + user.getId());

		return purchaseService.getPurchaseByBuyerId(user.getId());

	}

}