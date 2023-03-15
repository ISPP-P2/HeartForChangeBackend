package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;

@Service
public interface OngService {
	List<OngDTO> getAllOngs();

	OngDTO getOngById(Long id, String token) throws OperationNotAllowedException;
	
	OngDTO saveOng(OngDTO ongDTO) throws OperationNotAllowedException;
	
	OngDTO updateOng(Long id, String token, OngDTO ongDTO) throws OperationNotAllowedException;
	
	void deleteOng(Long id, String token) throws OperationNotAllowedException;
}
