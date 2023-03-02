package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.entity.Ong;

@Service
public interface OngService {
	List<OngDTO> getAllOngs();

	OngDTO getOngById(Long id);
	
	OngDTO saveOng(OngDTO ongDTO);
	
	OngDTO updateOng(OngDTO ongDTO);
	
	void deleteOng(OngDTO ongDTO);
}
