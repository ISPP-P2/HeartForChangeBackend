package com.ispp.heartforchange.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.AcademicExperience;

@Repository
public interface AcademicExperienceRepository extends JpaRepository<AcademicExperience, Long>{

	@Query("select a from AcademicExperience a where a.volunteer.username = :usernameSearched")
	Optional<List<AcademicExperience>> findByVolunteer(String usernameSearched);
	
	@Query("select a from AcademicExperience a where a.beneficiary.username = :usernameSearched")
	Optional<List<AcademicExperience>> findByBeneficiary(String usernameSearched);


}
