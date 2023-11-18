package com.javainuse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javainuse.entity.UserRoles;

@Repository
public interface RoleRepository extends JpaRepository<UserRoles, Integer>{
	
	UserRoles findByName(String name);

}
