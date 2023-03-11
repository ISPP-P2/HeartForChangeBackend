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
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.service.VolunteerService;

@Service
public class VolunteerServiceImpl implements VolunteerService{

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private VolunteerRepository volunteerRepository;
	private PasswordEncoder encoder;
	private ONGRepository ongRepository;
	/*
	 * Dependency injection 
	 */
	public VolunteerServiceImpl(VolunteerRepository volunteerRepository, PasswordEncoder encoder,
			 ONGRepository ongRepository) {
		super();
		this.ongRepository = ongRepository;
		this.volunteerRepository = volunteerRepository;
		this.encoder = encoder;
	}
	
	/*
	 * Get all volunteer
	 * @Return List<VolunteerDTO>
	 */
	@Override
	public List<VolunteerDTO> getAllVolunteers(){
		List<Volunteer> volunteers = volunteerRepository.findAll();
		List<VolunteerDTO> volunteersDTO = new ArrayList<>();
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
	public List<VolunteerDTO> getVolunteersByOng(String username) {
		List<Volunteer> volunteers = volunteerRepository.findVolunteersByOng(username);
		List<VolunteerDTO> volunteersDTO = new ArrayList<>();
		for(Volunteer volunteer: volunteers) {
			VolunteerDTO volunteerDTO = new VolunteerDTO(volunteer, volunteer.getHourOfAvailability(), volunteer.getSexCrimes());
			volunteersDTO.add(volunteerDTO);
		}
		return volunteersDTO;
	} 
	
	/*
	 * Get volunteer by id
	 * @Params Long id
	 * @Return VolunteerDTO
	 */
	@Override
	public VolunteerDTO getVolunteerById(Long id) {
		Optional<Volunteer> optVolunteer = volunteerRepository.findById(id);
		if(!optVolunteer.isPresent()) {
			throw new UsernameNotFoundException("This Volunteer doesn't exit");
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
	public VolunteerDTO updateVolunteer(Long id, VolunteerDTO newVolunteerDTO) {
		Optional<Volunteer> volunteerToUpdate = volunteerRepository.findById(id);
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
	public void deleteVolunteer(Long id) {
		logger.info("Deleting Volunteer with id={}", id);
		VolunteerDTO volunteerDTO = getVolunteerById(id);
		Volunteer volunteerToDelete = new Volunteer(volunteerDTO, volunteerDTO.getHourOfAvailability(), volunteerDTO.getSexCrimes());
		volunteerToDelete.setId(id);
		//Get all the work experiences that belong to the volunteer to delete
		try {
			volunteerRepository.delete(volunteerToDelete);
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
}
