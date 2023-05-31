package dev.ops.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.ops.app.models.AdminUser;
import dev.ops.app.models.Applicant;
import dev.ops.app.repository.AdminUserRepository;
import dev.ops.app.repository.ApplicationRepository;

@Service
public class ApplicationService {
	@Autowired
	ApplicationRepository applicationRepository;
	@Autowired
	AdminUserRepository adminUserRepository;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	JdbcTemplate template;

	@Value("${file.upload-dir}")
	String folder;

	public List<Applicant> getApplications() {
		return applicationRepository.findAll();
	}

	public Applicant saveApplication(String name, String phone, String email, MultipartFile file) {
		
		Applicant applicant = new Applicant();
		applicant.setName(name);
		applicant.setEmail(email);
		applicant.setPhone(phone);
		setFile(applicant, file);
		applicant.setDateAdded(LocalDateTime.now().toString());
		return applicationRepository.save(applicant);
	}

	public Optional<Applicant> getApplication(long id) {
		return applicationRepository.findById(id);
	}

	public AdminUser saveUser(AdminUser user) {
		System.err.println(user.getName());
		user.setDateAdded(LocalDate.now().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return adminUserRepository.save(user);
	}

	public List<AdminUser> getUsers() {
		return adminUserRepository.findAll();
	}

	public List<Integer> getDashData() {
		String sql = "SELECT count(*) FROM applicant UNION ALL \r\n"
				+ "SELECT count(*) FROM applicant WHERE MONTH(date_added)=MONTH(curdate()) UNION ALL \r\n"
				+ "SELECT count(*) FROM applicant WHERE MONTH(date_added)=MONTH(curdate())-1 UNION ALL \r\n"
				+ "SELECT count(*) FROM applicant WHERE cv='uploaded file' UNION ALL SELECT count(*) FROM applicant WHERE date_added=curdate()";

		return template.queryForList(sql, Integer.class);
	}

	public List<Applicant> getREcebt() {
		return applicationRepository.findLast5();
	}

	public boolean setFile(Applicant applicant, MultipartFile file) {
		String fileName = "applicant" + "-" + applicant.getName() + file.getOriginalFilename();
		Path path = Paths.get(folder + fileName);
		try {
		    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			applicant.setCv(fileName);

			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;

		}

	}

}
