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
	 * Get all appointments
	 * @Return List<AppointmentDTO>
	 */
	@Override
	public List<AppointmentDTO> getAllAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		List<AppointmentDTO> appointmentsDTO = new ArrayList<>();
		for (Appointment appointment : appointments) {
			AppointmentDTO appointmentDTO = new AppointmentDTO(appointment);
			appointmentsDTO.add(appointmentDTO);
		}
		return appointmentsDTO;
	}

	/*
	 * Get appointment by id
	 * @Params Long id
	 * @Params String token
	 * @Return AppointmentDTO
	 */
	@Override
	public AppointmentDTO getAppointmentById(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Optional<Appointment> appointment = appointmentRepository.findById(id);

		if (!appointment.isPresent()) {
			throw new UsernameNotFoundException("Appointment not found!");
		} else {
			if(appointment.get().getBeneficiary().getOng().getUsername().equals(username)) {
				return new AppointmentDTO(appointment.get());
			}else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}
	}

	/*
	 * Get appointments by ong
	 * @Params String ongUsername
	 * @Params String token
	 * @Return AppointmentDTO
	 */
	@Override
	public List<AppointmentDTO> getAppointmentsByONG(String ongUsername, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Optional<List<Appointment>> appointments = appointmentRepository.findAppointmentsByOngUsername(ongUsername);
		
		List<Ong> auxOngs = ongRepository.findAll();
 		boolean exception = true;
 		for(Ong o: auxOngs) {
 			if (ongUsername.equals(o.getUsername())){
 				exception = false;
 			}
 		}

 		if(exception==true) {
 			throw new UsernameNotFoundException("The username of the ONG doesn't exist!");

 		}
		
		List<AppointmentDTO> appointmentsDTO = new ArrayList<>();

		if (!appointments.isPresent()) {
			throw new UsernameNotFoundException("Appointments not found!");
		} else {
			if(appointments.get().size() == 0) {
				throw new UsernameNotFoundException("This ONG has not appointments!");
			}else {
				if(appointments.get().get(0).getBeneficiary().getOng().getUsername().equals(username)) {
					for (Appointment appointment : appointments.get()) {
						AppointmentDTO appointmentDTO = new AppointmentDTO(appointment);
						appointmentsDTO.add(appointmentDTO);
					}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
		}
		return appointmentsDTO;
	}

	/*
     * Save a work experience
     * @Params String token
     * @Params WorkExperienceDTO workExperienceDTO
     * @Return WorkExperienceDTO
     */
    @Override
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO, String username) {

    	Appointment appointment = new Appointment(appointmentDTO);
    	appointment.setId(Long.valueOf(0));
        List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();
        Beneficiary beneficiaryAux = new Beneficiary();
        
        for (Beneficiary beneficiary: beneficiaries) {
            if(beneficiary.getUsername().equals(username)) {
            	beneficiaryAux = beneficiary;
            	appointment.setBeneficiary(beneficiary);
            }
        }

        logger.info("Appointment saved associated with {}", username);
        try {
        	if(beneficiaryAux.getUsername() != null) {
        		Appointment appointmentSaved = appointmentRepository.save(appointment);
                return new AppointmentDTO(appointmentSaved);
        	}else {
        		throw new UsernameNotFoundException("The username doesn't exist");
        	}
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    /*
     * Update an appointment
     * @Params String token
     * @Params AppointmentDTO appointmentDTO
     * @Return AppointmentDTO
     */
	@Override
	public AppointmentDTO updateAppointment(String token, AppointmentDTO appointmentDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentDTO.getId());
		logger.info("Appointment is updating with id={}", appointmentDTO.getId());
		
		if (appointment.isPresent()) {
			if(appointment.get().getBeneficiary().getOng().getUsername().equals(username)) {
				appointment.get().setDateAppointment(appointmentDTO.getDateAppointment());
				appointment.get().setHourAppointment(appointmentDTO.getHourAppointment());
				appointment.get().setNotes(appointmentDTO.getNotes());
				try {
					Appointment appointmentSaved = appointmentRepository.save(appointment.get());
					return new AppointmentDTO(appointmentSaved);
				} catch (Exception e) {
					throw new UsernameNotFoundException(e.getMessage());
				}
			}else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}else {
			throw new UsernameNotFoundException("Appointment not exist!");
		}
	}

	/*
     * Delete an appointment
     * @Params String token
     * @Params Long id
     * @Return void
     */
	@Override
	public void deleteAppointment(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Optional<Appointment> appointment = appointmentRepository.findById(id);
		
		if (appointment.isPresent()) {
			if(appointment.get().getBeneficiary().getOng().getUsername().equals(username)) {
				appointmentRepository.delete(appointment.get());
			}else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}else {
			throw new UsernameNotFoundException("Appointment not exist!");
		}
	}

}
