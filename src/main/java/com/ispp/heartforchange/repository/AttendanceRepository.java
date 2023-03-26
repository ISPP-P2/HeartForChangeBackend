package com.ispp.heartforchange.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.PetitionState;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
	
	
	@Query("Select a from Attendance a where a.task.id = :idTask AND a.person.id = :idPerson")
	Optional<Attendance> findByPersonIdAndTaskId(Long idTask, Long idPerson);

	List<Attendance> findByTaskIdAndState(Long id, PetitionState aceptada);

	Optional<Attendance> findByPersonId(Long id);

	List<Attendance> findByTaskId(Long id);

}
