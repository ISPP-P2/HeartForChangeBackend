package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.ComplementaryFormationDTO;


@Service
public interface ComplementaryFormationService {
	List<ComplementaryFormationDTO> getAllComplementaryFormations();
	
	ComplementaryFormationDTO getComplementaryFormationById(Long id, String token);
	
	
	List<ComplementaryFormationDTO> getComplementaryFormationByVolunteer(String username, String token);
	
	
	List<ComplementaryFormationDTO> getComplementaryFormationByBeneficiary(String username, 
			String token);
	

	ComplementaryFormationDTO saveComplementaryFormation(ComplementaryFormationDTO 
			complementaryFormationDTO, String token);

	
	void deleteComplementaryFormation(Long id, String token);
	

	ComplementaryFormationDTO updateComplementaryFormation(String token, 
			ComplementaryFormationDTO complementaryFormationDTO);
	
	
	
	

	
	




	
}
