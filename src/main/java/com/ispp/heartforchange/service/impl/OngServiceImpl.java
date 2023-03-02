package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public OngDTO getOngbyId(Long id) {
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
		OngDTO ongToUpdate = getOngbyId(id);
		ongToUpdate = newOngDTO;
		Ong ong = new Ong(ongToUpdate);
		Ong ongSaved = ongRepository.save(ong);
		return new OngDTO(ongSaved);
	}

	@Override
	public void deleteOng(OngDTO ongDTO) {
		Ong ong = new Ong(ongDTO);
		ongRepository.delete(ong);	
	}
	
	/*public boolean validateCif(String cif) {
		boolean res = false;
		cif = cif.toUpperCase();
		if(cif.length()==9) {
			int suma = Integer.valueOf(cif.charAt(2)) + Integer.valueOf(cif.charAt(4)) + Integer.valueOf(cif.charAt(6));
			logger.info("Sum of pair numbers of CIF = {}", suma);
			for(int i=0; i<3; i++) {
				suma = suma + (2*Integer.valueOf(cif.charAt(2*i))%10) + (2*Integer.valueOf(cif.charAt(2*i))/10);
				logger.info("Sum of pair and none numbers of CIF = {}", suma);
			}
			if(cif.charAt(0)=='P' || cif.charAt(0)=='X') {
				
				
				logger.info("P or X letter of CIF = Yes");
				
			
				res = (cif.charAt(8) == (char)(65+10-(suma%10)));
				logger.info("Last char of CIF = {}", cif.charAt(8));
				logger.info("Last char of CIF calculated = {}", (char)(64+10-(suma%10)));
			}else {
				logger.info("P or X letter of CIF = No");
				try {
					res = (Integer.valueOf(""+cif.charAt(8)).equals(Integer.valueOf(10-(suma%10))));
				}catch(NumberFormatException e) {
					logger.info("Number Format Exception");
				}
			
				logger.info("Result = {}", res);
				logger.info("Last number of CIF = {}", cif.charAt(8));
				logger.info("Last number of CIF calculated = {}", (10-(suma%10)));
			}
		}
		logger.info("Result = {}", res);
		return res;
	}*/
	
	
}
