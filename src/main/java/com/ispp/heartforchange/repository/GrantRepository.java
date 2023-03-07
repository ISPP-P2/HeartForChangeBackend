package com.ispp.heartforchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.ispp.heartforchange.entity.Grant;
import com.ispp.heartforchange.entity.Ong;

@Repository
public interface GrantRepository extends JpaRepository<Grant, Long> {
	
	List<Grant> findByOng(Ong ong);
}
