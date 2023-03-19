package com.ispp.heartforchange.repository;

 import java.util.List;
 import java.util.Optional;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.stereotype.Repository;
 import com.ispp.heartforchange.entity.WorkExperience;


 @Repository
 public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long>{

 	@Query("Select w from WorkExperience w where w.volunteer.id = :id")
 	Optional<List<WorkExperience>> findWorkExperienceByVolunteerId(Long id);

 	@Query("Select w from WorkExperience w where w.beneficiary.id = :id")
 	Optional<List<WorkExperience>> findWorkExperienceByBeneficiaryId(Long id);
 }