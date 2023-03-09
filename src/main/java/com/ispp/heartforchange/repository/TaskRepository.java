package com.ispp.heartforchange.repository;


import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByOng(Ong ong);

}
