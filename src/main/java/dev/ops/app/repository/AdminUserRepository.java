package dev.ops.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ops.app.models.AdminUser;

public interface AdminUserRepository  extends JpaRepository<AdminUser, Long>{

	Optional<AdminUser> findByUsername(String username);
	
	

}
