package com.starter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.starter.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEnabled(Boolean enabled);
	
	Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    Page<User> findByNameContainingAndEnabled(Pageable pageable, String name, Boolean enabled);
    
    Page<User> findByEnabled(Pageable pageable, Boolean enabled);
    
}
