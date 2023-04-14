package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.AppointmentDTO;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.entity.Appointment;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AppointmentRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentServiceImplTest {

 	@InjectMocks
 	private AppointmentServiceImpl appointmentServiceImpl;
 	
 	@Mock
    private ONGRepository ongRepository;

 	@Mock
 	private JwtUtils jwtUtils;
 	
 	@Mock
    private BeneficiaryRepository beneficiaryRepository;

 	@Mock
 	private SigninResponseDTO signinResponseDTO;

 	@Mock
 	private AppointmentRepository appointmentRepository; 

 	@Mock
 	private AccountServiceImpl accountServiceImpl; 

 	@Mock
 	private PasswordEncoder encoder;
    
 	
    public Ong createOng() {
        Ong ong = new Ong();
        ong.setName("Unicef España");
        ong.setCif("D44577716");
        ong.setDescription("Ayudamos a los niños del mundo");
        ong.setEmail("unicef-esp@gmail.com");
 		ong.setPassword("unicef-esp-password-123");
 		ong.setRolAccount(RolAccount.ONG);
 		ong.setUsername("unicef-esp");

 		ongRepository.save(ong);
 		
 		return ong;
    }
    
    public Beneficiary createBeneficiary() {
    	Beneficiary beneficiary = new Beneficiary();
    	beneficiary.setAddress("Calle Cuna");
    	beneficiary.setArrivedDate(LocalDate.now());
    	beneficiary.setBirthday(LocalDate.now());
    	beneficiary.setCivilStatus(CivilStatus.SINGLE);
    	beneficiary.setComputerKnowledge(true);
    	beneficiary.setDateTouristVisa(null);
    	beneficiary.setDocumentNumber("32965314P");
    	beneficiary.setDocumentType(DocumentType.DNI);
    	beneficiary.setDoubleNationality(false);
    	beneficiary.setDriveLicenses(null);
    	beneficiary.setEmail("mrps@hotmail.es");
    	beneficiary.setEmploymentSector(null);
    	beneficiary.setEntryDate(LocalDate.now());
    	beneficiary.setEuropeanCitizenAuthorization(true);
    	beneficiary.setFirstSurname("García");
    	beneficiary.setGender(Gender.MALE);
    	beneficiary.setHealthCard(true);
    	beneficiary.setId(1L);
    	beneficiary.setLanguages(null);
    	beneficiary.setLeavingDate(null);
    	beneficiary.setListAcademicExperience(null);
    	beneficiary.setListAppointments(null);
    	beneficiary.setListComplementaryFormation(null);
    	beneficiary.setListWorkExperience(null);
    	beneficiary.setName("Ángel");
    	beneficiary.setNationality("Español");
    	beneficiary.setNumberOfChildren(2);
    	beneficiary.setOtherSkills(null);
    	beneficiary.setOwnedDevices("Movil");
    	beneficiary.setPassword("12345");
    	beneficiary.setPerceptionAid(null);
    	beneficiary.setPostalCode("113586");
    	beneficiary.setRegistrationAddress("asd");
    	beneficiary.setRolAccount(RolAccount.BENEFICIARY);
    	beneficiary.setSaeInscription(false);
    	beneficiary.setSavingsPossesion(false);
    	beneficiary.setSecondSurname("Vargas");
    	beneficiary.setTelephone("674935183");
    	beneficiary.setTouristVisa(false);
    	beneficiary.setTown("Jerez");
    	beneficiary.setUsername("angel123");
    	beneficiary.setWorking(true);
    	
    	beneficiaryRepository.save(beneficiary);
    	
    	

    	return beneficiary;
    }
    
    public AppointmentDTO createAppointmentDTO() {
    	AppointmentDTO appointmentDTO = new AppointmentDTO();
        Random random = new Random();
        String hourAppointment = String.format("%02d:%02d", random.nextInt(24), random.nextInt(60));
        String notes = UUID.randomUUID().toString();
        appointmentDTO.setDateAppointment(LocalDate.now());
        appointmentDTO.setHourAppointment(hourAppointment);
        appointmentDTO.setNotes(notes);
        
        return appointmentDTO;
    }
  
    
    
    @Test
    public void testSaveAppointment() throws OperationNotAllowedException {
    	// Crear una instancia de AppointmentDTO
    	AppointmentDTO appointmentDTO = createAppointmentDTO();
    	
        // Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
    	
    	// Configuramos los mocks
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment appointment = invocation.getArgument(0);
            appointment.setId(new Random().nextLong());
            return appointment;
        });
        
        // Probamos el metodo
        AppointmentDTO result = appointmentServiceImpl.saveAppointment(token, appointmentDTO, beneficiary.getId());
        
        // Verificamos que se haya guardado la cita correctamente
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(appointmentDTO.getDateAppointment(), result.getDateAppointment());
        Assertions.assertEquals(appointmentDTO.getHourAppointment(), result.getHourAppointment());
        Assertions.assertEquals(appointmentDTO.getNotes(), result.getNotes());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }
    
    
    @Test
    public void testGetAppointmentById() throws OperationNotAllowedException {
        // Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de Appointment
    	Appointment appointment = new Appointment(createAppointmentDTO());
    	appointment.setId(1L);
        appointment.setBeneficiary(beneficiary);
        appointment.setOng(ong);
    	
    	// Configuramos los mocks
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(appointmentRepository.findById(eq(appointment.getId()))).thenReturn(Optional.of(appointment));
        
        // Probamos el metodo
        AppointmentDTO result = appointmentServiceImpl.getAppointmentById(appointment.getId(), token);
        // Verificamos que se haya encontrado la cita correctamente
		assertNotNull(result);

    }
    
    @Test
    public void testGetBeneficiaryByAppointmentId() throws OperationNotAllowedException {
        // Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de Appointment
    	Appointment appointment = new Appointment(createAppointmentDTO());
    	appointment.setId(1L);
        appointment.setBeneficiary(beneficiary);
        appointment.setOng(ong);
    	
    	// Configuramos los mocks
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(appointmentRepository.findById(eq(appointment.getId()))).thenReturn(Optional.of(appointment));
        
        // Probamos el metodo
        BeneficiaryDTO result = appointmentServiceImpl.getBeneficiaryByAppointmentId(appointment.getId(), token);
        // Verificamos que se haya encontrado la cita correctamente
		assertNotNull(result);

    }
    
    @Test
    public void testGetAppointmentByOng() throws OperationNotAllowedException {
        // Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de Appointment
    	Appointment appointment = new Appointment(createAppointmentDTO());
    	appointment.setId(1L);
        appointment.setBeneficiary(beneficiary);
        appointment.setOng(ong);
        
        List<Appointment> appointments = List.of(appointment);
    	
    	// Configuramos los mocks
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(appointmentRepository.findAppointmentsByOngId(eq(ong.getId()))).thenReturn(Optional.of(appointments));
        
        // Probamos el metodo
        List<AppointmentDTO> result = appointmentServiceImpl.getAppointmentsByONG(token);
        // Verificamos que se hayan encontrado las citas correctamentes
		assertNotNull(result);

    }
    
    
    @Test
    public void testGetAppointmentByBeneficiary() throws OperationNotAllowedException {
        // Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de Appointment
    	Appointment appointment = new Appointment(createAppointmentDTO());
    	appointment.setId(1L);
        appointment.setBeneficiary(beneficiary);
        appointment.setOng(ong);
        
        List<Appointment> appointments = List.of(appointment);
    	
    	// Configuramos los mocks
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(appointmentRepository.findAppointmentsByBeneficiaryId(eq(beneficiary.getId()))).thenReturn(Optional.of(appointments));
        
        // Probamos el metodo
        List<AppointmentDTO> result = appointmentServiceImpl.getAppointmentsByBeneficiary(beneficiary.getId(), token);
        // Verificamos que se hayan encontrado las citas correctamentes
		assertNotNull(result);

    }
    
    @Test
    public void testUpdateAppointment() throws OperationNotAllowedException {
        // Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de Appointment
    	Appointment appointment = new Appointment(createAppointmentDTO());
    	appointment.setId(1L);
        appointment.setBeneficiary(beneficiary);
        appointment.setOng(ong);

        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment);
        appointmentDTO.setNotes("update");
        
    	
    	// Configuramos los mocks
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(appointmentRepository.findById(eq(appointment.getId()))).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        
        // Probamos el metodo
        AppointmentDTO result = appointmentServiceImpl.updateAppointment(token, appointmentDTO, appointment.getId());
        // Verificamos que se haya actualizado la cita correctamente
		assertNotNull(result);

    }
    
    @Test
    public void testDeleteAppointment() throws OperationNotAllowedException {
        // Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de Appointment
    	Appointment appointment = new Appointment(createAppointmentDTO());
    	appointment.setId(1L);
        appointment.setBeneficiary(beneficiary);
        appointment.setOng(ong);
        
    	// Configuramos los mocks
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(appointmentRepository.findById(eq(appointment.getId()))).thenReturn(Optional.of(appointment));
        
        // Verificamos que exista la cita previamente la cita
        AppointmentDTO result = appointmentServiceImpl.getAppointmentById(appointment.getId(), token);
		assertNotNull(result);
		
		// Probamos que el metodo funciona
		appointmentServiceImpl.deleteAppointment(appointment.getId(), token);
    }
}


