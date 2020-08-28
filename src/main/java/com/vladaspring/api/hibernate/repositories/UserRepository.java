package com.vladaspring.api.hibernate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vladaspring.api.hibernate.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
