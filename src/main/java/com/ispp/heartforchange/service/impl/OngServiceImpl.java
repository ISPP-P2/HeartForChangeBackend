package com.ispp.heartforchange.service.impl;

import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aeat.valida.Validador;
import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.service.OngService;

@Service
public class OngServiceImpl implements OngService{

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	
	/*
	 * Dependency injection 
	 */
	public OngServiceImpl(ONGRepository ongRepository) {
		super();
		this.ongRepository = ongRepository;
	}
	
	@Override
	public List<OngDTO> getAllOngs() {
		List<Ong> ongs = ongRepository.findAll();
		List<OngDTO> ongsDTOs = new ArrayList<>();
		for(Ong ong: ongs) {
			OngDTO ongDTO = new OngDTO(ong);
			ongsDTOs.add(ongDTO);
		}
		return ongsDTOs;
	}

	@Override
	public OngDTO getOngById(Long id) {
		Optional<Ong> optOng = ongRepository.findById(id);
		Ong ong = optOng.get();
		if(!optOng.isPresent()) {
			throw new IllegalArgumentException("This id does not refer to any ONG.");
		}
		OngDTO ongDTO = new OngDTO(ong);
		return ongDTO;


	}

	@Override
	public OngDTO saveOng(OngDTO ongDTO) {
		Ong ong = new Ong(ongDTO);
		Validador validador = new Validador();
		if(validador.checkNif(ongDTO.getCif()) > 0) {
			Ong ongSaved = ongRepository.save(ong);
			return new OngDTO(ongSaved);
		}else{
			throw new IllegalArgumentException("Invalid CIF, try again.");
		}

	}

	@Override
	public OngDTO updateOng(OngDTO newOngDTO) {
		Long id = newOngDTO.getId();
		OngDTO ongToUpdate = getOngById(id);
		ongToUpdate = newOngDTO;
		Ong ong = new Ong(ongToUpdate);
		Validador validador = new Validador();
		if(validador.checkNif(newOngDTO.getCif()) > 0) {
			Ong ongSaved = ongRepository.save(ong);
			return new OngDTO(ongSaved);
		}else{
			throw new IllegalArgumentException("Invalid CIF, try again.");
		}
	}

	@Override
	public void deleteOng(OngDTO ongDTO) {
		Ong ong = new Ong(ongDTO);
		ongRepository.delete(ong);	
	}
	
	
}
