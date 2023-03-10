package com.ispp.heartforchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.Ong;

@Repository
public interface ONGRepository extends JpaRepository<Ong, Long> {
	
	Ong findByUsername(String username);


}
