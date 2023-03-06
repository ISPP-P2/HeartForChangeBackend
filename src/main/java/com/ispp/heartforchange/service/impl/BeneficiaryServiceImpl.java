package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.service.AccountDetailsServiceImpl;
import com.ispp.heartforchange.service.BeneficiaryService;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService{

	

	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	private BeneficiaryRepository beneficiaryRepository;
	private PasswordEncoder encoder;
	
	public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository,ONGRepository ongRepository, PasswordEncoder encoder,
			AccountRepository accountRepository) {
		super();
		this.ongRepository = ongRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.encoder = encoder;
	}
	
	@Override
	public List<BeneficiaryDTO> getAllBeneficiares() {
		List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();
		List<BeneficiaryDTO> beneficiariesDTOs = new ArrayList<>();
		for(Beneficiary beneficiary: beneficiaries) {
			BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(beneficiary, 
					beneficiary.getId(), 
					beneficiary.getNationality(), 
					beneficiary.isDoublenationality(), 
					beneficiary.getArrived_date(), 
					beneficiary.isEuropean_citizen_authorization(), 
					beneficiary.isTourist_visa(), 
					beneficiary.getDate_tourist_visa(), 
					beneficiary.isHealth_card(), 
					beneficiary.getEmployment_sector(), 
					beneficiary.getPerception_aid(), 
					beneficiary.isSavings_possesion(),
					beneficiary.isSae_inscription(),
					beneficiary.isWorking(), 
					beneficiary.isComputer_knowledge(),
					beneficiary.getOwned_devices(), 
					beneficiary.getLanguages());
			beneficiariesDTOs.add(beneficiaryDTO);
		}
		return beneficiariesDTOs;
	}

	@Override
	public BeneficiaryDTO getBeneficiaryById(Long id) {
		Optional<Beneficiary> optBenficiary = beneficiaryRepository.findById(id);
		
		if(!optBenficiary.isPresent()) {
			throw new UsernameNotFoundException("This ONG not exist!");
		}
		Beneficiary beneficiary = optBenficiary.get();
		BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(beneficiary, 
				beneficiary.getId(), 
				beneficiary.getNationality(), 
				beneficiary.isDoublenationality(), 
				beneficiary.getArrived_date(), 
				beneficiary.isEuropean_citizen_authorization(), 
				beneficiary.isTourist_visa(), 
				beneficiary.getDate_tourist_visa(), 
				beneficiary.isHealth_card(), 
				beneficiary.getEmployment_sector(), 
				beneficiary.getPerception_aid(), 
				beneficiary.isSavings_possesion(),
				beneficiary.isSae_inscription(),
				beneficiary.isWorking(), 
				beneficiary.isComputer_knowledge(),
				beneficiary.getOwned_devices(), 
				beneficiary.getLanguages());
		return beneficiaryDTO;
	}

	@Override
	public List<BeneficiaryDTO> getBeneficiaryByOng(Long id) {
		// TODO Auto-generated method stub
		List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();
		List<BeneficiaryDTO> beneficiariesDTOs = new ArrayList<>();
		for(Beneficiary beneficiary: beneficiaries) {
			BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(beneficiary, 
					beneficiary.getId(), 
					beneficiary.getNationality(), 
					beneficiary.isDoublenationality(), 
					beneficiary.getArrived_date(), 
					beneficiary.isEuropean_citizen_authorization(), 
					beneficiary.isTourist_visa(), 
					beneficiary.getDate_tourist_visa(), 
					beneficiary.isHealth_card(), 
					beneficiary.getEmployment_sector(), 
					beneficiary.getPerception_aid(), 
					beneficiary.isSavings_possesion(),
					beneficiary.isSae_inscription(),
					beneficiary.isWorking(), 
					beneficiary.isComputer_knowledge(),
					beneficiary.getOwned_devices(), 
					beneficiary.getLanguages());
			beneficiariesDTOs.add(beneficiaryDTO);
		}
		return beneficiariesDTOs;
	}

	@Override
	public BeneficiaryDTO saveBeneficiary(BeneficiaryDTO beneficiaryDTO, String username ) {
		System.out.println(username);
		Ong ong = ongRepository.findByUsername(username);

		
		Beneficiary beneficiary = new Beneficiary(beneficiaryDTO, 
				beneficiaryDTO.getNationality(), 
				beneficiaryDTO.isDoublenationality(), 
				beneficiaryDTO.getArrived_date(), 
				beneficiaryDTO.isEuropean_citizen_authorization(), 
				beneficiaryDTO.isTourist_visa(), 
				beneficiaryDTO.getDate_tourist_visa(), 
				beneficiaryDTO.isHealth_card(), 
				beneficiaryDTO.getEmployment_sector(), 
				beneficiaryDTO.getPerception_aid(), 
				beneficiaryDTO.isSavings_possesion(),
				beneficiaryDTO.isSae_inscription(),
				beneficiaryDTO.isWorking(), 
				beneficiaryDTO.isComputer_knowledge(),
				beneficiaryDTO.getOwned_devices(), 
				beneficiaryDTO.getLanguages());
		beneficiary.setId(Long.valueOf(0));
		beneficiary.setRolAccount(RolAccount.BENEFICIARY);
		beneficiary.setPassword(encoder.encode(beneficiary.getPassword()));
		logger.info("Saving Beneficiary with username={}", beneficiary.getUsername());
		
		//Guardar ONG en beneficiario
		beneficiary.setOng(ong);
		
		
		try {
			Beneficiary beneficiarySaved = beneficiaryRepository.save(beneficiary);
			return new BeneficiaryDTO(beneficiary, 
					beneficiarySaved.getId(), 
					beneficiarySaved.getNationality(), 
					beneficiarySaved.isDoublenationality(), 
					beneficiarySaved.getArrived_date(), 
					beneficiarySaved.isEuropean_citizen_authorization(), 
					beneficiarySaved.isTourist_visa(), 
					beneficiarySaved.getDate_tourist_visa(), 
					beneficiarySaved.isHealth_card(), 
					beneficiarySaved.getEmployment_sector(), 
					beneficiarySaved.getPerception_aid(), 
					beneficiarySaved.isSavings_possesion(),
					beneficiarySaved.isSae_inscription(),
					beneficiarySaved.isWorking(), 
					beneficiarySaved.isComputer_knowledge(),
					beneficiarySaved.getOwned_devices(), 
					beneficiarySaved.getLanguages());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public BeneficiaryDTO updateBeneficiary(Long id, BeneficiaryDTO newBeneficiaryDTO) {
		Optional<Beneficiary> beneficiaryToUpdate = beneficiaryRepository.findById(id);
		logger.info("Beneficiary is updating with id={}", id);
		if(beneficiaryToUpdate.isPresent()) {
			beneficiaryToUpdate.get().setPassword(encoder.encode(newBeneficiaryDTO.getPassword()));
			beneficiaryToUpdate.get().setAddress(newBeneficiaryDTO.getAddress());
			beneficiaryToUpdate.get().setBirthday(newBeneficiaryDTO.getBirthday());
			beneficiaryToUpdate.get().setCivilStatus(newBeneficiaryDTO.getCivilStatus());
			beneficiaryToUpdate.get().setDocumentNumber(newBeneficiaryDTO.getDocumentNumber());
			beneficiaryToUpdate.get().setDocumentType(newBeneficiaryDTO.getDocumentType());
			beneficiaryToUpdate.get().setDriveLicenses(newBeneficiaryDTO.getDriveLicenses());
			beneficiaryToUpdate.get().setEmail(newBeneficiaryDTO.getEmail());
			beneficiaryToUpdate.get().setEntryDate(newBeneficiaryDTO.getEntryDate());
			beneficiaryToUpdate.get().setFirstSurname(newBeneficiaryDTO.getFirstSurname());
			beneficiaryToUpdate.get().setSecondSurname(newBeneficiaryDTO.getSecondSurname());
			beneficiaryToUpdate.get().setGender(newBeneficiaryDTO.getGender());
			beneficiaryToUpdate.get().setLeavingDate(newBeneficiaryDTO.getLeavingDate());
			beneficiaryToUpdate.get().setName(newBeneficiaryDTO.getName());
			beneficiaryToUpdate.get().setNumberOfChildren(newBeneficiaryDTO.getNumberOfChildren());
			beneficiaryToUpdate.get().setOtherSkills(newBeneficiaryDTO.getOtherSkills());
			beneficiaryToUpdate.get().setPostalCode(newBeneficiaryDTO.getPostalCode());
			beneficiaryToUpdate.get().setRegistrationAddress(newBeneficiaryDTO.getRegistrationAddress());
			beneficiaryToUpdate.get().setTelephone(newBeneficiaryDTO.getTelephone());
			beneficiaryToUpdate.get().setTown(newBeneficiaryDTO.getTown());
			beneficiaryToUpdate.get().setUsername(newBeneficiaryDTO.getUsername());
			beneficiaryToUpdate.get().setNationality(newBeneficiaryDTO.getNationality());
			beneficiaryToUpdate.get().setDoublenationality(newBeneficiaryDTO.isDoublenationality());
			beneficiaryToUpdate.get().setArrived_date(newBeneficiaryDTO.getArrived_date());
			beneficiaryToUpdate.get().setEuropean_citizen_authorization(newBeneficiaryDTO.isEuropean_citizen_authorization());
			beneficiaryToUpdate.get().setTourist_visa(newBeneficiaryDTO.isTourist_visa());
			beneficiaryToUpdate.get().setDate_tourist_visa(newBeneficiaryDTO.getDate_tourist_visa());
			beneficiaryToUpdate.get().setHealth_card(newBeneficiaryDTO.isHealth_card());
			beneficiaryToUpdate.get().setEmployment_sector(newBeneficiaryDTO.getEmployment_sector());
			beneficiaryToUpdate.get().setPerception_aid(newBeneficiaryDTO.getPerception_aid());
			beneficiaryToUpdate.get().setSavings_possesion(newBeneficiaryDTO.isSavings_possesion());
			beneficiaryToUpdate.get().setWorking(newBeneficiaryDTO.isWorking());
			beneficiaryToUpdate.get().setComputer_knowledge(newBeneficiaryDTO.isComputer_knowledge());
			beneficiaryToUpdate.get().setOwned_devices(newBeneficiaryDTO.getOwned_devices());
			beneficiaryToUpdate.get().setLanguages(newBeneficiaryDTO.getLanguages());
		}else {
			throw new UsernameNotFoundException("This Beneficiary not exist!");

		}		try {
			Beneficiary beneficiarySaved =  beneficiaryRepository.save(beneficiaryToUpdate.get());
			return new BeneficiaryDTO(beneficiarySaved, 
					beneficiarySaved.getId(), 
					beneficiarySaved.getNationality(), 
					beneficiarySaved.isDoublenationality(), 
					beneficiarySaved.getArrived_date(), 
					beneficiarySaved.isEuropean_citizen_authorization(), 
					beneficiarySaved.isTourist_visa(), 
					beneficiarySaved.getDate_tourist_visa(), 
					beneficiarySaved.isHealth_card(), 
					beneficiarySaved.getEmployment_sector(), 
					beneficiarySaved.getPerception_aid(), 
					beneficiarySaved.isSavings_possesion(),
					beneficiarySaved.isSae_inscription(),
					beneficiarySaved.isWorking(), 
					beneficiarySaved.isComputer_knowledge(),
					beneficiarySaved.getOwned_devices(), 
					beneficiarySaved.getLanguages());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<BeneficiaryDTO> getAllBeneficiaresByOng(String username){
		
		List<Beneficiary> beneficiaries = beneficiaryRepository.findBeneficiariesByOng(username);
		List<BeneficiaryDTO> beneficiariesDTOs = new ArrayList<>();
		for(Beneficiary beneficiary: beneficiaries) {
			BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(beneficiary, 
					beneficiary.getId(), 
					beneficiary.getNationality(), 
					beneficiary.isDoublenationality(), 
					beneficiary.getArrived_date(), 
					beneficiary.isEuropean_citizen_authorization(), 
					beneficiary.isTourist_visa(), 
					beneficiary.getDate_tourist_visa(), 
					beneficiary.isHealth_card(), 
					beneficiary.getEmployment_sector(), 
					beneficiary.getPerception_aid(), 
					beneficiary.isSavings_possesion(),
					beneficiary.isSae_inscription(),
					beneficiary.isWorking(), 
					beneficiary.isComputer_knowledge(),
					beneficiary.getOwned_devices(), 
					beneficiary.getLanguages());
			
			beneficiariesDTOs.add(beneficiaryDTO);
		}
		return beneficiariesDTOs;
	}
	
	@Override
 	public void deteleBeneficiary(Long id) {
		logger.info("Deleting Beneficiary with id={}", id);
		BeneficiaryDTO beneficiaryDTO = getBeneficiaryById(id);
		Beneficiary beneficiaryToDelete = new Beneficiary(beneficiaryDTO, 
				beneficiaryDTO.getNationality(), 
				beneficiaryDTO.isDoublenationality(), 
				beneficiaryDTO.getArrived_date(), 
				beneficiaryDTO.isEuropean_citizen_authorization(), 
				beneficiaryDTO.isTourist_visa(), 
				beneficiaryDTO.getDate_tourist_visa(), 
				beneficiaryDTO.isHealth_card(), 
				beneficiaryDTO.getEmployment_sector(), 
				beneficiaryDTO.getPerception_aid(), 
				beneficiaryDTO.isSavings_possesion(),
				beneficiaryDTO.isSae_inscription(),
				beneficiaryDTO.isWorking(), 
				beneficiaryDTO.isComputer_knowledge(),
				beneficiaryDTO.getOwned_devices(), 
				beneficiaryDTO.getLanguages());
		beneficiaryToDelete.setId(id);
		try {
			beneficiaryRepository.delete(beneficiaryToDelete);	
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}		
		
	}

}
