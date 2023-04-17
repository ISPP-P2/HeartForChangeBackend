package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.entity.Appointment;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.ComplementaryFormation;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.repository.AcademicExperienceRepository;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.AppointmentRepository;
import com.ispp.heartforchange.repository.AttendanceRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ComplementaryFormationRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.WorkExperienceRepository;
import com.ispp.heartforchange.service.BeneficiaryService;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService{

	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	private BeneficiaryRepository beneficiaryRepository;
	private AppointmentRepository appointmentRepository;
	private AccountRepository accountRepository;
	private WorkExperienceRepository workExperienceRepository;	
	private AcademicExperienceRepository academicExperienceRepository;
	private ComplementaryFormationRepository complementaryFormationRepository;
	private AttendanceRepository attendanceRepository;
	private PasswordEncoder encoder;
	

	public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository,ONGRepository ongRepository, PasswordEncoder encoder, 
		  ComplementaryFormationRepository complementaryFormationRepository, WorkExperienceRepository workExperienceRepository, AccountRepository accountRepository, 
		  AcademicExperienceRepository academicExperienceRepository,AppointmentRepository appointmentRepository, AttendanceRepository attendanceRepository) {
		super();
		this.ongRepository = ongRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.workExperienceRepository = workExperienceRepository;
		this.complementaryFormationRepository = complementaryFormationRepository;
		this.encoder = encoder;
		this.academicExperienceRepository = academicExperienceRepository;
		this.appointmentRepository = appointmentRepository;
		this.accountRepository = accountRepository;
		this.attendanceRepository = attendanceRepository;
	}
	
  


	/*
	 * Get beneficiary by id
	 * @Params Long id
	 * @Return BeneficiaryDTO
	 */
	@Override
	public BeneficiaryDTO getBeneficiaryById(Long id, String username) {
		
		Optional<Beneficiary> optBenficiary = beneficiaryRepository.findById(id);
		
		if(!optBenficiary.isPresent()) {
			throw new UsernameNotFoundException("This Beneficiary not exist!");
		}
		
		//Account of the person who calls the action
		Account account = accountRepository.findByUsername(username);
		Boolean res = null;
		//if calls the action ong, checks that beneficiary is on the ong who calls the action
		if(account.getRolAccount().equals(RolAccount.ONG)) {
			Ong ong = ongRepository.findByUsername(username);
			Ong ongBeneficiary = optBenficiary.get().getOng();
			if(!ong.equals(ongBeneficiary)){
				//If not are the same ong it´s an error
				res = false;
			}else {
				res = true;
			} 
			
		}else if(account.getRolAccount().equals(RolAccount.BENEFICIARY)) {
			Beneficiary beneficiaryById = optBenficiary.get();
			Beneficiary beneficiaryByToken = beneficiaryRepository.findByUsername(username);
			
			if(!beneficiaryById.equals(beneficiaryByToken)) {
				//if not are the same beneficiary it´s an error
				res = false;
			}else {
				res = true;
			}
		}else {
			res = false;
		}
		
		if(res != true) {
			throw new UsernameNotFoundException("You don´t have permissions on this beneficiary");
		}
		
		Beneficiary beneficiary = optBenficiary.get();
		BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(beneficiary, 
				beneficiary.getId(), 
				beneficiary.getNationality(), 
				beneficiary.isDoubleNationality(), 
				beneficiary.getArrivedDate(), 
				beneficiary.isEuropeanCitizenAuthorization(), 
				beneficiary.isTouristVisa(), 
				beneficiary.getDateTouristVisa(), 
				beneficiary.isHealthCard(), 
				beneficiary.getEmploymentSector(), 
				beneficiary.getPerceptionAid(), 
				beneficiary.isSavingsPossesion(),
				beneficiary.isSaeInscription(),
				beneficiary.isWorking(), 
				beneficiary.isComputerKnowledge(),
				beneficiary.getOwnedDevices(), 
				beneficiary.getLanguages());
		return beneficiaryDTO;
		
		
	}

	


	
	/*
	 * Save an beneficiary with their account.
	 * @Params beneficiaryDTO
	 * @Return BeneficiaryDTO
	 */
	@Override
	public BeneficiaryDTO saveBeneficiary(BeneficiaryDTO beneficiaryDTO, String username ) {
		
		if(!accountRepository.findByUsername(username).getRolAccount().equals(RolAccount.ONG)) {
			throw new UsernameNotFoundException("You are not an ONG");
		}

		Ong ong = ongRepository.findByUsername(username);
		
		Beneficiary beneficiary = new Beneficiary(beneficiaryDTO, 
				beneficiaryDTO.getNationality(), 
				beneficiaryDTO.isDoubleNationality(), 
				beneficiaryDTO.getArrivedDate(), 
				beneficiaryDTO.isEuropeanCitizenAuthorization(), 
				beneficiaryDTO.isTouristVisa(), 
				beneficiaryDTO.getDateTouristVisa(), 
				beneficiaryDTO.isHealthCard(), 
				beneficiaryDTO.getEmploymentSector(), 
				beneficiaryDTO.getPerceptionAid(), 
				beneficiaryDTO.isSavingsPossesion(),
				beneficiaryDTO.isSaeInscription(),
				beneficiaryDTO.isWorking(), 
				beneficiaryDTO.isComputerKnowledge(),
				beneficiaryDTO.getOwnedDevices(), 
				beneficiaryDTO.getLanguages());
		beneficiary.setId(Long.valueOf(0));
		beneficiary.setRolAccount(RolAccount.BENEFICIARY);
		beneficiary.setPassword(encoder.encode(beneficiary.getPassword()));
		logger.info("Saving Beneficiary with username={}", beneficiary.getUsername());
		
		//Guardar ONG en beneficiario
		beneficiary.setOng(ong);
		
		
		try {
			Beneficiary beneficiarySaved = beneficiaryRepository.save(beneficiary);
			return new BeneficiaryDTO(beneficiarySaved, 
					beneficiarySaved.getId(), 
					beneficiarySaved.getNationality(), 
					beneficiarySaved.isDoubleNationality(), 
					beneficiarySaved.getArrivedDate(), 
					beneficiarySaved.isEuropeanCitizenAuthorization(), 
					beneficiarySaved.isTouristVisa(), 
					beneficiarySaved.getDateTouristVisa(), 
					beneficiarySaved.isHealthCard(), 
					beneficiarySaved.getEmploymentSector(), 
					beneficiarySaved.getPerceptionAid(), 
					beneficiarySaved.isSavingsPossesion(),
					beneficiarySaved.isSaeInscription(),
					beneficiarySaved.isWorking(), 
					beneficiarySaved.isComputerKnowledge(),
					beneficiarySaved.getOwnedDevices(), 
					beneficiarySaved.getLanguages());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	
	/*
	 * Update beneficiary
	 * @Params Long id
	 * @Params BeneficiaryDTO
	 * @Return BeneficiaryDTO
	 */
	@Override
	public BeneficiaryDTO updateBeneficiary(Long id, BeneficiaryDTO newBeneficiaryDTO, String username) {
		Optional<Beneficiary> beneficiaryToUpdate = beneficiaryRepository.findById(id);
		
		Ong ongByBeneficiary = beneficiaryToUpdate.get().getOng();
		Ong ongByToken = ongRepository.findByUsername(username);

		if(!accountRepository.findByUsername(username).getRolAccount().equals(RolAccount.ONG)) {
			throw new UsernameNotFoundException("You don´t have permissions to update a beneficiary. You are not an ONG");

		}
		
		if(!ongByBeneficiary.equals(ongByToken)) {
			throw new UsernameNotFoundException("This beneficiary don´t belogns to your ONG");
		}
		
		logger.info("Beneficiary is updating with id={}", id);
		if(beneficiaryToUpdate.isPresent()) {
			beneficiaryToUpdate.get().setPassword(encoder.encode("a"));
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
			beneficiaryToUpdate.get().setDoubleNationality(newBeneficiaryDTO.isDoubleNationality());
			beneficiaryToUpdate.get().setArrivedDate(newBeneficiaryDTO.getArrivedDate());
			beneficiaryToUpdate.get().setEuropeanCitizenAuthorization(newBeneficiaryDTO.isEuropeanCitizenAuthorization());
			beneficiaryToUpdate.get().setTouristVisa(newBeneficiaryDTO.isTouristVisa());
			beneficiaryToUpdate.get().setDateTouristVisa(newBeneficiaryDTO.getDateTouristVisa());
			beneficiaryToUpdate.get().setHealthCard(newBeneficiaryDTO.isHealthCard());
			beneficiaryToUpdate.get().setEmploymentSector(newBeneficiaryDTO.getEmploymentSector());
			beneficiaryToUpdate.get().setPerceptionAid(newBeneficiaryDTO.getPerceptionAid());
			beneficiaryToUpdate.get().setSavingsPossesion(newBeneficiaryDTO.isSavingsPossesion());
			beneficiaryToUpdate.get().setWorking(newBeneficiaryDTO.isWorking());
			beneficiaryToUpdate.get().setComputerKnowledge(newBeneficiaryDTO.isComputerKnowledge());
			beneficiaryToUpdate.get().setOwnedDevices(newBeneficiaryDTO.getOwnedDevices());
			beneficiaryToUpdate.get().setLanguages(newBeneficiaryDTO.getLanguages());
		}else {
			throw new UsernameNotFoundException("This Beneficiary not exist!");

		}		try {
			Beneficiary beneficiarySaved =  beneficiaryRepository.save(beneficiaryToUpdate.get());
			return new BeneficiaryDTO(beneficiarySaved, 
					beneficiarySaved.getId(), 
					beneficiarySaved.getNationality(), 
					beneficiarySaved.isDoubleNationality(), 
					beneficiarySaved.getArrivedDate(), 
					beneficiarySaved.isEuropeanCitizenAuthorization(), 
					beneficiarySaved.isTouristVisa(), 
					beneficiarySaved.getDateTouristVisa(), 
					beneficiarySaved.isHealthCard(), 
					beneficiarySaved.getEmploymentSector(), 
					beneficiarySaved.getPerceptionAid(), 
					beneficiarySaved.isSavingsPossesion(),
					beneficiarySaved.isSaeInscription(),
					beneficiarySaved.isWorking(), 
					beneficiarySaved.isComputerKnowledge(),
					beneficiarySaved.getOwnedDevices(), 
					beneficiarySaved.getLanguages());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	/*
	 * get all beneficiaries by ong
	 * @Params String username
	 * @Return List<BeneficiaryDTO>
	 */
	@Override
	public List<BeneficiaryDTO> getAllBeneficiaresByOng(String username){
		if(!accountRepository.findByUsername(username).getRolAccount().equals(RolAccount.ONG)) {
			throw new UsernameNotFoundException("You are not an ONG");

		}
		List<Beneficiary> allBenficiaries = beneficiaryRepository.findAll();
		List<Beneficiary> beneficiaries = new ArrayList<>();
		for(Beneficiary b : allBenficiaries) {
			if(b.getOng().equals(ongRepository.findByUsername(username))) {
				beneficiaries.add(b);
			}
		}
		List<BeneficiaryDTO> beneficiariesDTOs = new ArrayList<>();
		if(beneficiaries.isEmpty()) {
			return beneficiariesDTOs;

		}
		
		
		
		for(Beneficiary beneficiary: beneficiaries) {
			BeneficiaryDTO beneficiaryDTO = new BeneficiaryDTO(beneficiary, 
					beneficiary.getId(), 
					beneficiary.getNationality(), 
					beneficiary.isDoubleNationality(), 
					beneficiary.getArrivedDate(), 
					beneficiary.isEuropeanCitizenAuthorization(), 
					beneficiary.isTouristVisa(), 
					beneficiary.getDateTouristVisa(), 
					beneficiary.isHealthCard(), 
					beneficiary.getEmploymentSector(), 
					beneficiary.getPerceptionAid(), 
					beneficiary.isSavingsPossesion(),
					beneficiary.isSaeInscription(),
					beneficiary.isWorking(), 
					beneficiary.isComputerKnowledge(),
					beneficiary.getOwnedDevices(), 
					beneficiary.getLanguages());
			
			beneficiariesDTOs.add(beneficiaryDTO);
		}
		return beneficiariesDTOs;
	}
	
	@Override
	public Integer getNumberBeneficiaresByOng(String username){
		if(!accountRepository.findByUsername(username).getRolAccount().equals(RolAccount.ONG)) {
			throw new UsernameNotFoundException("You are not an ONG");

		}
		List<Beneficiary> allBenficiaries = beneficiaryRepository.findAll();
		List<Beneficiary> beneficiaries = new ArrayList<>();
		for(Beneficiary b : allBenficiaries) {
			if(b.getOng().equals(ongRepository.findByUsername(username))) {
				beneficiaries.add(b);
			}
		}
		
		if(beneficiaries.isEmpty()) {
			return 0;

		}
		
		return beneficiaries.size();
	}
	
	
	/*
	 * delete beneficiary
	 * @Params Long id
	 * @Return void
	 */
	@Override
 	public void deteleBeneficiary(Long id, String username) {
		logger.info("Deleting Beneficiary with id={}", id);
		BeneficiaryDTO beneficiaryDTO = getBeneficiaryById(id, username);
		Beneficiary beneficiaryToDelete = new Beneficiary(beneficiaryDTO, 
				beneficiaryDTO.getNationality(), 
				beneficiaryDTO.isDoubleNationality(), 
				beneficiaryDTO.getArrivedDate(), 
				beneficiaryDTO.isEuropeanCitizenAuthorization(), 
				beneficiaryDTO.isTouristVisa(), 
				beneficiaryDTO.getDateTouristVisa(), 
				beneficiaryDTO.isHealthCard(), 
				beneficiaryDTO.getEmploymentSector(), 
				beneficiaryDTO.getPerceptionAid(), 
				beneficiaryDTO.isSavingsPossesion(),
				beneficiaryDTO.isSaeInscription(),
				beneficiaryDTO.isWorking(), 
				beneficiaryDTO.isComputerKnowledge(),
				beneficiaryDTO.getOwnedDevices(), 
				beneficiaryDTO.getLanguages());
		    beneficiaryToDelete.setId(id);
		
		List<AcademicExperience> acadExps = academicExperienceRepository.findAcademicExperienceByBeneficiaryId(beneficiaryToDelete.getId()).get();

		List<Appointment> appointments = appointmentRepository.findAppointmentsByBeneficiaryId(beneficiaryToDelete.getId()).get();
		List<WorkExperience> workExperiencesList = workExperienceRepository.findWorkExperienceByBeneficiaryId(beneficiaryToDelete.getId()).get();
		List<ComplementaryFormation> complementaryFormationList = complementaryFormationRepository.findComplementaryFormationByBeneficiary(beneficiaryToDelete.getId()).get();
		List<Attendance> attendances = attendanceRepository.findAllByPersonId(beneficiaryToDelete.getId());
        
    try {
	    	for(Appointment a : appointments) {
				appointmentRepository.delete(a);
	    	}
			for(AcademicExperience a: acadExps) {
				academicExperienceRepository.delete(a);
			}
			for(Attendance a : attendances) {
				attendanceRepository.delete(a);
			}
			for(WorkExperience w : workExperiencesList) {
				workExperienceRepository.delete(w);
			}
      
			for(ComplementaryFormation c : complementaryFormationList) {
				complementaryFormationRepository.delete(c);
			}
			beneficiaryRepository.delete(beneficiaryToDelete);	
      
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}		
		
	}




	

}