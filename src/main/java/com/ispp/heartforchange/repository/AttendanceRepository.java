package com.ispp.heartforchange.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispp.heartforchange.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

	Optional<Attendance> findByPersonIdAndTaskId(Long idTask, Long idPerson);

}
