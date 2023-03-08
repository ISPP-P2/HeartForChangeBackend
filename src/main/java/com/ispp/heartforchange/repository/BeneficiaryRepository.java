package com.ispp.heartforchange.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ispp.heartforchange.entity.Beneficiary;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>{

	@Query("Select distinct b from Beneficiary b, Person p where p.ong.username = :username")
	List<Beneficiary> findBeneficiariesByOng(String username);
}
