package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.dto.WorkExperienceDTO;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.repository.WorkExperienceRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WorkExperienceServiceImplTest {
		
	@InjectMocks
	private WorkExperienceServiceImpl workExperienceServiceImpl;
	
	@Mock
	private WorkExperienceRepository workExperienceRepository; 
	
	@Mock
	private SigninResponseDTO signinResponseDTO;
	
	@Mock
	private OngServiceImpl ongServiceImpl; 
	
	@Mock
	private JwtUtils jwtUtils;

	@Mock
	PasswordEncoder encoder;

	@Mock
	ONGRepository ongRepository;
	
	@Mock
	AccountRepository accountRepository;
	
	@Mock
	VolunteerRepository volunteerRepository;
	
	@Mock
	BeneficiaryRepository beneficiaryRepository;

	
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
	
	private WorkExperienceDTO createWorkExperience() {
		WorkExperienceDTO workExperience = new WorkExperienceDTO();
		workExperience.setJob("Ingeniero software");
		workExperience.setPlace("Sevilla");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("Junio-2022");
		
		return workExperience;
	}
	
	@Test
	public void saveWorkExperienceBeneficiary_positive() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(workExperienceRepository.save(any(WorkExperience.class))).thenAnswer(invocation -> {
        	WorkExperience workExperienceAux = invocation.getArgument(0);
        	workExperienceAux.setId(new Random().nextLong());
            return workExperienceAux;
        });
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
        WorkExperienceDTO result = workExperienceServiceImpl.saveWorkExperience(workExperienceDTO, 1L, token);
		
		Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(workExperienceDTO.getJob(), result.getJob());
        Assertions.assertEquals(workExperienceDTO.getPlace(), result.getPlace());
        Assertions.assertEquals(workExperienceDTO.getReasonToFinish(), result.getReasonToFinish());
        Assertions.assertEquals(workExperienceDTO.getTime(), result.getTime());
        verify(workExperienceRepository, times(1)).save(any(WorkExperience.class));
	}
	
	@Test
	public void saveWorkExperienceBeneficiary_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11234577");
		ong2.setDescription("description");
		ong2.setEmail("prueba@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("prueba");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("prueba");
		ong2.setId(1L);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong2.getUsername());
        when(ongRepository.findByUsername(eq(ong2.getUsername()))).thenReturn(ong2);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.saveWorkExperience(workExperienceDTO, 1L, token));
	}

	
	@Test
	public void saveWorkExperienceVolunteer_positive() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
        
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        when(workExperienceRepository.save(any(WorkExperience.class))).thenAnswer(invocation -> {
        	WorkExperience workExperienceAux = invocation.getArgument(0);
        	workExperienceAux.setId(new Random().nextLong());
            return workExperienceAux;
        });
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
        WorkExperienceDTO result = workExperienceServiceImpl.saveWorkExperience(workExperienceDTO, 1L, token);
		
		Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(workExperienceDTO.getJob(), result.getJob());
        Assertions.assertEquals(workExperienceDTO.getPlace(), result.getPlace());
        Assertions.assertEquals(workExperienceDTO.getReasonToFinish(), result.getReasonToFinish());
        Assertions.assertEquals(workExperienceDTO.getTime(), result.getTime());
        verify(workExperienceRepository, times(1)).save(any(WorkExperience.class));
	}
	
	@Test
	public void saveWorkExperienceVolunteer_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11234577");
		ong2.setDescription("description");
		ong2.setEmail("prueba@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("prueba");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("prueba");
		ong2.setId(1L);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong2.getUsername());
        when(ongRepository.findByUsername(eq(ong2.getUsername()))).thenReturn(ong2);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.saveWorkExperience(workExperienceDTO, 1L, token));
	}
		
	@Test
	public void saveWorkExperienceWithVolunteerWhoNotExists_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.saveWorkExperience(workExperienceDTO, 2L, token));
	}
	
	@Test
	public void saveWorkExperienceWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(volunteer.getUsername());
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
	
		assertThrows(OperationNotAllowedException.class, () -> workExperienceServiceImpl.saveWorkExperience(workExperienceDTO, 1L, token));
	}
	
	@Test
	public void updateWorkExperienceTestBeneficiary() throws OperationNotAllowedException {
		
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
		
		WorkExperience newWorkExperience = workExperience;
		newWorkExperience.setTime("2 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
		workExperienceDTO.setTime("2 años");
		workExperienceDTO.setId(1L);

		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		when(workExperienceRepository.save(any())).thenReturn(newWorkExperience);

		WorkExperienceDTO workExperienceDTOSaved = workExperienceServiceImpl.updateWorkExperience(token, workExperienceDTO, beneficiary.getId());

		assertNotNull(workExperienceDTOSaved);
		assertEquals(workExperience.getJob(), workExperienceDTOSaved.getJob());
		assertEquals(workExperience.getPlace(),workExperienceDTOSaved.getPlace());
		assertEquals(workExperience.getReasonToFinish(), workExperienceDTOSaved.getReasonToFinish());
		assertEquals("2 años", workExperienceDTOSaved.getTime());
	}
	
	@Test
	public void updateWorkExperienceTestVolunteer() throws OperationNotAllowedException {
		
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
		
		WorkExperience newWorkExperience = workExperience;
		newWorkExperience.setTime("2 años");
		WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
		workExperienceDTO.setTime("2 años");
		workExperienceDTO.setId(1L);

		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		when(workExperienceRepository.save(any())).thenReturn(newWorkExperience);

		WorkExperienceDTO workExperienceDTOSaved = workExperienceServiceImpl.updateWorkExperience(token, workExperienceDTO, volunteer.getId());

		assertNotNull(workExperienceDTOSaved);
	}
	
	@Test
	public void updateWorkExperienceVolunteerOfOtherONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11234577");
		ong2.setDescription("description");
		ong2.setEmail("prueba@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("prueba");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("prueba");
		ong2.setId(1L);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong2.getUsername());
        when(ongRepository.findByUsername(eq(ong2.getUsername()))).thenReturn(ong2);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.updateWorkExperience(token, workExperienceDTO, 1L));
	}
	
	@Test
	public void updateWorkExperienceBeneficiaryOfOtherONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11234577");
		ong2.setDescription("description");
		ong2.setEmail("prueba@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("prueba");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("prueba");
		ong2.setId(1L);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong2.getUsername());
        when(ongRepository.findByUsername(eq(ong2.getUsername()))).thenReturn(ong2);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
    	
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.updateWorkExperience(token, workExperienceDTO, 1L));
	}
	
	@Test
	public void updateWorkExperienceWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(beneficiary.getUsername());
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        
        WorkExperienceDTO workExperienceDTO = createWorkExperience();
    	
		assertThrows(OperationNotAllowedException.class, () -> workExperienceServiceImpl.updateWorkExperience(token, workExperienceDTO, 1L));
	}

	@Test
	public void deleteWorkExperienceOfVolunteerTest() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		workExperienceServiceImpl.deleteWorkExperience(Long.valueOf(1), token);
	}
	
	@Test
	public void deleteWorkExperienceOfBeneficiaryTest() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		workExperienceServiceImpl.deleteWorkExperience(Long.valueOf(1), token);
	}
	
	@Test
	public void deleteWorkExperienceWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(beneficiary.getUsername());
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        	
		assertThrows(OperationNotAllowedException.class, () -> workExperienceServiceImpl.deleteWorkExperience(1L, token));
	}
	
	@Test
	public void deleteWorkExperienceThatNotExists_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        	
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.deleteWorkExperience(3L, token));
	}
	
	@Test
	public void deleteWorkExperienceOfBeneficiaryTest_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11234577");
		ong2.setDescription("description");
		ong2.setEmail("prueba@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("prueba");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("prueba");
		ong2.setId(1L);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong2.getUsername());
        when(ongRepository.findByUsername(eq(ong2.getUsername()))).thenReturn(ong2);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        
        WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
	
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.deleteWorkExperience(1L, token));
		
	}
	
	@Test
	public void getWorkExperienceByBeneficiaryTest() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(workExperienceRepository.findWorkExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(workExperiences));

		List<WorkExperienceDTO> workExperiencesOfBeneficiary = workExperienceServiceImpl.getWorkExperienceByBeneficiary(beneficiary.getId(), token);
		assertNotNull(workExperiencesOfBeneficiary);
		assertEquals(workExperiencesOfBeneficiary.size(), 1);
	}
	
	@Test
	public void getWorkExperienceOfBeneficiaryWhoNotExistsTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(workExperienceRepository.findWorkExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(workExperiences));

		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.getWorkExperienceByBeneficiary(45L, token));

	}
	
	@Test
	public void getWorkExperienceOfBenificaryOfOtherONG_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11234577");
		ong2.setDescription("description");
		ong2.setEmail("prueba@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("prueba");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("prueba");
		ong2.setId(1L);
		
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("prueba");
		when(ongRepository.findByUsername("prueba")).thenReturn(ong2);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(workExperienceRepository.findWorkExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(workExperiences));

		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.getWorkExperienceByBeneficiary(1L, token));

	}
	
	@Test
	public void getWorkExperienceOfBeneficiaryWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("angel123");
		when(beneficiaryRepository.findByUsername("angel123")).thenReturn(beneficiary);
		when(workExperienceRepository.findWorkExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(workExperiences));

		assertThrows(OperationNotAllowedException.class, () -> workExperienceServiceImpl.getWorkExperienceByBeneficiary(1L, token));

	}
	
	@Test
	public void getWorkExperienceByVolunteerTest() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(workExperienceRepository.findWorkExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(workExperiences));

		List<WorkExperienceDTO> workExperiencesOfVolunteer = workExperienceServiceImpl.getWorkExperienceByVolunteer(volunteer.getId(), token);
		assertNotNull(workExperiencesOfVolunteer);
		assertEquals(workExperiencesOfVolunteer.size(), 1);

	}
	
	@Test
	public void getWorkExperienceOfVolunteerWhoNotExistsTest_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(volunteer));
		when(workExperienceRepository.findWorkExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(workExperiences));

		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.getWorkExperienceByBeneficiary(45L, token));

	}
	
	@Test
	public void getWorkExperienceOfVolunteerOfOtherONG_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11234577");
		ong2.setDescription("description");
		ong2.setEmail("prueba@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("prueba");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("prueba");
		ong2.setId(1L);
		
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("prueba");
		when(ongRepository.findByUsername("prueba")).thenReturn(ong2);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(workExperienceRepository.findWorkExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(workExperiences));

		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.getWorkExperienceByBeneficiary(1L, token));

	}
	
	@Test
	public void getWorkExperienceOfVolunteerWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer(); 
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");

		String token = "tokenprueba";
		List<WorkExperience> workExperiences = List.of(workExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("angel123");
		when(volunteerRepository.findByUsername("angel123")).thenReturn(volunteer);
		when(workExperienceRepository.findWorkExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(workExperiences));

		assertThrows(OperationNotAllowedException.class, () -> workExperienceServiceImpl.getWorkExperienceByBeneficiary(1L, token));

	}
	
	@Test
	public void getWorkExperienceOfVolunteeryByIDTest() throws OperationNotAllowedException {
		
		String token = "tokenprueba";
		
		Account account = new Account();
		account.setRolAccount(RolAccount.ONG);
		account.setUsername("test");
		account.setEmail("test@gmail.com");
		account.setPassword("asdf1234");
		when(accountRepository.findByUsername("test")).thenReturn(account);
		
		Volunteer volunteer = createVolunteer();
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		
		Ong ong = createOng();
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(account.getUsername());
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setVolunteer(volunteer);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		
		volunteer.setOng(ong);
		WorkExperienceDTO workExperienceDTO = workExperienceServiceImpl.getWorkExperienceById(Long.valueOf(1), token);
		assertNotNull(workExperienceDTO);
		assertEquals(workExperienceDTO.getJob(), "Obrero");
		assertEquals(workExperienceDTO.getPlace(), "Huelva");
		assertEquals(workExperienceDTO.getId(), 1);
		assertEquals(workExperienceDTO.getReasonToFinish(), "Cansancio");
		assertEquals(workExperienceDTO.getTime(), "3 años");

	}
	
	@Test
	public void getWorkExperienceOfBeneficiaryByIDTest() throws OperationNotAllowedException {
		
		String token = "tokenprueba";
		
		Account account = new Account();
		account.setRolAccount(RolAccount.ONG);
		account.setUsername("test");
		account.setEmail("test@gmail.com");
		account.setPassword("asdf1234");
		when(accountRepository.findByUsername("test")).thenReturn(account);
		
		Beneficiary beneficiary = createBeneficiary();
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		
		Ong ong = createOng();
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(account.getUsername());
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		
		beneficiary.setOng(ong);
		WorkExperienceDTO workExperienceDTO = workExperienceServiceImpl.getWorkExperienceById(Long.valueOf(1), token);
		assertNotNull(workExperienceDTO);
		assertEquals(workExperienceDTO.getJob(), "Obrero");
		assertEquals(workExperienceDTO.getPlace(), "Huelva");
		assertEquals(workExperienceDTO.getId(), 1);
		assertEquals(workExperienceDTO.getReasonToFinish(), "Cansancio");
		assertEquals(workExperienceDTO.getTime(), "3 años");
	}
	
	@Test
	public void getWorkExperienceThatNotExistsByIDTest_negative() throws OperationNotAllowedException {
		
		String token = "tokenprueba";
		
		Account account = new Account();
		account.setRolAccount(RolAccount.ONG);
		account.setUsername("test");
		account.setEmail("test@gmail.com");
		account.setPassword("asdf1234");
		when(accountRepository.findByUsername("test")).thenReturn(account);
		
		Beneficiary beneficiary = createBeneficiary();
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		
		Ong ong = createOng();
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(account.getUsername());
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		
		
		beneficiary.setOng(ong);
		assertThrows(UsernameNotFoundException.class, () -> workExperienceServiceImpl.getWorkExperienceById(45L, token));

	}
	
	@Test
	public void getWorkExperienceWithOutPermission_negative() throws OperationNotAllowedException {
		
		String token = "tokenprueba";
		
		Account account = new Account();
		account.setRolAccount(RolAccount.ONG);
		account.setUsername("test");
		account.setEmail("test@gmail.com");
		account.setPassword("asdf1234");
		when(accountRepository.findByUsername("test")).thenReturn(account);
		
		Beneficiary beneficiary = createBeneficiary();
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		
		Ong ong = createOng();
		when(ongRepository.findByUsername("angel123")).thenReturn(ong);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(account.getUsername());
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setBeneficiary(beneficiary);
		workExperience.setId(1L);
		workExperience.setJob("Obrero");
		workExperience.setPlace("Huelva");
		workExperience.setReasonToFinish("Cansancio");
		workExperience.setTime("3 años");
		when(workExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(workExperience));
		
		
		beneficiary.setOng(ong);
		assertThrows(OperationNotAllowedException.class, () -> workExperienceServiceImpl.getWorkExperienceById(45L, token));

	}
}