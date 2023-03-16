package com.ispp.heartforchange.service;

 import java.util.List;

 import com.ispp.heartforchange.dto.WorkExperienceDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;

 public interface WorkExperienceService {

 	List<WorkExperienceDTO> getAllWorkExperiences();

 	WorkExperienceDTO getWorkExperienceById(Long id, String token) throws OperationNotAllowedException;

 	List<WorkExperienceDTO> getWorkExperienceByVolunteer(Long volunteerId, String token) throws OperationNotAllowedException;

 	List<WorkExperienceDTO> getWorkExperienceByBeneficiary(Long beneficiaryId, String token) throws OperationNotAllowedException;

 	WorkExperienceDTO saveWorkExperience(WorkExperienceDTO workExperienceDTO, Long id, String token) throws OperationNotAllowedException;

 	WorkExperienceDTO updateWorkExperience(String token, WorkExperienceDTO workExperienceDTO, Long id) throws OperationNotAllowedException;

 	void deleteWorkExperience(Long id, String token) throws OperationNotAllowedException;

 }