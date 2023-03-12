package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.OngDTO;

@Service
public interface OngService {
	List<OngDTO> getAllOngs();

	OngDTO getOngById(Long id);
	
	OngDTO saveOng(OngDTO ongDTO);
	
	OngDTO updateOng(Long id, OngDTO ongDTO);
	
	void deleteOng(Long id);
}
