package com.ispp.heartforchange.service.impl;

import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AppointmentDTO;
import com.ispp.heartforchange.entity.Appointment;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.AppointmentRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.AppointmentService;


@Service
public class AppointmentServiceImpl implements AppointmentService {

	
	private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	private AppointmentRepository appointmentRepository;
	private BeneficiaryRepository beneficiaryRepository;
	private ONGRepository ongRepository;
	private JwtUtils jwtUtils;
	
	
	/*
	 * Dependency injection
	 */
	public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AccountRepository accountRepository,
			BeneficiaryRepository beneficiaryRepository, ONGRepository ongRepository, JwtUtils jwtUtils) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.ongRepository = ongRepository;
		this.jwtUtils = jwtUtils;
	}
	

	/*
	 * Get appointment by id
	 * @Params Long id
	 * @Params String token
	 * @Return AppointmentDTO
	 */
	@Override
	public AppointmentDTO getAppointmentById(Long id, String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Appointment> appointment = appointmentRepository.findById(id);

		if(ong!=null) {
			if (!appointment.isPresent()) {
				throw new UsernameNotFoundException("Not Found: Appointment not exist!");
			} else {
				if(appointment.get().getBeneficiary().getOng().getId() == ong.getId()) {
					return new AppointmentDTO(appointment.get());
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
		}else{
 			throw new OperationNotAllowedException("You must be an ONG to use this method.");
		}	
	}

	/*
	 * Get appointments by ong
	 * @Params Long ongId
	 * @Params String token
	 * @Return AppointmentDTO
	 */
	@Override
	public List<AppointmentDTO> getAppointmentsByONG(String token) throws OperationNotAllowedException{
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		
 		if(ong!=null) {
			List<AppointmentDTO> appointmentsDTO = new ArrayList<>();
			Optional<List<Appointment>> appointments = appointmentRepository.findAppointmentsByOngId(ong.getId());
	
			if (!appointments.isPresent()) {
				throw new UsernameNotFoundException("Appointments do not exist for this ONG!");
			} else {
				if(appointments.get().size() == 0) {
					return appointmentsDTO;
				}else {
					if(appointments.get().get(0).getBeneficiary().getOng().getId() == ong.getId()) {
						for (Appointment appointment : appointments.get()) {
							AppointmentDTO appointmentDTO = new AppointmentDTO(appointment);
							appointmentsDTO.add(appointmentDTO);
						}
						return appointmentsDTO;
					}else {
						throw new UsernameNotFoundException("You don't have access to information of others ONGs!");
					}
				}
			}
 		}else{
 			throw new OperationNotAllowedException("You must be an ONG to use this method.");
 		}
	}
	
	/*
	 * Get appointments by beneficiary
	 * @Params Long ongId
	 * @Params String token
	 * @Return AppointmentDTO
	 */
	@Override
	public List<AppointmentDTO> getAppointmentsByBeneficiary(Long beneficiaryId, String token) throws OperationNotAllowedException{
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<List<Appointment>> appointments = appointmentRepository.findAppointmentsByBeneficiaryId(beneficiaryId);
		Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(beneficiaryId);
		
 		if(ong!=null) {
 			if(beneficiary.isPresent()) {
 				List<AppointmentDTO> appointmentsDTO = new ArrayList<>();
 				if (!appointments.isPresent()) {
 					throw new UsernameNotFoundException("Not Found: Appointments not exist for the beneficiary!");
 				} else {
 					if(appointments.get().size() == 0) {
 						throw new UsernameNotFoundException("This beneficiary has not appointments!");
 					}else {
 						if(appointments.get().get(0).getBeneficiary().getOng().getId() == ong.getId()) {
 							for (Appointment appointment : appointments.get()) {
 								AppointmentDTO appointmentDTO = new AppointmentDTO(appointment);
 								appointmentsDTO.add(appointmentDTO);
 							}
 							return appointmentsDTO;
 						}else {
 							throw new UsernameNotFoundException("You don't have access!");
 						}
 					}
 				}
 			}else {
 				throw new UsernameNotFoundException("Not Found: The Beneficiary with this ID doesn't exist!");
 			}
			
 		}else{
 			throw new OperationNotAllowedException("You must be an ONG to use this method.");
		}
	}

	/*
     * Save a work experience
     * @Params String token
     * @Params WorkExperienceDTO workExperienceDTO
     * @Params Long beneficiaryId
     * @Return WorkExperienceDTO
     */
    @Override
    public AppointmentDTO saveAppointment(String token, AppointmentDTO appointmentDTO, Long beneficiaryId) throws OperationNotAllowedException{

    	String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
    	
    	Appointment appointment = new Appointment(appointmentDTO);
    	appointment.setId(Long.valueOf(0));
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(beneficiaryId);

        if(ong!=null) {
	        try {
	        	if(beneficiary.isPresent()) {
	        		if(beneficiary.get().getOng().getId() == ong.getId()) {
	        			appointment.setBeneficiary(beneficiary.get());
	        			logger.info("Appointment saved associated with beneficiay id={}", beneficiaryId);
		        		Appointment appointmentSaved = appointmentRepository.save(appointment);
		                return new AppointmentDTO(appointmentSaved);
	        		}else {
	        			throw new UsernameNotFoundException("You don't have access!");
	        		}
	        	}else {
		        	throw new UsernameNotFoundException("Not Found: The beneficiary with this ID doesn't exist");
		        }
	        } catch (Exception e) {
	            throw new UsernameNotFoundException(e.getMessage());
	        }
        }else{
 			throw new OperationNotAllowedException("You must be an ONG to use this method.");
		}
    }

    /*
     * Update an appointment
     * @Params String token
     * @Params AppointmentDTO appointmentDTO
     * @Params Long appointmentId
     * @Return AppointmentDTO
     */
	@Override
	public AppointmentDTO updateAppointment(String token, AppointmentDTO appointmentDTO, Long appointmentId) throws OperationNotAllowedException {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
		
		if(ong!=null) {
			if (appointment.isPresent()) {
				if(appointment.get().getBeneficiary().getOng().getId() == ong.getId()) {
					appointment.get().setDateAppointment(appointmentDTO.getDateAppointment());
					appointment.get().setHourAppointment(appointmentDTO.getHourAppointment());
					appointment.get().setNotes(appointmentDTO.getNotes());
					try {
						logger.info("Appointment is updating with id={}", appointmentId);
						Appointment appointmentSaved = appointmentRepository.save(appointment.get());
						return new AppointmentDTO(appointmentSaved);
					} catch (Exception e) {
						throw new UsernameNotFoundException(e.getMessage());
					}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}else {
				throw new UsernameNotFoundException("Not Found: Appointment not exist!");
			}
		}else{
 			throw new OperationNotAllowedException("You must be an ONG to use this method.");
		}
		
	}

	/*
     * Delete an appointment
     * @Params String token
     * @Params Long id
     * @Return void
     */
	@Override
	public void deleteAppointment(Long id, String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Appointment> appointment = appointmentRepository.findById(id);
		
		if(ong!=null) {
			if (appointment.isPresent()) {
				if(appointment.get().getBeneficiary().getOng().getId() == ong.getId()) {
					appointmentRepository.delete(appointment.get());
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}else {
				throw new UsernameNotFoundException("Not Found: Appointment not exist!");
			}
		}else{
 			throw new OperationNotAllowedException("You must be an ONG to use this method.");
		}
	}

}
