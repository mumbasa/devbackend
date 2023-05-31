package dev.ops.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.ops.app.configuration.FileStorageProperties;

@SpringBootApplication
@EnableJpaRepositories("dev.ops.app.repository")

public class DevopsApplication {
	
	@Value("${file.upload-dir}")
	String UPLOAD_FOLDER;
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}
	
	@Bean
	public FileStorageProperties getFileStorage() {

		return new FileStorageProperties();

	}

	public static void main(String[] args) {
		SpringApplication.run(DevopsApplication.class, args);
	}

}
