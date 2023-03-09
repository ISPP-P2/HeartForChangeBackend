package com.ispp.heartforchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
	Person findByUsername(String username);

}
