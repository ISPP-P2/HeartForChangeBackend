package com.ispp.heartforchange.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AcademicExperienceDTO;

@Service
public interface AcademicExperienceService {
	
	List<AcademicExperienceDTO> getAllAcademicExp();
	AcademicExperienceDTO saveAcademicExperience(AcademicExperienceDTO academicExperienceDTO, String username);
	AcademicExperienceDTO getAcademicExpByID(Long id, String token);
	List<AcademicExperienceDTO> getAcademicExperienceByVolunteerUsername(String volunteerSearched, String token);
	List<AcademicExperienceDTO> getAcademicExperienceByBeneficiaryUsername(String beneficiaryUserName, String token);
	AcademicExperienceDTO updateAcademicExperience(@Valid AcademicExperienceDTO academicExperienceDTO, String jwt);
	void deleteAcademicExperience(Long id, String token);
}
