package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.BeneficiaryDTO;

@Service
public interface BeneficiaryService {
	List<BeneficiaryDTO> getAllBeneficiares();
	
	BeneficiaryDTO getBeneficiaryById(Long id);
	
	
	List<BeneficiaryDTO> getBeneficiaryByOng(Long id);
	
	BeneficiaryDTO saveBeneficiary(BeneficiaryDTO beneficiaryDTO);
	
	
	
	BeneficiaryDTO updateBeneficiary(Long id, BeneficiaryDTO beneficiaryDTO);
	
	
	void deteleBeneficiary(Long id);
	
}
