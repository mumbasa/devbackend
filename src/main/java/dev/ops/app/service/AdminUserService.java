package dev.ops.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.ops.app.models.AdminUser;
import dev.ops.app.repository.AdminUserRepository;

@Service
public class AdminUserService implements UserDetailsService {

	@Autowired
	AdminUserRepository adminUserRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public Optional<AdminUser> loadUserByUsernames(String username) {
		Optional<AdminUser> userAdmin = adminUserRepository.findByUsername(username);

		return userAdmin;
	}

	public boolean userExists(String username) {
		Optional<AdminUser> userAdmin = adminUserRepository.findByUsername(username);
		return userAdmin.isEmpty();
	}
	
	public List<AdminUser> getAllUsers() {
		return adminUserRepository.findAll();
		}
	
	public AdminUser findUser(long id) {
		return adminUserRepository.findById(id).get();
		}

	public AdminUser saveUser(AdminUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return adminUserRepository.save(user);

	}
	
	public AdminUser updateUser(AdminUser user) {
		return adminUserRepository.save(user);

	}
	
	public AdminUser delete(AdminUser user) {
		return adminUserRepository.save(user);

	}
	
	public void delete(long user) {
		 adminUserRepository.deleteById(user);;

	}


	public AdminUser activate(AdminUser user) {
		return adminUserRepository.save(user);

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
