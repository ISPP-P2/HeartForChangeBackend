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

import com.ispp.heartforchange.dto.ComplementaryFormationDTO;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.ComplementaryFormation;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.ComplementaryFormationServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ComplementaryFormationControllerTest {
	
	@InjectMocks
	private ComplementaryFormationController complementaryFormationController;
	
	@Mock
	private ComplementaryFormationServiceImpl complementaryFormationService;

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
    	beneficiary.setAddress("Calle Ejemplo");
    	beneficiary.setArrivedDate(LocalDate.now());
    	beneficiary.setBirthday(LocalDate.now());
    	beneficiary.setCivilStatus(CivilStatus.SINGLE);
    	beneficiary.setComputerKnowledge(true);
    	beneficiary.setDateTouristVisa(null);
    	beneficiary.setDocumentNumber("90335072L");
    	beneficiary.setDocumentType(DocumentType.DNI);
    	beneficiary.setDoubleNationality(false);
    	beneficiary.setDriveLicenses(null);
    	beneficiary.setEmail("ejemplo@hotmail.es");
    	beneficiary.setEmploymentSector(null);
    	beneficiary.setEntryDate(LocalDate.now());
    	beneficiary.setEuropeanCitizenAuthorization(true);
    	beneficiary.setFirstSurname("Pérez");
    	beneficiary.setGender(Gender.MALE);
    	beneficiary.setHealthCard(true);
    	beneficiary.setId(1L);
    	beneficiary.setLanguages(null);
    	beneficiary.setLeavingDate(null);
    	beneficiary.setListAcademicExperience(null);
    	beneficiary.setListAppointments(null);
    	beneficiary.setListComplementaryFormation(null);
    	beneficiary.setListWorkExperience(null);
    	beneficiary.setName("Luis");
    	beneficiary.setNationality("Español");
    	beneficiary.setNumberOfChildren(2);
    	beneficiary.setOtherSkills(null);
    	beneficiary.setOwnedDevices("Movil");
    	beneficiary.setPassword("abcd12");
    	beneficiary.setPerceptionAid(null);
    	beneficiary.setPostalCode("473621");
    	beneficiary.setRegistrationAddress("asd");
    	beneficiary.setRolAccount(RolAccount.BENEFICIARY);
    	beneficiary.setSaeInscription(false);
    	beneficiary.setSavingsPossesion(false);
    	beneficiary.setSecondSurname("Ruiz");
    	beneficiary.setTelephone("698112334");
    	beneficiary.setTouristVisa(false);
    	beneficiary.setTown("Sevilla");
    	beneficiary.setUsername("luis14");
    	beneficiary.setWorking(true);
    	
    	beneficiaryRepository.save(beneficiary);
    	
    	return beneficiary;
    }
	
	public Volunteer createVolunteer() {
    	Volunteer volunteer = new Volunteer();
    	volunteer.setAddress("Calle Ejemplo");
    	volunteer.setBirthday(LocalDate.now());
    	volunteer.setCivilStatus(CivilStatus.SINGLE);
    	volunteer.setDocumentNumber("90335072L");
    	volunteer.setDocumentType(DocumentType.DNI);
    	volunteer.setDriveLicenses(null);
    	volunteer.setEmail("ejemplo@hotmail.es");
    	volunteer.setEntryDate(LocalDate.now());
    	volunteer.setFirstSurname("Pérez");
    	volunteer.setGender(Gender.MALE);
    	volunteer.setId(1L);
    	volunteer.setLeavingDate(null);
    	volunteer.setListAcademicExperience(null);
    	volunteer.setListWorkExperience(null);
    	volunteer.setComplementaryFormation(null);
    	volunteer.setName("Luis");
    	volunteer.setNumberOfChildren(2);
    	volunteer.setOtherSkills(null);
    	volunteer.setPassword("abc12");
    	volunteer.setPostalCode("473621");
    	volunteer.setRegistrationAddress("asd");
    	volunteer.setRolAccount(RolAccount.VOLUNTEER);
    	volunteer.setSecondSurname("Ruiz");
    	volunteer.setTelephone("698112334");
    	volunteer.setTown("Sevilla");
    	volunteer.setUsername("luis14");
    	volunteer.setHourOfAvailability("Nunca");
    	volunteer.setSexCrimes(false);
    	
    	volunteerRepository.save(volunteer);
    	
    	return volunteer;
    }
	
	@Test
	public void saveComplementaryFormation() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(complementaryFormationService.saveComplementaryFormation(complementaryFormationDTO, Long.valueOf(1), "oken")).thenReturn(complementaryFormationDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = complementaryFormationController.saveComplementaryFormation(request, complementaryFormationDTO, 1L);
		assertEquals(res, ResponseEntity.ok(complementaryFormationDTO));
	}
	
	@Test
	public void saveComplementaryFormationWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(complementaryFormationService.saveComplementaryFormation(complementaryFormationDTO, Long.valueOf(1), "oken")).thenReturn(complementaryFormationDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = complementaryFormationController.saveComplementaryFormation(request, complementaryFormationDTO, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void saveComplementaryFormationWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);


		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(complementaryFormationService.saveComplementaryFormation(complementaryFormationDTO, Long.valueOf(1), "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = complementaryFormationController.saveComplementaryFormation(request, complementaryFormationDTO, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void updateComplementaryFormationTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(complementaryFormationService.updateComplementaryFormation("oken", complementaryFormationDTO, 1L)).thenReturn(complementaryFormationDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = complementaryFormationController.updateComplementaryFormation(complementaryFormationDTO, request, 1L);
		assertEquals(res, ResponseEntity.ok(complementaryFormationDTO));
	}
	
	@Test
	public void updateComplementaryFormationWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
		complementaryFormationDTO.setPlace("Málaga");
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(complementaryFormationService.updateComplementaryFormation("oken", complementaryFormationDTO, 1L)).thenReturn(complementaryFormationDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = complementaryFormationController.updateComplementaryFormation(complementaryFormationDTO, request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void updateComplementaryFormationWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(complementaryFormationService.updateComplementaryFormation("valid_token", complementaryFormationDTO, 1L)).thenThrow(new OperationNotAllowedException("You must be an ONG to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = complementaryFormationController.updateComplementaryFormation(complementaryFormationDTO, request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getComplementaryFormationByBeneficiaryID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		List<ComplementaryFormationDTO> complementaryFormations = List.of(complementaryFormationDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(complementaryFormationService.getComplementaryFormationByBeneficiary(1L, "oken")).thenReturn(complementaryFormations);

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationByBeneficiary(request, 1L);

		assertEquals(res, ResponseEntity.ok(complementaryFormations));
	}
	
	@Test
	public void getComplementaryFormationByBeneficiaryIDWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		List<ComplementaryFormationDTO> complementaryFormations = List.of(complementaryFormationDTO);

		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(complementaryFormationService.getComplementaryFormationByBeneficiary(1L, "oken")).thenReturn(complementaryFormations);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationByBeneficiary(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void getComplementaryFormationByBeneficiaryIDWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		
		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(complementaryFormationService.getComplementaryFormationByBeneficiary(1L, "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG or volunteer to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationByBeneficiary(request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getComplementaryFormationByVolunteerID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		List<ComplementaryFormationDTO> complementaryFormations = List.of(complementaryFormationDTO);


		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(complementaryFormationService.getComplementaryFormationByVolunteer(1L, "oken")).thenReturn(complementaryFormations);

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationByVolunteer(request, 1L);

		assertEquals(res, ResponseEntity.ok(complementaryFormations));
	}
	
	@Test
	public void getComplementaryFormationByVolunteerIDWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);

		List<ComplementaryFormationDTO> complementaryFormations = List.of(complementaryFormationDTO);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(complementaryFormationService.getComplementaryFormationByVolunteer(1L, "oken")).thenReturn(complementaryFormations);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationByVolunteer(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	@Test
	public void getComplementaryFormationByVolunteerIDWithOutBeingONGOrVolunteerTest_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
 
		when(jwtUtils.validateJwtToken("valid_token")).thenReturn(true);
		when(complementaryFormationService.getComplementaryFormationByVolunteer(1L, "valid_token")).thenThrow(new OperationNotAllowedException("You must be an ONG or volunteer to use this method."));
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationByVolunteer(request, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}
	
	@Test
	public void getComplementaryFormationByID() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);


		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(complementaryFormationService.getComplementaryFormationById(1L, "oken")).thenReturn(complementaryFormationDTO);

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationById(request, 1L);

		assertEquals(res, ResponseEntity.ok(complementaryFormationDTO));
	}
	
	@Test
	public void getComplementaryFormationByIDWithOutToken_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);


		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(complementaryFormationService.getComplementaryFormationById(1L, "oken")).thenReturn(complementaryFormationDTO);

		ResponseEntity<?> res = complementaryFormationController.getComplementaryFormationById(request, 1L);

		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteComplementaryFormationTest() throws OperationNotAllowedException {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);

		ResponseEntity<?> res = complementaryFormationController.deleteComplementaryFormation(1L, request);

		assertEquals(res, ResponseEntity.ok("Complementary formation deleted"));
	}
	
	@Test
	public void deleteComplementaryFormationWithOutTokenTest_negative() throws OperationNotAllowedException {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		
		ResponseEntity<?> res = complementaryFormationController.deleteComplementaryFormation(1L, request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}


}
