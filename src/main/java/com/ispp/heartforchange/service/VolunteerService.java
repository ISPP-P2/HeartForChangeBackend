package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.UpdatePasswordDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;

@Service
public interface VolunteerService {

	
	VolunteerDTO getVolunteerById(Long id, String username);
	
	List<VolunteerDTO> getVolunteersByOng(String username);
	
	Integer getNumberOfVolunteersByOng(String username);

	VolunteerDTO saveVolunteer(VolunteerDTO volunteerDTO, String token);
	
	VolunteerDTO updateVolunteer(Long id, VolunteerDTO volunteerDTO, String username);
	
	VolunteerDTO updateVolunteerPassword(Long id, UpdatePasswordDTO passwordDTO, String token);
	
	void deleteVolunteer(Long id, String username);
}
