package com.ispp.heartforchange.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.ispp.heartforchange.dto.AcademicExperienceDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;

@Service
public interface AcademicExperienceService {
	
	AcademicExperienceDTO getAcademicExpByID(Long id, String token) throws OperationNotAllowedException;
				
	List<AcademicExperienceDTO> getAcademicExperienceByVolunteer(Long volunteerId, String token)
			throws OperationNotAllowedException;
	
	List<AcademicExperienceDTO> getAcademicExperienceByBeneficiary(Long beneficiaryId, String token)
			throws OperationNotAllowedException;
	
	AcademicExperienceDTO saveAcademicExperience(AcademicExperienceDTO academicExperienceDTO, Long id, String token)
			throws OperationNotAllowedException;

	AcademicExperienceDTO updateAcademicExperience(AcademicExperienceDTO academicExperienceDTO, String token, Long id)
			throws OperationNotAllowedException;

	void deleteAcademicExperience(Long id, String token) throws OperationNotAllowedException;


}
