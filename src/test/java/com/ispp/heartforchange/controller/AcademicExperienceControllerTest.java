package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ispp.heartforchange.dto.AcademicExperienceDTO;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AcademicExperienceRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.AcademicExperienceServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AcademicExperienceControllerTest {
	
	@InjectMocks
	private AcademicExperienceController academicExperienceController;
	
	@Mock
	private AcademicExperienceServiceImpl academicExperienceService;

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
	public void saveAcademicExperience() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(academicExperienceService.saveAcademicExperience(academicExperienceDTO, Long.valueOf(1), "oken")).thenReturn(academicExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = academicExperienceController.saveAcademicExperience(request, academicExperienceDTO, 1L);
		assertEquals(res, ResponseEntity.ok(academicExperienceDTO));
	}
	
	@Test
	public void saveAcademicExperienceWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(academicExperienceService.saveAcademicExperience(academicExperienceDTO, Long.valueOf(1), "oken")).thenReturn(academicExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = academicExperienceController.saveAcademicExperience(request, academicExperienceDTO, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void saveAcademicExperienceWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);

		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true); // Simular un token válido
		when(academicExperienceService.saveAcademicExperience(academicExperienceDTO, Long.valueOf(1), "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG to use this method.")); // Simular una excepción cuando el usuario no es una ONG
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token"); // Inyectar token válido

		ResponseEntity<?> res = academicExperienceController.saveAcademicExperience(request, academicExperienceDTO, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void updateAcademicExperienceTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(academicExperienceService.updateAcademicExperience(academicExperienceDTO, "oken", 1L)).thenReturn(academicExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = academicExperienceController.updateAcademicExperience(academicExperienceDTO, request, 1L);
		assertEquals(res, ResponseEntity.ok(academicExperienceDTO));
	}
	
	@Test
	public void updateAcademicExperienceWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
		academicExperienceDTO.setSpeciality("Idiomas");
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(academicExperienceService.updateAcademicExperience(academicExperienceDTO, "oken", 1L)).thenReturn(academicExperienceDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = academicExperienceController.updateAcademicExperience(academicExperienceDTO, request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void updateAcademicExperienceWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);

		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true); // Simular un token válido
		when(academicExperienceService.updateAcademicExperience(academicExperienceDTO, "valid_token", 1L)).thenThrow(new OperationNotAllowedException("You must be an ONG to use this method.")); // Simular una excepción cuando el usuario no es una ONG
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token"); // Inyectar token válido

		ResponseEntity<?> res = academicExperienceController.updateAcademicExperience(academicExperienceDTO, request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getAcademicExperienceByBeneficiaryID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
		List<AcademicExperienceDTO> academicExperiences = List.of(academicExperienceDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(academicExperienceService.getAcademicExperienceByBeneficiary(1L, "oken")).thenReturn(academicExperiences);

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceByBeneficiaryID(request, 1L);

		assertEquals(res, ResponseEntity.ok(academicExperiences));
	}
	
	@Test
	public void getAcademicExperienceByBeneficiaryIDWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
		academicExperienceDTO.setSpeciality("Idiomas");
		
		List<AcademicExperienceDTO> academicExperiences = List.of(academicExperienceDTO);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(academicExperienceService.getAcademicExperienceByBeneficiary(1L, "oken")).thenReturn(academicExperiences);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceByBeneficiaryID(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void getAcademicExperienceByBeneficiaryIDWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
 
		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true); // Simular un token válido
		when(academicExperienceService.getAcademicExperienceByBeneficiary(1L, "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG or volunteer to use this method.")); // Simular una excepción cuando el usuario no es una ONG
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token"); // Inyectar token válido

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceByBeneficiaryID(request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getAcademicExperienceByVolunteerID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
		List<AcademicExperienceDTO> academicExperiences = List.of(academicExperienceDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(academicExperienceService.getAcademicExperienceByVolunteer(1L, "oken")).thenReturn(academicExperiences);

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceByVolunteerUsername(request, 1L);

		assertEquals(res, ResponseEntity.ok(academicExperiences));
	}
	
	@Test
	public void getAcademicExperienceByVolunteerIDWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
		
		List<AcademicExperienceDTO> academicExperiences = List.of(academicExperienceDTO);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(academicExperienceService.getAcademicExperienceByVolunteer(1L, "oken")).thenReturn(academicExperiences);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceByVolunteerUsername(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void getAcademicExperienceByVolunteerIDWithOutBeingONGOrVolunteerTest_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
 
		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true); // Simular un token válido
		when(academicExperienceService.getAcademicExperienceByVolunteer(1L, "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG or volunteer to use this method.")); // Simular una excepción cuando el usuario no es una ONG
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token"); // Inyectar token válido

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceByVolunteerUsername(request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getAcademicExperienceByID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(academicExperienceService.getAcademicExpByID(1L, "oken")).thenReturn(academicExperienceDTO);

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceById(request, 1L);

		assertEquals(res, ResponseEntity.ok(academicExperienceDTO));
	}
	
	@Test
	public void getAcademicExperienceByIDWithOutToken_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(academicExperienceService.getAcademicExpByID(1L, "oken")).thenReturn(academicExperienceDTO);

		ResponseEntity<?> res = academicExperienceController.getAcademicExperienceById(request, 1L);

		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteAcademicExperienceTest() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);

		ResponseEntity<?> res = academicExperienceController.deleteAcademicExperience(1L, request);

		assertEquals(res, ResponseEntity.ok("Academic Experience deleted"));
	}
	
	@Test
	public void deleteAcademicExperienceWithOutTokenTest_negative() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		
		ResponseEntity<?> res = academicExperienceController.deleteAcademicExperience(1L, request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

}
