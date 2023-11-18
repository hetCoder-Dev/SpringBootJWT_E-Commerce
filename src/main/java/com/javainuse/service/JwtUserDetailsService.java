package com.javainuse.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javainuse.config.CustomUserDetails;
import com.javainuse.dao.RoleRepository;
import com.javainuse.dao.UserRepository;
import com.javainuse.entity.User;
import com.javainuse.entity.UserDTO;
import com.javainuse.entity.UserRoles;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userDao;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		

		System.out.println(username);
		User user = userDao.findByEmail(username);
		System.out.println(user);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
	}
	
	public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<UserRoles> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	
	public User save(UserDTO user) {
		
		UserRoles role = new UserRoles();
		
		User newUser = new User();
		newUser.setId(user.getId());
		System.out.println("Logged in user id: "+newUser.getId());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setMobile(user.getMobile());
		newUser.setDob(user.getDob());
		newUser.setBalance(user.getBalance());
		newUser.setEmail(user.getEmail());		
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		System.out.println("User Role: "+user.getUserRole());
		if(user.getUserRole().equals("BUYER")) {
			role = roleRepository.findByName("ROLE_BUYER");
		}else if(user.getUserRole().equals("SELLER")) {
			role = roleRepository.findByName("ROLE_SELLER");
		}
		
		newUser.setRoles(role);
		System.out.println("User Role: "+newUser.getRoles());
		return userDao.save(newUser);
	}
}