package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.ComplementaryFormation;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.repository.AcademicExperienceRepository;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.ComplementaryFormationRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.repository.WorkExperienceRepository;
import com.ispp.heartforchange.service.VolunteerService;

@Service
public class VolunteerServiceImpl implements VolunteerService{

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private VolunteerRepository volunteerRepository;
	private PasswordEncoder encoder;
	private ONGRepository ongRepository;
	private ComplementaryFormationRepository complementaryFormationRepository;
	private AcademicExperienceRepository academicExperienceRepository;
	private WorkExperienceRepository workExperienceRepository;
	private AccountRepository accountRepository;
	

	
	/*
	 * Dependency injection 
	 */
	public VolunteerServiceImpl(VolunteerRepository volunteerRepository, PasswordEncoder encoder,	WorkExperienceRepository workExperienceRepository, 
  ComplementaryFormationRepository complementaryFormationRepository, ONGRepository ongRepository, AcademicExperienceRepository academicExperienceRepository, AccountRepository accountRepository) {
		super();
		this.ongRepository = ongRepository;
		this.volunteerRepository = volunteerRepository;
		this.encoder = encoder;
		this.ongRepository = ongRepository;
		this.workExperienceRepository = workExperienceRepository;
		this.complementaryFormationRepository = complementaryFormationRepository;
		this.academicExperienceRepository = academicExperienceRepository;
		this.accountRepository = accountRepository;
	}
  
	
	
	/*
	 * Get volunteer by ong
	 * @Params String username
	 * @Return VolunteerDTO
	 */
	@Override
	public List<VolunteerDTO> getVolunteersByOng(String username) {
		List<Volunteer> allVolunteers = volunteerRepository.findAll();
		List<VolunteerDTO> volunteersDTO = new ArrayList<>();
		List<Volunteer> volunteers = new ArrayList<>();
		for(Volunteer v : allVolunteers) {
			if(v.getOng().equals(ongRepository.findByUsername(username))) {
				volunteers.add(v);
			}
		}
		if(volunteers.isEmpty()) {
			throw new UsernameNotFoundException("You dont have any volunteers");
		}
		for(Volunteer volunteer: volunteers) {
			VolunteerDTO volunteerDTO = new VolunteerDTO(volunteer, volunteer.getHourOfAvailability(), volunteer.getSexCrimes());
			volunteersDTO.add(volunteerDTO);
		}
		return volunteersDTO;
	}
	
	/*
	 * Get volunteer by ong
	 * @Params String username
	 * @Return VolunteerDTO
	 */
	@Override
	public Integer getNumberOfVolunteersByOng(String username) {
		List<Volunteer> allVolunteers = volunteerRepository.findAll();
		List<VolunteerDTO> volunteersDTO = new ArrayList<>();
		List<Volunteer> volunteers = new ArrayList<>();
		for(Volunteer v : allVolunteers) {
			if(v.getOng().equals(ongRepository.findByUsername(username))) {
				volunteers.add(v);
			}
		}
		if(volunteers.isEmpty()) {
			throw new UsernameNotFoundException("You dont have any volunteers");
		}
		for(Volunteer volunteer: volunteers) {
			VolunteerDTO volunteerDTO = new VolunteerDTO(volunteer, volunteer.getHourOfAvailability(), volunteer.getSexCrimes());
			volunteersDTO.add(volunteerDTO);
		}
		return volunteersDTO.size();
	} 
	
	/*   
	 * Get volunteer by id
	 * @Params Long id
	 * @Return VolunteerDTO
	 */
	@Override
	public VolunteerDTO getVolunteerById(Long id, String username) {
		Optional<Volunteer> optVolunteer = volunteerRepository.findById(id);
		if(!optVolunteer.isPresent()) {
			throw new UsernameNotFoundException("This Volunteer doesn't exit");
		}
		Account account = accountRepository.findByUsername(username);
		Boolean res = null;
		if(account.getRolAccount().equals(RolAccount.ONG)) {
			Ong ong = ongRepository.findByUsername(username);
			if(!optVolunteer.get().getOng().equals(ong)) {
				res = false;
			} else {
				res = true;
			}
		} else if(account.getRolAccount().equals(RolAccount.VOLUNTEER)) {
			if(!optVolunteer.get().equals(volunteerRepository.findByUsername(username))) {
				res = false;
			} else {
				res = true;
			}
		} else {
			res = false;
		}
		
		if(res != true) {
			throw new UsernameNotFoundException("You dont have permission");

		}
		Volunteer volunteer = optVolunteer.get();
		VolunteerDTO volunteerDTO = new VolunteerDTO(volunteer, volunteer.getHourOfAvailability(), volunteer.getSexCrimes());
		return volunteerDTO;
	}
	

	
	/*
	 * Save a Volunteer with their account.
	 * @Params VolunteerDTO
	 * @Params String username
	 * @Return VolunteerDTO
	 */
	@Override
	public VolunteerDTO saveVolunteer(VolunteerDTO volunteerDTO, String username) {
		
		if(!accountRepository.findByUsername(username).getRolAccount().equals(RolAccount.ONG)) {
			throw new UsernameNotFoundException("You must be logged as ONG to create this volunteer");
		}
		
		Ong ong2 = ongRepository.findByUsername(username);
		Volunteer volunteer = new Volunteer(volunteerDTO, volunteerDTO.getHourOfAvailability(), volunteerDTO.getSexCrimes());
		volunteer.setId(Long.valueOf(0));
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		volunteer.setPassword(encoder.encode(volunteer.getPassword()));
		volunteer.setOng(ong2);
		logger.info("Saving Volunteer with username={}", volunteer.getUsername());
		try {
			Volunteer volunteerSave = volunteerRepository.save(volunteer);
			return new VolunteerDTO(volunteerSave, volunteer.getHourOfAvailability(), volunteer.getSexCrimes());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		} 
	}
	
