package com.ispp.heartforchange.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.ComplementaryFormation;

@Repository
public interface ComplementaryFormationRepository extends JpaRepository<ComplementaryFormation, Long> {
	
	
	@Query("Select c from ComplementaryFormation c where c.volunteer.id = :id")
	Optional<List<ComplementaryFormation>> findComplementaryFormationByVolunteer(Long id);
	
	@Query("Select c from ComplementaryFormation c where c.beneficiary.id = :id")
	Optional<List<ComplementaryFormation>> findComplementaryFormationByBeneficiary(Long id);
}
