package com.ispp.heartforchange.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ispp.heartforchange.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

	@Query("Select a from Appointment a where a.beneficiary.ong.id = :id")
	Optional<List<Appointment>> findAppointmentsByOngId(Long id);
	
	@Query("Select a from Appointment a where a.beneficiary.id = :id")
	Optional<List<Appointment>> findAppointmentsByBeneficiaryId(Long id);
	
	@Query("Select a from Appointment a where a.beneficiary.ong.username = :ongSearched")
	Optional<List<Appointment>> findAppointmentsByOngUsername(String ongSearched);
	
	@Query("Select a from Appointment a where a.beneficiary.username = :beneficiarySearched")
	Optional<List<Appointment>> findAppointmentsByBeneficiaryUsername(String beneficiarySearched);
}