	/*
	 * Update volunteer
	 * @Params Long id
	 * @Params VolunteerDTO
	 * @Return VolunteerDTO
	 */
	@Override 
	public VolunteerDTO updateVolunteer(Long id, VolunteerDTO newVolunteerDTO, String username) {

		Optional<Volunteer> volunteerToUpdate = volunteerRepository.findById(id);
		if(!accountRepository.findByUsername(username).getRolAccount().equals(RolAccount.ONG)) {
			throw new UsernameNotFoundException("You must be logged as ONG to edit this volunteer");
		} else if(!accountRepository.findByUsername(username).equals(volunteerToUpdate.get().getOng())) {
			throw new UsernameNotFoundException("You cant edit this volunteer");
 
		}
		logger.info("Volunteer is updating with id={}", id);
		if(volunteerToUpdate.isPresent()) {
			volunteerToUpdate.get().setPassword(encoder.encode(newVolunteerDTO.getPassword()));
			volunteerToUpdate.get().setAddress(newVolunteerDTO.getAddress());
			volunteerToUpdate.get().setBirthday(newVolunteerDTO.getBirthday());
			volunteerToUpdate.get().setCivilStatus(newVolunteerDTO.getCivilStatus());
			volunteerToUpdate.get().setDocumentNumber(newVolunteerDTO.getDocumentNumber());
			volunteerToUpdate.get().setDocumentType(newVolunteerDTO.getDocumentType());
			volunteerToUpdate.get().setDriveLicenses(newVolunteerDTO.getDriveLicenses());
			volunteerToUpdate.get().setEmail(newVolunteerDTO.getEmail());
			volunteerToUpdate.get().setEntryDate(newVolunteerDTO.getEntryDate());
			volunteerToUpdate.get().setFirstSurname(newVolunteerDTO.getFirstSurname());
			volunteerToUpdate.get().setSecondSurname(newVolunteerDTO.getSecondSurname());
			volunteerToUpdate.get().setGender(newVolunteerDTO.getGender());
			volunteerToUpdate.get().setHourOfAvailability(newVolunteerDTO.getHourOfAvailability());
			volunteerToUpdate.get().setLeavingDate(newVolunteerDTO.getLeavingDate());
			volunteerToUpdate.get().setName(newVolunteerDTO.getName());
			volunteerToUpdate.get().setNumberOfChildren(newVolunteerDTO.getNumberOfChildren());
			volunteerToUpdate.get().setOtherSkills(newVolunteerDTO.getOtherSkills());
			volunteerToUpdate.get().setPostalCode(newVolunteerDTO.getPostalCode());
			volunteerToUpdate.get().setRegistrationAddress(newVolunteerDTO.getRegistrationAddress());
			volunteerToUpdate.get().setSexCrimes(newVolunteerDTO.getSexCrimes());
			volunteerToUpdate.get().setTelephone(newVolunteerDTO.getTelephone());
			volunteerToUpdate.get().setTown(newVolunteerDTO.getTown());
			volunteerToUpdate.get().setUsername(newVolunteerDTO.getUsername());
		} else {
			throw new UsernameNotFoundException("This Volunteer doesn't exist!");
		} 
		try {
			Volunteer volunteerSave = volunteerRepository.save(volunteerToUpdate.get());
			return new VolunteerDTO(volunteerSave, volunteerSave.getHourOfAvailability(), volunteerSave.getSexCrimes());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		} 
	}
	
	/*
	 * Delete volunteer
	 * @Params Long id
	 * @Return void
	 */
	@Override
	public void deleteVolunteer(Long id, String username) {
		if(!accountRepository.findByUsername(username).getRolAccount().equals(RolAccount.ONG)) {
			throw new UsernameNotFoundException("You must be logged as ONG to delete this volunteer");
		}
		logger.info("Deleting Volunteer with id={}", id);
		VolunteerDTO volunteerDTO = getVolunteerById(id,username);
		Volunteer volunteerToDelete = new Volunteer(volunteerDTO, volunteerDTO.getHourOfAvailability(), volunteerDTO.getSexCrimes());
		volunteerToDelete.setId(id);

		List<AcademicExperience> academicExps = academicExperienceRepository.findByVolunteer(volunteerToDelete.getUsername()).get();
		//Get all the work experiences that belong to the volunteer to delete

		List<WorkExperience> workExperiencesList = workExperienceRepository.findWorkExperienceByVolunteerId(volunteerToDelete.getId()).get();
		List<ComplementaryFormation> complementaryFormations = complementaryFormationRepository.findComplementaryFormationByVolunteer(volunteerToDelete.getId()).get();

		try {
			for(AcademicExperience a: academicExps) {
				academicExperienceRepository.delete(a);
			}
      
		    for(WorkExperience w : workExperiencesList) {
				workExperienceRepository.delete(w);
			}
      
		    for(AcademicExperience a: academicExps) {
		    	academicExperienceRepository.delete(a);
		    }
      
		    for(ComplementaryFormation c : complementaryFormations) {
				complementaryFormationRepository.delete(c);
		    }
			volunteerRepository.delete(volunteerToDelete);
		
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
}
