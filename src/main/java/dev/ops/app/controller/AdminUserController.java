package dev.ops.app.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.ops.app.models.AdminUser;
import dev.ops.app.models.ResponseMessage;
import dev.ops.app.service.AdminUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/v1/api")
public class AdminUserController {
	@Autowired
	AdminUserService userService;

	@Value("${file.upload-dir}")
	String UPLOAD_FOLDER;

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	AuthenticationManager authenticationManager;



	
	@PutMapping("/adminuser")
	public ResponseEntity<AdminUser> updateUser(@RequestBody AdminUser user) {
		
		try {
			return new ResponseEntity<AdminUser>(userService.updateUser(user), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<AdminUser>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/deactivate/adminuser")
	public ResponseEntity<AdminUser> deleteUser(@RequestBody AdminUser user) {
		
		try {
			return new ResponseEntity<AdminUser>(userService.delete(user), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<AdminUser>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/user")
	public void deleteUsr(@RequestParam long id) {
		
		userService.delete(id);
		
	}
	
	@PutMapping("/activate/adminuser")
	public ResponseEntity<AdminUser> activate(@RequestBody AdminUser user) {
		
		try {
			return new ResponseEntity<AdminUser>(userService.activate(user), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<AdminUser>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/adminusers")
	public ResponseEntity<List<AdminUser>> adminUsers() {
		return new ResponseEntity<List<AdminUser>>(userService.getAllUsers(), HttpStatus.OK);

	}
	
	@GetMapping("/admin/user/{id}")
	public ResponseEntity<AdminUser> getUser(@PathVariable long id) {
		return new ResponseEntity<AdminUser>(userService.findUser(id), HttpStatus.OK);

	}

	@PostMapping("/authenticated")
	public ResponseEntity<ResponseMessage> login(@RequestBody AdminUser user) {
		ResponseMessage message = new ResponseMessage();

		Optional<AdminUser> details = userService.loadUserByUsernames(user.getUsername());
		if (details.isPresent()) {
			System.err.println("------------------" + details.toString());

			String token = Jwts.builder().setSubject(details.get().getUsername())
					.claim("authorities", details.get().getAuthorities()).setIssuedAt(new Date())
					.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
					.signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
			details.get().setToken(token);
			message.setPayload(details.get());
			message.setMessage("User found Successfully");
			message.setStatusCode(200);
			System.err.println(details);
			return new ResponseEntity<ResponseMessage>(message, HttpStatus.OK);
		} else {
			// TODO:ser handle exception
			message.setStatusCode(500);
			message.setMessage("Wrong credentials");
			return new ResponseEntity<ResponseMessage>(message, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/authenticate")
	public ResponseEntity<AdminUser> logins(@RequestBody AdminUser user) {
		ResponseMessage message = new ResponseMessage();
		System.err.println("------------------" + user.toString());

		

		Optional<AdminUser> details = userService.loadUserByUsernames(user.getUsername());
		System.err.println("------------------" + details.get().getRole());

		if (details.isPresent()) {
			System.err.println("------------------" + details.toString());

			String token = Jwts.builder().setSubject(details.get().getName())
					.claim("authorities", details.get().getAuthorities()).setIssuedAt(new Date())
					.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
					.signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
			details.get().setToken(token);
			message.setPayload(details.get());
			message.setMessage("User found Successfully");
			message.setStatusCode(200);
			return new ResponseEntity<AdminUser>(details.get(), HttpStatus.OK);
		} else {
			// TODO:ser handle exception
			message.setStatusCode(500);
			message.setMessage("Wrong credentials");
			return new ResponseEntity<AdminUser>(HttpStatus.BAD_REQUEST);
		}

	}

}
