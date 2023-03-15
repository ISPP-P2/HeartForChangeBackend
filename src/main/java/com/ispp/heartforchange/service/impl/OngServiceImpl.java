package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.entity.Appointment;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.AppointmentRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.OngService;

@Service
public class OngServiceImpl implements OngService{

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	private PasswordEncoder encoder;

	private AccountRepository accountRepository;
	private VolunteerServiceImpl volunteerService;
	private BeneficiaryRepository beneficiaryRepository;
	private AppointmentRepository appointmentRepository;
	
	private JwtUtils jwtUtils;
	
	/*
	 * Dependency injection 
	 */

	public OngServiceImpl(ONGRepository ongRepository, PasswordEncoder encoder,BeneficiaryRepository beneficiaryRepository,
			VolunteerServiceImpl volunteerService, AccountRepository accountRepository, AppointmentRepository appointmentRepository,
			JwtUtils jwtUtils) {
		super();
		this.ongRepository = ongRepository;
		this.encoder = encoder;
		this.beneficiaryRepository = beneficiaryRepository;
		this.appointmentRepository = appointmentRepository;
		this.accountRepository = accountRepository;
		this.volunteerService = volunteerService;
		this.jwtUtils = jwtUtils;

	}
	
	/*
	 * Get all ongs
	 * @Return List<OngDTO>
	 */
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
	
	/*
	 * Get ong by id
	 * @Params Long id
	 * @Params String token
	 * @Return OngDTO
	 */
	@Override
	public OngDTO getOngById(Long id, String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		if(ong!=null) {
			Optional<Ong> optOng = ongRepository.findById(id);
			if(!optOng.isPresent()) {
				throw new UsernameNotFoundException("This ONG not exist!");
			}
			ong = optOng.get();
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
	public OngDTO saveOng(OngDTO ongDTO) {
		Ong ong = new Ong(ongDTO);
		ong.setId(Long.valueOf(0));
		ong.setRolAccount(RolAccount.ONG);
		ong.setPassword(encoder.encode(ong.getPassword()));
		logger.info("Saving ONG with username={}", ong.getUsername());
		try {
			Ong ongSaved = ongRepository.save(ong);
			return new OngDTO(ongSaved);
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	/*
	 * Update ong
	 * @Params Long id
	 * @Params OngDTO
	 * @Return OngDTO
	 */
	@Override
	public OngDTO updateOng(Long id, OngDTO newOngDTO) {
		Optional<Ong> ongToUpdate = ongRepository.findById(id);
		logger.info("ONG is updating with id={}", id);
		if( ongToUpdate.isPresent() ) {
			ongToUpdate.get().setUsername(newOngDTO.getUsername());
			ongToUpdate.get().setPassword(encoder.encode(newOngDTO.getPassword()));
			ongToUpdate.get().setName(newOngDTO.getName());
			ongToUpdate.get().setCif(newOngDTO.getCif());
			ongToUpdate.get().setEmail(newOngDTO.getEmail());
			ongToUpdate.get().setDescription(newOngDTO.getDescription());
			
		} else {
			throw new UsernameNotFoundException("This ONG not exist!");
		}
		try {
			Ong ongSaved = ongRepository.save(ongToUpdate.get());
			return new OngDTO(ongSaved);
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public void deleteOng(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Delete ong
	 * @Params Long id
	 * @Params OngDTO
	 * @Return void
	 */
	/*@Override
	public void deleteOng(Long id) {
		logger.info("Deleting ONG with id={}", id);
		OngDTO ongDTO = getOngById(id);
		Ong ongToDelete = new Ong(ongDTO);
		ongToDelete.setId(id);

		//Get all the volunteers that belong to the Ong to delete
		List<VolunteerDTO> volunteerList = volunteerService.getVolunteersByOng(ongToDelete.getUsername());
		List<Beneficiary> beneficiariesONG = beneficiaryRepository.findBeneficiariesByOng(ongToDelete.getUsername());
		List<Appointment> appointments = appointmentRepository.findAppointmentsByOngUsername(ongToDelete.getUsername()).get();
		try {
			for( Beneficiary b : beneficiariesONG) {
				beneficiaryRepository.delete(b);
			}
			for(VolunteerDTO volunteer: volunteerList) {
				accountRepository.deleteById(volunteer.getId());
            }
			for( Appointment a : appointments) {
				appointmentRepository.delete(a);
			}
			ongRepository.delete(ongToDelete);	
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}*/
	 
}
