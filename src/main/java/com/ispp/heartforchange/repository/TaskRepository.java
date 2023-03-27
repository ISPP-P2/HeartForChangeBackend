package com.ispp.heartforchange.repository;


import com.ispp.heartforchange.dto.AttendanceDTO;
import org.springframework.stereotype.Repository;

import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByOng(Ong ong);

	@Query("SELECT DISTINCT t FROM Task t where t.ong = :loggedOng and t.type = com.ispp.heartforchange.entity.TaskType.ACTIVIDAD")
	List<Task> findyByOngAndActivity(Ong loggedOng);
	

	@Query("SELECT DISTINCT t FROM Task t where t.ong = :loggedOng and t.type = com.ispp.heartforchange.entity.TaskType.CURSO")
	List<Task> findyByOngAndCurso(Ong loggedOng);
	

	@Query("SELECT DISTINCT t FROM Task t where t.ong = :loggedOng and t.type = com.ispp.heartforchange.entity.TaskType.TALLER")
	List<Task> findyByOngAndTaller(Ong loggedOng);

	
	//@Query("SELECT DISTINCT t FROM Task t where t.ong = :loggedOng and ")
	List<Task> findByOngAndDateAfterAndType(Ong loggedOng, LocalDateTime now, TaskType type);

}
