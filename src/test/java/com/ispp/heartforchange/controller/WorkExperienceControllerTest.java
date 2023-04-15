package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ispp.heartforchange.dto.WorkExperienceDTO;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.WorkExperienceServiceImpl;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkExperienceControllerTest {
	
	@InjectMocks
	private WorkExperienceController workExperienceController;
	
	@Mock
	private WorkExperienceServiceImpl workExperienceService;

	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private VolunteerRepository volunteerRepository;
	
	@Mock
	private BeneficiaryRepository beneficiaryRepository;
	
	@Mock
	private ONGRepository ongRepository;

	public Ong createOng() throws OperationNotAllowedException {
		Ong ong = new Ong();
		ong.setCif("G17777777");
		ong.setDescription("description");
		ong.setEmail("test@gmail.com");
		ong.setId(Long.valueOf(0));
		ong.setName("test");
		ong.setPassword("asdf1234");
		ong.setRolAccount(RolAccount.ONG);
		ong.setUsername("test");
		ong.setId(1L);
		
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
	
	public Volunteer createVolunteer() {
    	Volunteer volunteer = new Volunteer();
    	volunteer.setAddress("Calle Cuna");
    	volunteer.setBirthday(LocalDate.now());
    	volunteer.setCivilStatus(CivilStatus.SINGLE);
    	volunteer.setDocumentNumber("32965314P");
    	volunteer.setDocumentType(DocumentType.DNI);
    	volunteer.setDriveLicenses(null);
    	volunteer.setEmail("mrps@hotmail.es");
    	volunteer.setEntryDate(LocalDate.now());
    	volunteer.setFirstSurname("García");
    	volunteer.setGender(Gender.MALE);
    	volunteer.setId(1L);
    	volunteer.setLeavingDate(null);
    	volunteer.setListAcademicExperience(null);
    	volunteer.setListWorkExperience(null);
    	volunteer.setComplementaryFormation(null);
    	volunteer.setName("Ángel");
    	volunteer.setNumberOfChildren(2);
    	volunteer.setOtherSkills(null);
    	volunteer.setPassword("12345");
    	volunteer.setPostalCode("113586");
    	volunteer.setRegistrationAddress("asd");
    	volunteer.setRolAccount(RolAccount.VOLUNTEER);
    	volunteer.setSecondSurname("Vargas");
    	volunteer.setTelephone("674935183");
    	volunteer.setTown("Jerez");
    	volunteer.setUsername("angel123");
    	volunteer.setHourOfAvailability("Nunca");
    	volunteer.setSexCrimes(false);
    	
    	volunteerRepository.save(volunteer);
    	
    	return volunteer;
    }
	
	@Test
	public void saveWorkExperience() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(workExperienceService.saveWorkExperience(workExperienceDTO, Long.valueOf(1), "oken")).thenReturn(workExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = workExperienceController.saveWorkExperience(request, workExperienceDTO, 1L);
		assertEquals(res, ResponseEntity.ok(workExperienceDTO));
	}
	
	@Test
	public void saveWorkExperienceWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(workExperienceService.saveWorkExperience(workExperienceDTO, Long.valueOf(1), "oken")).thenReturn(workExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = workExperienceController.saveWorkExperience(request, workExperienceDTO, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void saveWorkExperienceWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(workExperienceService.saveWorkExperience(workExperienceDTO, Long.valueOf(1), "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = workExperienceController.saveWorkExperience(request, workExperienceDTO, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void updateWorkExperienceTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(workExperienceService.updateWorkExperience("oken", workExperienceDTO, 1L)).thenReturn(workExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = workExperienceController.updateWorkExperience(workExperienceDTO, request, 1L);
		assertEquals(res, ResponseEntity.ok(workExperienceDTO));
	}
	
	@Test
	public void updateWorkExperienceWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
		workExperienceDTO.setTime("2 años");
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(workExperienceService.updateWorkExperience("oken", workExperienceDTO, 1L)).thenReturn(workExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = workExperienceController.updateWorkExperience(workExperienceDTO, request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void updateWorkExperienceWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(workExperienceService.updateWorkExperience("valid_token", workExperienceDTO, 1L)).thenThrow(new OperationNotAllowedException("You must be an ONG to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = workExperienceController.updateWorkExperience(workExperienceDTO, request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getWorkExperienceByBeneficiaryID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
		List<WorkExperienceDTO> workExperiences = List.of(workExperienceDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(workExperienceService.getWorkExperienceByBeneficiary(1L, "oken")).thenReturn(workExperiences);

		ResponseEntity<?> res = workExperienceController.getWorkExperienceByBeneficiaryUsername(request, 1L);

		assertEquals(res, ResponseEntity.ok(workExperiences));
	}
	
	@Test
	public void getWorkExperienceByBeneficiaryIDWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
		
		List<WorkExperienceDTO> workExperiences = List.of(workExperienceDTO);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(workExperienceService.getWorkExperienceByBeneficiary(1L, "oken")).thenReturn(workExperiences);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = workExperienceController.getWorkExperienceByBeneficiaryUsername(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void getWorkExperienceByBeneficiaryIDWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		
		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(workExperienceService.getWorkExperienceByBeneficiary(1L, "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG or volunteer to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = workExperienceController.getWorkExperienceByBeneficiaryUsername(request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getWorkExperienceByVolunteerID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		List<WorkExperienceDTO> workExperiences = List.of(workExperienceDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(workExperienceService.getWorkExperienceByVolunteer(1L, "oken")).thenReturn(workExperiences);

		ResponseEntity<?> res = workExperienceController.getWorkExperienceByVolunteerUsername(request, 1L);

		assertEquals(res, ResponseEntity.ok(workExperiences));
	}
	
	@Test
	public void getWorkExperienceByVolunteerIDWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		List<WorkExperienceDTO> workExperiences = List.of(workExperienceDTO);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(workExperienceService.getWorkExperienceByVolunteer(1L, "oken")).thenReturn(workExperiences);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = workExperienceController.getWorkExperienceByVolunteerUsername(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void getWorkExperienceByVolunteerIDWithOutBeingONGOrVolunteerTest_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
 
		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(workExperienceService.getWorkExperienceByVolunteer(1L, "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG or volunteer to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = workExperienceController.getWorkExperienceByVolunteerUsername(request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getWorkExperienceByID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(workExperienceService.getWorkExperienceById(1L, "oken")).thenReturn(workExperienceDTO);

		ResponseEntity<?> res = workExperienceController.getWorkExperienceById(request, 1L);

		assertEquals(res, ResponseEntity.ok(workExperienceDTO));
	}
	
	@Test
	public void getWorkExperienceByIDWithOutToken_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(workExperienceService.getWorkExperienceById(1L, "oken")).thenReturn(workExperienceDTO);

		ResponseEntity<?> res = workExperienceController.getWorkExperienceById(request, 1L);

		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteWorkExperienceTest() throws OperationNotAllowedException {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);

		ResponseEntity<?> res = workExperienceController.deleteWorkExperience(1L, request);

		assertEquals(res, ResponseEntity.ok("Work Experience deleted"));
	}
	
	@Test
	public void deleteWorkExperienceWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		
		ResponseEntity<?> res = workExperienceController.deleteWorkExperience(1L, request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

}