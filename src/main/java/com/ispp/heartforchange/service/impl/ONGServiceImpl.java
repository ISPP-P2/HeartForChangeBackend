package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.ONGDTO;
import com.ispp.heartforchange.entity.ONG;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.service.ONGService;

@Service
public class ONGServiceImpl implements ONGService{

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	
	/*
	 * Dependency injection 
	 */
	public ONGServiceImpl(ONGRepository ongRepository) {
		super();
		this.ongRepository = ongRepository;
	}
	
	@Override
	public List<ONGDTO> getAllONGs() {
		List<ONG> ongs = ongRepository.findAll();
		List<ONGDTO> ongsDTOs = new ArrayList<>();
		for(ONG ong: ongs) {
			ONGDTO ongDTO = new ONGDTO(ong);
			ongsDTOs.add(ongDTO);
		}
		return ongsDTOs;
	}

	@Override
	public ONGDTO getONGbyId(Long id) {
		Optional<ONG> optOng = ongRepository.findById(id);
		try {
			ONG ong = optOng.get();
			ONGDTO ongDTO = new ONGDTO(ong);
			return ongDTO;
		}catch(Exception e) {
			throw new IllegalArgumentException("This id does not refer to any ONG.");
		}

	}

	@Override
	public void saveONG(ONG ong) {
		ongRepository.save(ong);
	}

	@Override
	public void updateONG(ONG ong) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteONG(ONG ong) {
		ongRepository.delete(ong);	
	}
	
}
