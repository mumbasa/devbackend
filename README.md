# devbackend
This is a test project for a full stack role. The challenge is the create a middleware for a frontend app to access a REST service.
This project was done in java (backend) and ReactJs (frontend). The React Front end is found here https://github.com/mumbasa/devportal .Make sure you have Java, Maven npm  and nodejs installed
To get it up and running clone this repository.( Master branch)

# 1. Spring Boot (Initializer)
To start off with you can use Spring Initializr to get the Spring Boot project structure for you, and this can be found here
Once you get there in this case using Maven. I use maven because of the ease of using the pom.xml file. You also need to add the following

Spring Boot DevTools -Provides fast application restarts, LiveReload, and configurations for enhanced development experience.

Spring Web - Build web, including RESTful, applications using Spring MVC

Lombok - Java annotation library which helps reduce boilerplate code

# 2 REST API Service (JAVA)


# A Models

Applicant Class
```java
package dev.ops.app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table
public class Applicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long Id;
	private String email;
	private String phone;
	private String name;
	private String cv;
	private String dateAdded;

}


```

AdminUser Class

```java
package dev.ops.app.models;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table
public class AdminUser  implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "adminuser")
	private long id;
	private String username;
	private String password;
	private String role;
	private String dateAdded;
	private String name;
	
	@Transient
	private String token;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}


```

# Service
I create a two services one for AdminUser  operations and Applicant Operations.

AdminUserService.class 
ApplicantService class
```java 
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

```





# Running The project
Clone this repository.
On command line enter git clone https://github.com/mumbasa/devbackend.git

cd the folder created

To build the project on the prompt type in `mvn package`

locate the jar file created in the target folder

To run this file type in `java -jar name of the jar file`
To view the app type in http://localhost:1010/ in the brower or Open [http://localhost:1010](http://localhost:10101)
There is a swagger documentation for this restApi Open [http://localhost:8080/swagger-ui/index.html](http://localhost:1010/swagger-ui/index.html)




 
