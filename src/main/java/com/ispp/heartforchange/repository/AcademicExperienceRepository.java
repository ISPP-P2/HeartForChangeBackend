package com.ispp.heartforchange.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.AcademicExperience;

@Repository
public interface AcademicExperienceRepository extends JpaRepository<AcademicExperience, Long>{

 	@Query("Select w from AcademicExperience w where w.volunteer.id = :id")
 	Optional<List<AcademicExperience>> findAcademicExperienceByVolunteerId(Long id);

 	@Query("Select w from AcademicExperience w where w.beneficiary.id = :id")
 	Optional<List<AcademicExperience>> findAcademicExperienceByBeneficiaryId(Long id);


}
