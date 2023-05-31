package dev.ops.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.ops.app.models.Applicant;

@Repository
public interface ApplicationRepository extends JpaRepository<Applicant, Long> {
	@Query(value = "SELECT * FROM applicant ORDER BY id DESC LIMIT 5",nativeQuery = true)
	List<Applicant> findLast5();

}
