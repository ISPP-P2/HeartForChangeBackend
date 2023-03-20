package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.ComplementaryFormationDTO;
import com.ispp.heartforchange.expections.OperationNotAllowedException;


@Service
public interface ComplementaryFormationService {
	
	ComplementaryFormationDTO getComplementaryFormationById(Long id, String token) throws OperationNotAllowedException;
	
	
	List<ComplementaryFormationDTO> getComplementaryFormationByVolunteer(Long volunteerId, String token) throws OperationNotAllowedException;
	
	
	List<ComplementaryFormationDTO> getComplementaryFormationByBeneficiary(Long beneficiaryId, 
			String token) throws OperationNotAllowedException;
	

	ComplementaryFormationDTO saveComplementaryFormation(ComplementaryFormationDTO 
			complementaryFormationDTO, Long id, String token) throws OperationNotAllowedException;

	
	void deleteComplementaryFormation(Long id, String token) throws OperationNotAllowedException;
	

	ComplementaryFormationDTO updateComplementaryFormation(String token, 
			ComplementaryFormationDTO complementaryFormationDTO, Long id) throws OperationNotAllowedException;
	
	
	
	

	
	




	
}
