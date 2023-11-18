package com.javainuse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.config.JwtTokenUtil;
import com.javainuse.dao.UserRepository;
import com.javainuse.entity.JwtRequest;
import com.javainuse.entity.JwtResponse;
import com.javainuse.entity.User;
import com.javainuse.entity.UserDTO;
import com.javainuse.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {

		 final Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                		jwtRequest.getEmail(),
	                		jwtRequest.getPassword()
	                )
	        );
		
		 SecurityContextHolder.getContext().setAuthentication(authentication);
	        final String token = jwtTokenUtil.generateToken(authentication);
	        return ResponseEntity.ok(new JwtResponse(token));
	    }
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		User userByEmail = userRepository.findByEmail(user.getEmail());
		if(userByEmail != null && userByEmail.getEmail().equals(user.getEmail())) {
			return ResponseEntity.badRequest().body("Email already exists");
		}
		else {
		return ResponseEntity.ok(userDetailsService.save(user));
		}
	}

//	private void authenticate(String username, String password) throws Exception {
//		try {
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//		} catch (DisabledException e) {
//			throw new Exception("USER_DISABLED", e);
//		} catch (BadCredentialsException e) {
//			throw new Exception("INVALID_CREDENTIALS", e);
//		}
//	}
}