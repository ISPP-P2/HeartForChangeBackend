package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.BeneficiaryDTO;


@Service
public interface BeneficiaryService {
	
	BeneficiaryDTO getBeneficiaryById(Long id, String username);
	
	
	
	
	List<BeneficiaryDTO> getAllBeneficiaresByOng(String username);

	
	BeneficiaryDTO updateBeneficiary(Long id, BeneficiaryDTO beneficiaryDTO, String username);
	
	
	void deteleBeneficiary(Long id, String username);

	BeneficiaryDTO saveBeneficiary(BeneficiaryDTO beneficiaryDTO, String username );
	
}
