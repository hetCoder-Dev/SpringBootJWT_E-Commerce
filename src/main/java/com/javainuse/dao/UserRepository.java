package com.javainuse.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.javainuse.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByEmail(String email);
	
	User findUserById(int id);
	
	//List<UserRoles> getAllRoles();
	
}