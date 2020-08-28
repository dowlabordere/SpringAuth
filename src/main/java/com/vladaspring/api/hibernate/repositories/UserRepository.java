package com.vladaspring.api.hibernate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vladaspring.api.hibernate.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	//vraper vratice ako postoji
	Optional<User> findByUsername(String username);

}
