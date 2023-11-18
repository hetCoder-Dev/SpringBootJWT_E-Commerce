package com.javainuse.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "u_id")
	private int id;
	private String firstName;
	private String lastName;
	private String dob;
	private String email;
	private String mobile;
	private double balance;
	private String password;
	private String address;
	private boolean isValid;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	Set<UserRoles> roles = new HashSet<UserRoles>();

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Product> products;

	@OneToMany(mappedBy = "seller")
	@JsonIgnore
	private List<Purchase> purchases;

	public User() {
		super();
	}

	public User(int id, String firstName, String lastName, String dob, String email, String mobile, double balance,
			String password, String address, boolean isValid, Set<UserRoles> roles, List<Product> products,
			List<Purchase> purchases) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.email = email;
		this.mobile = mobile;
		this.balance = balance;
		this.password = password;
		this.address = address;
		this.isValid = isValid;
		this.roles = roles;
		this.products = products;
		this.purchases = purchases;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Set<UserRoles> getRoles() {
		return roles;
	}

	public void setRoles(UserRoles role) {
		this.roles.add(role);
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob + ", email="
				+ email + ", mobile=" + mobile + ", balance=" + balance + ", password=" + password + ", address="
				+ address + ", isValid=" + isValid + ", roles=" + roles +"]";
	}

//	public List<Purchase> getPurchases() {
//		return purchases;
//	}
//
//	public void setPurchases(List<Purchase> purchases) {
//		this.purchases = purchases;
//	}

	

}