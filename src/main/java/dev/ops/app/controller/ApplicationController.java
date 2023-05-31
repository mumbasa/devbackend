package dev.ops.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.ops.app.models.AdminUser;
import dev.ops.app.models.Applicant;
import dev.ops.app.service.ApplicationService;

@RestController
@RequestMapping("/v1/api")
@CrossOrigin(origins = "**")
public class ApplicationController {

	@Autowired
	ApplicationService applicationService;

	@PostMapping("/save/application")
	public ResponseEntity<Applicant> saveApplication(@RequestParam String phone, @RequestParam String email,
			@RequestParam String name, @RequestPart MultipartFile attachment) {
		try {
			return new ResponseEntity<Applicant>(applicationService.saveApplication(name, phone, email, attachment),
					HttpStatus.OK);
		} catch (Exception e) {

			return new ResponseEntity<Applicant>(HttpStatus.BAD_REQUEST);

		}

	}
	
	
	@PostMapping("/save/user")
	public ResponseEntity<AdminUser> saveUser(@RequestBody AdminUser user) {
		try {
			return new ResponseEntity<AdminUser>(applicationService.saveUser(user),
					HttpStatus.OK);
		} catch (Exception e) {

			return new ResponseEntity<AdminUser>(HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping("/all/application")
	public ResponseEntity<List<Applicant>> getApplications() {
		return new ResponseEntity<List<Applicant>>(applicationService.getApplications(),HttpStatus.OK);

	}
	
	@GetMapping("/all/dashdata")
	public ResponseEntity<List<Integer>> data() {
		return new ResponseEntity<List<Integer>>(applicationService.getDashData(),HttpStatus.OK);

	}
	
	

	@GetMapping("/all/users")
	public ResponseEntity<List<AdminUser>> getUSerss() {
		return new ResponseEntity<List<AdminUser>>(applicationService.getUsers(),HttpStatus.OK);

	}
	
	@GetMapping("/all/recent")
	public ResponseEntity<List<Applicant>> getRecent() {
		return new ResponseEntity<List<Applicant>>(applicationService.getREcebt(),HttpStatus.OK);

	}

	@GetMapping("/application")
	public Optional<Applicant> getApplication(@RequestParam long id) {
		return applicationService.getApplication(id);

	}
}
