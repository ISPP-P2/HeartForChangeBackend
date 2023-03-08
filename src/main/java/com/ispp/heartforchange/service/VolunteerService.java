package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.VolunteerDTO;

@Service
public interface VolunteerService {

	List<VolunteerDTO> getAllVolunteers();
	
	VolunteerDTO getVolunteerById(Long id);
	
	List<VolunteerDTO> getVolunteersByOng(String username);
	
	VolunteerDTO saveVolunteer(VolunteerDTO volunteerDTO, String username);
	
	VolunteerDTO updateVolunteer(Long id, VolunteerDTO volunteerDTO);
	
	void deleteVolunteer(Long id);
}
