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

import com.ispp.heartforchange.dto.AcademicExperienceDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AcademicExperienceRepository;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AcademicServiceImplTest {
		
	@InjectMocks
	private AcademicExperienceServiceImpl academicExperienceServiceImpl;
	
	@Mock
	private AcademicExperienceRepository academicExperienceRepository; 
	
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
	
	private AcademicExperienceDTO createAcademicExperience() {
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO();
		academicExperienceDTO.setEducationalLevel("ESO");
		academicExperienceDTO.setEndingYear(2021);
		academicExperienceDTO.setSatisfactionDegree(5);
		academicExperienceDTO.setSpeciality("Idiomas");
		
		return academicExperienceDTO;
	}
	
	@Test
	public void saveAcademicExperienceBeneficiary_positive() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(academicExperienceRepository.save(any(AcademicExperience.class))).thenAnswer(invocation -> {
            AcademicExperience academicExperienceAux = invocation.getArgument(0);
            academicExperienceAux.setId(new Random().nextLong());
            return academicExperienceAux;
        });
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
		AcademicExperienceDTO result = academicExperienceServiceImpl.saveAcademicExperience(academicExperienceDTO , 1L, token);
		
		Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(academicExperienceDTO.getEndingYear(), result.getEndingYear());
        Assertions.assertEquals(academicExperienceDTO.getSatisfactionDegree(), result.getSatisfactionDegree());
        Assertions.assertEquals(academicExperienceDTO.getEducationalLevel(), result.getEducationalLevel());
        Assertions.assertEquals(academicExperienceDTO.getSpeciality(), result.getSpeciality());
        verify(academicExperienceRepository, times(1)).save(any(AcademicExperience.class));
	}
	
	@Test
	public void saveAcademicExperienceBeneficiary_negative() throws OperationNotAllowedException {
		
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
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.saveAcademicExperience(academicExperienceDTO, 1L, token));
	}

	
	@Test
	public void saveAcademicExperienceVolunteer_positive() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        when(academicExperienceRepository.save(any(AcademicExperience.class))).thenAnswer(invocation -> {
            AcademicExperience academicExperienceAux = invocation.getArgument(0);
            academicExperienceAux.setId(new Random().nextLong());
            return academicExperienceAux;
        });
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
		AcademicExperienceDTO result = academicExperienceServiceImpl.saveAcademicExperience(academicExperienceDTO , 1L, token);
		
		Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(academicExperienceDTO.getEndingYear(), result.getEndingYear());
        Assertions.assertEquals(academicExperienceDTO.getSatisfactionDegree(), result.getSatisfactionDegree());
        Assertions.assertEquals(academicExperienceDTO.getEducationalLevel(), result.getEducationalLevel());
        Assertions.assertEquals(academicExperienceDTO.getSpeciality(), result.getSpeciality());
        verify(academicExperienceRepository, times(1)).save(any(AcademicExperience.class));
	}
	
	@Test
	public void saveAcademicExperienceVolunteer_negative() throws OperationNotAllowedException {
		
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
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.saveAcademicExperience(academicExperienceDTO, 1L, token));
	}
		
	@Test
	public void saveAcademicExperienceWithVolunteerWhoNotExists_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.saveAcademicExperience(academicExperienceDTO, 2L, token));
	}
	
	@Test
	public void saveAcademicExperienceWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(volunteer.getUsername());
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
	
		assertThrows(OperationNotAllowedException.class, () -> academicExperienceServiceImpl.saveAcademicExperience(academicExperienceDTO, 1L, token));
	}
	
	@Test
	public void updateAcademicExperienceTestBeneficiary() throws OperationNotAllowedException {
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		System.out.println(academicExperience.getSatisfactionDegree());
		AcademicExperience newAcademicExperience = academicExperience;
		newAcademicExperience.setSatisfactionDegree(4);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
		academicExperienceDTO.setSatisfactionDegree(4);
		academicExperienceDTO.setId(1L);

		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		when(academicExperienceRepository.save(any())).thenReturn(newAcademicExperience);

		AcademicExperienceDTO academicExperienceDTOSaved = academicExperienceServiceImpl.updateAcademicExperience(academicExperienceDTO, token, beneficiary.getId());

		assertNotNull(academicExperienceDTOSaved);
		assertEquals(academicExperience.getSpeciality(), academicExperienceDTOSaved.getSpeciality());
		assertEquals(academicExperience.getEndingYear(),academicExperienceDTOSaved.getEndingYear());
		assertEquals(academicExperience.getEducationalLevel(), academicExperienceDTOSaved.getEducationalLevel());
		assertEquals(4, academicExperienceDTOSaved.getSatisfactionDegree());
	}
	
	@Test
	public void updateAcademicExperienceTestVolunteer() throws OperationNotAllowedException {
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		AcademicExperience newAcademicExperience = academicExperience;
		newAcademicExperience.setSatisfactionDegree(4);
		AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
		academicExperienceDTO.setSatisfactionDegree(4);
		academicExperienceDTO.setId(1L);

		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		when(academicExperienceRepository.save(any())).thenReturn(newAcademicExperience);

		AcademicExperienceDTO academicExperienceDTOSaved = academicExperienceServiceImpl.updateAcademicExperience(academicExperienceDTO, token, volunteer.getId());

		assertNotNull(academicExperienceDTOSaved);
	}
	
	@Test
	public void updateAcademicExperienceVolunteerOfOtherONG_negative() throws OperationNotAllowedException {
		
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
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.updateAcademicExperience(academicExperienceDTO, token, 1L));
	}
	
	@Test
	public void updateAcademicExperienceBeneficiaryOfOtherONG_negative() throws OperationNotAllowedException {
		
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
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
	
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.updateAcademicExperience(academicExperienceDTO, token, 1L));
	}
	
	@Test
	public void updateAcademicExperienceWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(beneficiary.getUsername());
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        
		AcademicExperienceDTO academicExperienceDTO = createAcademicExperience();
	
		assertThrows(OperationNotAllowedException.class, () -> academicExperienceServiceImpl.updateAcademicExperience(academicExperienceDTO, token, 1L));
	}

	@Test
	public void deleteAcademicExperienceOfVolunteerTest() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		academicExperience.setVolunteer(volunteer);

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		academicExperienceServiceImpl.deleteAcademicExperience(Long.valueOf(1), token);
	}
	
	@Test
	public void deleteAcademicExperienceOfBeneficiaryTest() throws OperationNotAllowedException {
		Beneficiary beneficiary = createBeneficiary();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary,null);
		academicExperience.setBeneficiary(beneficiary);

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		academicExperienceServiceImpl.deleteAcademicExperience(Long.valueOf(1), token);
	}
	
	@Test
	public void deleteAcademicExperienceWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(beneficiary.getUsername());
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        	
		assertThrows(OperationNotAllowedException.class, () -> academicExperienceServiceImpl.deleteAcademicExperience(1L, token));
	}
	
	@Test
	public void deleteAcademicExperienceThatNotExists_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        	
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.deleteAcademicExperience(3L, token));
	}
	
	@Test
	public void deleteAcademicExperienceOfBeneficiaryTest_negative() throws OperationNotAllowedException {
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
        
        AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		academicExperience.setBeneficiary(beneficiary);
	
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.deleteAcademicExperience(1L, token));
		
	}
	
	@Test
	public void getAcademyExperienceByBeneficiaryTest() throws OperationNotAllowedException {
		Beneficiary beneficiary = createBeneficiary();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		academicExperience.setBeneficiary(beneficiary);

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(academicExperienceRepository.findAcademicExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(academicExperiences));

		List<AcademicExperienceDTO> academicExperiencesOfBeneficiary = academicExperienceServiceImpl.getAcademicExperienceByBeneficiary(beneficiary.getId(), token);
		assertNotNull(academicExperiencesOfBeneficiary);
		assertEquals(academicExperiencesOfBeneficiary.size(), 1);
	}
	
	@Test
	public void getAcademyExperienceOfBeneficiaryWhoNotExistsTest_negative() throws OperationNotAllowedException {
		Beneficiary beneficiary = createBeneficiary();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		academicExperience.setBeneficiary(beneficiary);

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(academicExperienceRepository.findAcademicExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(academicExperiences));

		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.getAcademicExperienceByBeneficiary(45L, token));

	}
	
	@Test
	public void getAcademyExperienceOfBenificaryOfOtherONG_negative() throws OperationNotAllowedException {
		Beneficiary beneficiary = createBeneficiary();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		academicExperience.setBeneficiary(beneficiary);

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
		
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("prueba");
		when(ongRepository.findByUsername("prueba")).thenReturn(ong2);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(academicExperienceRepository.findAcademicExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(academicExperiences));

		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.getAcademicExperienceByBeneficiary(1L, token));

	}
	
	@Test
	public void getAcademyExperienceOfBeneficiaryWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		Beneficiary beneficiary = createBeneficiary();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", beneficiary, null);
		academicExperience.setBeneficiary(beneficiary);

		String token = "tokenprueba";
		Ong ong = createOng();
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("angel123");
		when(beneficiaryRepository.findByUsername("angel123")).thenReturn(beneficiary);
		when(academicExperienceRepository.findAcademicExperienceByBeneficiaryId(beneficiary.getId())).thenReturn(Optional.of(academicExperiences));

		assertThrows(OperationNotAllowedException.class, () -> academicExperienceServiceImpl.getAcademicExperienceByBeneficiary(1L, token));

	}
	
	@Test
	public void getAcademyExperienceByVolunteerTest() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		academicExperience.setVolunteer(volunteer);

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(academicExperienceRepository.findAcademicExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(academicExperiences));

		List<AcademicExperienceDTO> academicExperiencesOfVolunteer = academicExperienceServiceImpl.getAcademicExperienceByVolunteer(volunteer.getId(), token);
		assertNotNull(academicExperiencesOfVolunteer);
		assertEquals(academicExperiencesOfVolunteer.size(), 1);

	}
	
	@Test
	public void getAcademyExperienceOfVolunteerWhoNotExistsTest_negative() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		academicExperience.setVolunteer(volunteer);

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(volunteer));
		when(academicExperienceRepository.findAcademicExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(academicExperiences));

		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.getAcademicExperienceByVolunteer(45L, token));

	}
	
	@Test
	public void getAcademyExperienceOfVolunteerOfOtherONG_negative() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		academicExperience.setVolunteer(volunteer);

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
		
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("prueba");
		when(ongRepository.findByUsername("prueba")).thenReturn(ong2);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(academicExperienceRepository.findAcademicExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(academicExperiences));

		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.getAcademicExperienceByVolunteer(1L, token));

	}
	
	@Test
	public void getAcademyExperienceOfVolunteerWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer(); 
		AcademicExperience academicExperience = new AcademicExperience(1L, "Comer", 2009, 2, "Bach", null, volunteer);
		academicExperience.setVolunteer(volunteer);

		String token = "tokenprueba";
		Ong ong = createOng();
		List<AcademicExperience> academicExperiences = List.of(academicExperience);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("angel123");
		when(volunteerRepository.findByUsername("angel123")).thenReturn(volunteer);
		when(academicExperienceRepository.findAcademicExperienceByVolunteerId(volunteer.getId())).thenReturn(Optional.of(academicExperiences));

		assertThrows(OperationNotAllowedException.class, () -> academicExperienceServiceImpl.getAcademicExperienceByVolunteer(1L, token));

	}
	
	@Test
	public void getAcademyExperienceOfVolunteeryByIDTest() throws OperationNotAllowedException {
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
		
		AcademicExperience academicExperience = new AcademicExperience(Long.valueOf(1), "Comer", 2009, 2, "Bach", null, volunteer);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		
		volunteer.setOng(ong);
		AcademicExperienceDTO academicExperienceDTO = academicExperienceServiceImpl.getAcademicExpByID(Long.valueOf(1), token);
		assertNotNull(academicExperienceDTO);
		assertEquals(academicExperienceDTO.getEducationalLevel(), "Bach");
		assertEquals(academicExperienceDTO.getEndingYear(), 2009);
		assertEquals(academicExperienceDTO.getId(), 1);
		assertEquals(academicExperienceDTO.getSatisfactionDegree(), 2);
		assertEquals(academicExperienceDTO.getSpeciality(), "Comer");

	}
	
	@Test
	public void getAcademyExperienceOfBeneficiaryByIDTest() throws OperationNotAllowedException {
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
		
		AcademicExperience academicExperience = new AcademicExperience(Long.valueOf(1), "Comer", 2009, 2, "Bach", beneficiary, null);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		
		beneficiary.setOng(ong);
		AcademicExperienceDTO academicExperienceDTO = academicExperienceServiceImpl.getAcademicExpByID(Long.valueOf(1), token);
		assertNotNull(academicExperienceDTO);
		assertEquals(academicExperienceDTO.getEducationalLevel(), "Bach");
		assertEquals(academicExperienceDTO.getEndingYear(), 2009);
		assertEquals(academicExperienceDTO.getId(), 1);
		assertEquals(academicExperienceDTO.getSatisfactionDegree(), 2);
		assertEquals(academicExperienceDTO.getSpeciality(), "Comer");
	}
	
	@Test
	public void getAcademyExperienceThatNotExistsByIDTest_negative() throws OperationNotAllowedException {
		
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
		
		AcademicExperience academicExperience = new AcademicExperience(Long.valueOf(1), "Comer", 2009, 2, "Bach", beneficiary, null);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		
		beneficiary.setOng(ong);
		assertThrows(UsernameNotFoundException.class, () -> academicExperienceServiceImpl.getAcademicExpByID(45L, token));

	}
	
	@Test
	public void getAcademyExperienceWithOutPermission_negative() throws OperationNotAllowedException {
		
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
		
		AcademicExperience academicExperience = new AcademicExperience(Long.valueOf(1), "Comer", 2009, 2, "Bach", beneficiary, null);
		when(academicExperienceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(academicExperience));
		
		beneficiary.setOng(ong);
		assertThrows(OperationNotAllowedException.class, () -> academicExperienceServiceImpl.getAcademicExpByID(45L, token));

	}
}
