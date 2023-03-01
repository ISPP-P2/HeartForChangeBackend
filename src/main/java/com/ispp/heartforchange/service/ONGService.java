package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.ONGDTO;
import com.ispp.heartforchange.entity.ONG;

@Service
public interface ONGService {
	List<ONGDTO> getAllONGs();

	ONGDTO getONGbyId(Long id);
	
	void saveONG(ONG ong);
	
	void updateONG(ONG ong);
	
	void deleteONG(ONG ong);
}
