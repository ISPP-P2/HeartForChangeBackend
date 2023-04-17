package com.ispp.heartforchange.service;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;

@Service
public interface OngService {

	OngDTO getOng(String token) throws OperationNotAllowedException;
	
	OngDTO saveOng(OngDTO ongDTO, String jwt) throws OperationNotAllowedException;
	
	OngDTO updateOng(String token, OngDTO ongDTO) throws OperationNotAllowedException;
	
	void deleteOng(String token) throws OperationNotAllowedException;
}
