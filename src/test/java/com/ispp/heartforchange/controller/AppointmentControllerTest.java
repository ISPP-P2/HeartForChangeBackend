package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.AppointmentDTO;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
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
import com.ispp.heartforchange.service.impl.AccountServiceImpl;
import com.ispp.heartforchange.service.impl.AppointmentServiceImpl;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppointmentControllerTest {

	
	@InjectMocks
	private AppointmentController appointmentController;
	
	@Mock
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
 	public void saveAppointmentTest() throws OperationNotAllowedException {
 		// Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de AppointmentDTO
    	AppointmentDTO appointmentDTO = createAppointmentDTO();
        

 		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
 		when(appointmentServiceImpl.saveAppointment("oken", appointmentDTO, beneficiary.getId())).thenReturn(appointmentDTO);
 		HttpServletRequest request = mock(HttpServletRequest.class);
 		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

 		ResponseEntity<?> res = appointmentController.saveAppointment(request, appointmentDTO, beneficiary.getId());
 		assertEquals(res, ResponseEntity.ok(appointmentDTO));
 	}
 	
 	@Test
 	public void updateAppointmentTest() throws OperationNotAllowedException {
 		// Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de AppointmentDTO
    	AppointmentDTO appointmentDTO = createAppointmentDTO();
        

 		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
 		when(appointmentServiceImpl.updateAppointment("oken", appointmentDTO, Long.valueOf(1))).thenReturn(appointmentDTO);
 		HttpServletRequest request = mock(HttpServletRequest.class);
 		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

 		ResponseEntity<?> res = appointmentController.updateAppointment(appointmentDTO, Long.valueOf(1), request);
 		assertEquals(res, ResponseEntity.ok(appointmentDTO));
 	}
 	
 	@Test
 	public void testGetAppointmentById() throws OperationNotAllowedException {
 		// Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de AppointmentDTO
        AppointmentDTO appointmentDTO = createAppointmentDTO();

 		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
 		when(appointmentServiceImpl.getAppointmentById(appointmentDTO.getId(), "oken")).thenReturn(appointmentDTO);
 		HttpServletRequest request = mock(HttpServletRequest.class);
 		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

 		ResponseEntity<?> res = appointmentController.getAppointmentById(request, appointmentDTO.getId());
 		assertEquals(res, ResponseEntity.ok(appointmentDTO));
 	}
 	
 	@Test
 	public void testGetBeneficiaryByAppointmentId() throws OperationNotAllowedException {
 		// Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
     // Crear una instancia de AppointmentDTO
        AppointmentDTO appointmentDTO = createAppointmentDTO();
        
        // Crear una instancia de BeneficiaryDTO
		BeneficiaryDTO beneficiaryDTO =  new BeneficiaryDTO(beneficiary, 
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

 		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
 		when(appointmentServiceImpl.getBeneficiaryByAppointmentId(appointmentDTO.getId(), "oken")).thenReturn(beneficiaryDTO);
 		HttpServletRequest request = mock(HttpServletRequest.class);
 		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

 		ResponseEntity<?> res = appointmentController.getBeneficiaryByAppointment(request, appointmentDTO.getId());
 		assertEquals(res, ResponseEntity.ok(beneficiaryDTO));
 	}
 	
 	@Test
 	public void testGetAppointmentByOng() throws OperationNotAllowedException {
 		// Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de AppointmentDTO
        AppointmentDTO appointmentDTO = createAppointmentDTO();
        List<AppointmentDTO> appointmentsDTO = List.of(appointmentDTO);

 		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
 		when(appointmentServiceImpl.getAppointmentsByONG("oken")).thenReturn(appointmentsDTO);
 		HttpServletRequest request = mock(HttpServletRequest.class);
 		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

 		ResponseEntity<?> res = appointmentController.getAppointmentByOng(request);
 		assertEquals(res, ResponseEntity.ok(appointmentsDTO));
 	}
 	
 	@Test
 	public void testGetAppointmentByBeneficiary() throws OperationNotAllowedException {
 		// Crear una instancia de Ong
        Ong ong = createOng();
        // Crear una instancia de Beneficiary
        Beneficiary beneficiary = createBeneficiary();
        beneficiary.setOng(ong);
        
        // Crear una instancia de AppointmentDTO
        AppointmentDTO appointmentDTO = createAppointmentDTO();
        List<AppointmentDTO> appointmentsDTO = List.of(appointmentDTO);

 		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
 		when(appointmentServiceImpl.getAppointmentsByBeneficiary(beneficiary.getId(), "oken")).thenReturn(appointmentsDTO);
 		HttpServletRequest request = mock(HttpServletRequest.class);
 		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

 		ResponseEntity<?> res = appointmentController.getAppointmentByBeneficiary(request, beneficiary.getId());
 		assertEquals(res, ResponseEntity.ok(appointmentsDTO));
 	}
 	
 	@Test
 	public void testDeleteAppointment() throws OperationNotAllowedException {

 		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
 		HttpServletRequest request = mock(HttpServletRequest.class);
 		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

 		ResponseEntity<?> res = appointmentController.deleteAppointment(Long.valueOf(1), request);
 		assertEquals(res, ResponseEntity.ok("Appointment deleted"));
 	}
}