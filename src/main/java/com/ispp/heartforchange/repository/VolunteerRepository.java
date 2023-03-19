package com.ispp.heartforchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.Volunteer;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long>{

	@Query("Select Distinct v from Volunteer v, Person p where p.ong.username = :username")
	List<Volunteer> findVolunteersByOng(String username);
	
	@Query("Select v from Volunteer v where v.username = :username")
 	Volunteer findVolunteerByUsername(String username);
}
