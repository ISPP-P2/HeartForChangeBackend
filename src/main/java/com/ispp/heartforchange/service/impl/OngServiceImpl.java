package com.ispp.heartforchange.service.impl;

import java.util.List; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.entity.Appointment;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;

import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.AppointmentRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.OngService;

@Service
public class OngServiceImpl implements OngService{

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	private PasswordEncoder encoder;

	private AccountRepository accountRepository;
	private VolunteerRepository volunteerRepository;
	private BeneficiaryRepository beneficiaryRepository;
	private AppointmentRepository appointmentRepository;
	
	private JwtUtils jwtUtils;
	
	/*
	 * Dependency injection 
	 */

	public OngServiceImpl(ONGRepository ongRepository, PasswordEncoder encoder,BeneficiaryRepository beneficiaryRepository,
			VolunteerRepository volunteerRepository, AccountRepository accountRepository, AppointmentRepository appointmentRepository, JwtUtils jwtUtils) {
		super();
		this.ongRepository = ongRepository;
		this.encoder = encoder;
		this.beneficiaryRepository = beneficiaryRepository;
		this.appointmentRepository = appointmentRepository;
		this.accountRepository = accountRepository;
		this.volunteerRepository = volunteerRepository;
		this.jwtUtils = jwtUtils;

	}
	
	/*
	 * Get ong by id
	 * @Params Long id
	 * @Params String token
	 * @Return OngDTO
	 */
	@Override
	public OngDTO getOng(String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		if(ong!=null) {
			OngDTO ongDTO = new OngDTO(ong);
			return ongDTO;
		}else {
			throw new OperationNotAllowedException("You must be an ONG to use this method.");
			
		}
	}
	
	/*
	 * Save an ONG with their account.
	 * @Params OngDTO
	 * @Return OngDTO
	 */
	@Override
	public OngDTO saveOng( OngDTO ongDTO,String jwt) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		System.out.println(username);
		if( ! username.equals("admin")) {
			throw new UsernameNotFoundException("You don't have permision");
		}
		Ong ong = new Ong(ongDTO);
		ong.setId(Long.valueOf(0));
		ong.setRolAccount(RolAccount.ONG);
		ong.setPassword(encoder.encode(ong.getPassword()));
		logger.info("Saving ONG with username={}", ong.getUsername());
		try {
			Ong ongSaved = ongRepository.save(ong);
			return new OngDTO(ongSaved);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	/*
	 * Update ong
	 * @Params Long id
	 * @Params String token
	 * @Params OngDTO
	 * @Return OngDTO
	 */
	@Override
	public OngDTO updateOng(String token, OngDTO newOngDTO) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong loggedOng = ongRepository.findByUsername(username);
		if(loggedOng != null) {
					logger.info("ONG is updating with id={}", loggedOng.getId());
					loggedOng.setUsername(newOngDTO.getUsername());
					loggedOng.setPassword(encoder.encode(newOngDTO.getPassword()));
					loggedOng.setName(newOngDTO.getName());
					loggedOng.setCif(newOngDTO.getCif());
					loggedOng.setEmail(newOngDTO.getEmail());
					loggedOng.setDescription(newOngDTO.getDescription());
		}else {
			throw new OperationNotAllowedException("You must be logged as ONG to use this method.");
		}
		
		try {
			Ong ongSaved = ongRepository.save(loggedOng);
			return new OngDTO(ongSaved);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	/*
	 * Delete ong
	 * @Params Long id
	 * @Params OngDTO
	 * @Return void
	 */
	@Override
	public void deleteOng(String token) throws OperationNotAllowedException {

		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong loggedOng = ongRepository.findByUsername(username);
		if(loggedOng != null) {
			//Get all the volunteers that belong to the Ong to delete
			List<Volunteer> volunteerList = volunteerRepository.findVolunteersByOng(loggedOng.getUsername());
			List<Beneficiary> beneficiariesONG = beneficiaryRepository.findBeneficiariesByOng(loggedOng.getUsername());
			List<Appointment> appointments = appointmentRepository.findByOng(loggedOng);
			for( Beneficiary b : beneficiariesONG) {
				beneficiaryRepository.delete(b);
			}
			for(Volunteer volunteer: volunteerList) {
				accountRepository.deleteById(volunteer.getId());
            }
			for( Appointment a : appointments) {
				appointmentRepository.delete(a); 
			}
			logger.info("Deleting ONG with id={}", loggedOng.getId());
			ongRepository.delete(loggedOng);	
		} else {
			throw new OperationNotAllowedException("You must be logged as ONG to use this method.");
		}
	}

	public String existsOng(String email, String username) {
		Boolean isUsername = 	accountRepository.existsByUsername(username);
		Boolean isEmail = 	accountRepository.existsByEmail(email);

		if(isUsername && isEmail){
			return "El usuario y email ya existe";
		}
		if(isUsername){
			return "El usuario ya existe";
		}

		if(isEmail){
			return "El email ya existe";
		}

		return null;
	}
	 
}
