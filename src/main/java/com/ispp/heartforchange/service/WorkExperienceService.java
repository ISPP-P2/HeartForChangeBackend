package com.ispp.heartforchange.service;

 import java.util.List;

 import com.ispp.heartforchange.dto.WorkExperienceDTO;

 public interface WorkExperienceService {

 	List<WorkExperienceDTO> getAllWorkExperiences();

 	WorkExperienceDTO getWorkExperienceById(Long id, String token);

 	List<WorkExperienceDTO> getWorkExperienceByVolunteerUsername(String volunteerUserName, String token);

 	List<WorkExperienceDTO> getWorkExperienceByBeneficiaryUsername(String beneficiaryUserName, String token);

 	WorkExperienceDTO saveWorkExperience(WorkExperienceDTO workExperienceDTO, String token);

 	WorkExperienceDTO updateWorkExperience(String token, WorkExperienceDTO workExperienceDTO);

 	void deleteWorkExperience(Long id, String token);

 }