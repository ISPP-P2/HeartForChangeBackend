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
import com.ispp.heartforchange.dto.ComplementaryFormationDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.ComplementaryFormation;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ComplementaryFormationRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ComplementaryFormationServiceImplTest {
	
	@InjectMocks
	private ComplementaryFormationServiceImpl complementaryFormationServiceImpl;
	
	@Mock
	private ComplementaryFormationRepository complementaryFormationRepository; 
	
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
		ong.setCif("A98174328");
		ong.setDescription("description test");
		ong.setEmail("test@gmail.com");
		ong.setId(Long.valueOf(0));
		ong.setName("CPTest");
		ong.setPassword("hola1234");
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
	
	private ComplementaryFormationDTO createComplementaryFormation() {
		ComplementaryFormationDTO complementaryFormation = new ComplementaryFormationDTO();
		complementaryFormation.setName("Bachillerato");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		
		return complementaryFormation;
	}
	
	@Test
	public void saveComplementaryFormationBeneficiary_positive() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        when(complementaryFormationRepository.save(any(ComplementaryFormation.class))).thenAnswer(invocation -> {
        	ComplementaryFormation complementaryFormationAux = invocation.getArgument(0);
        	complementaryFormationAux.setId(new Random().nextLong());
            return complementaryFormationAux;
        });
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
        ComplementaryFormationDTO result = complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, 1L, token);
		
		Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(complementaryFormationDTO.getName(), result.getName());
        Assertions.assertEquals(complementaryFormationDTO.getOrganization(), result.getOrganization());
        Assertions.assertEquals(complementaryFormationDTO.getDate(), result.getDate());
        Assertions.assertEquals(complementaryFormationDTO.getPlace(), result.getPlace());
        verify(complementaryFormationRepository, times(1)).save(any(ComplementaryFormation.class));
	}
	
	@Test
	public void saveComplementaryFormationBeneficiary_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
		
		Ong ong2 = new Ong();
		ong2.setCif("G11012543");
		ong2.setDescription("description");
		ong2.setEmail("test2@gmail.com");
		ong2.setId(Long.valueOf(0));
		ong2.setName("Test2");
		ong2.setPassword("asdf1234");
		ong2.setRolAccount(RolAccount.ONG);
		ong2.setUsername("test2");
		ong2.setId(1L);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong2.getUsername());
        when(ongRepository.findByUsername(eq(ong2.getUsername()))).thenReturn(ong2);
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
	
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, 1L, token));
	}

	
	@Test
	public void saveComplementaryFormationVolunteer_positive() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
        
        String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        when(complementaryFormationRepository.save(any(ComplementaryFormation.class))).thenAnswer(invocation -> {
        	ComplementaryFormation complementaryFormationAux = invocation.getArgument(0);
        	complementaryFormationAux.setId(new Random().nextLong());
            return complementaryFormationAux;
        });
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
        ComplementaryFormationDTO result = complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, 1L, token);
		
		Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(complementaryFormationDTO.getName(), result.getName());
        Assertions.assertEquals(complementaryFormationDTO.getOrganization(), result.getOrganization());
        Assertions.assertEquals(complementaryFormationDTO.getDate(), result.getDate());
        Assertions.assertEquals(complementaryFormationDTO.getPlace(), result.getPlace());
        verify(complementaryFormationRepository, times(1)).save(any(ComplementaryFormation.class));
	}
	
	@Test
	public void saveComplementaryFormationVolunteer_negative() throws OperationNotAllowedException {
		
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
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
	
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, 1L, token));
	}
		
	@Test
	public void saveComplementaryFormationWithVolunteerWhoNotExists_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
	
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, 2L, token));
	}
	
	@Test
	public void saveComplementaryFormationWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(volunteer.getUsername());
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
	
		assertThrows(OperationNotAllowedException.class, () -> complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, 1L, token));
	}
	
	@Test
	public void updateComplementaryFormationTestBeneficiary() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);
	
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		
		ComplementaryFormation newComplementaryFormation = complementaryFormation;
		newComplementaryFormation.setPlace("Jaén");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
		complementaryFormationDTO.setPlace("Jaén");
		complementaryFormationDTO.setId(1L);

		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		when(complementaryFormationRepository.save(any())).thenReturn(newComplementaryFormation);

		ComplementaryFormationDTO complementaryFormationDTOSaved = complementaryFormationServiceImpl.updateComplementaryFormation(token, complementaryFormationDTO, beneficiary.getId());

		assertNotNull(complementaryFormationDTOSaved);
		assertEquals(complementaryFormation.getName(), complementaryFormationDTOSaved.getName());
		assertEquals(complementaryFormation.getOrganization(),complementaryFormationDTOSaved.getOrganization());
		assertEquals(complementaryFormation.getDate(), complementaryFormationDTOSaved.getDate());
		assertEquals("Jaén", complementaryFormationDTOSaved.getPlace());
	}
	
	@Test
	public void updateComplementaryFormationTestVolunteer() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("Primaria");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 2, 2));
		complementaryFormation.setPlace("Sevilla");
		
		ComplementaryFormation newComplementaryFormation = complementaryFormation;
		newComplementaryFormation.setPlace("Málaga");
		ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
		complementaryFormationDTO.setPlace("Málaga");
		complementaryFormationDTO.setId(1L);

		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		when(complementaryFormationRepository.save(any())).thenReturn(newComplementaryFormation);

		ComplementaryFormationDTO complementaryFormationDTOSaved = complementaryFormationServiceImpl.updateComplementaryFormation(token, complementaryFormationDTO, volunteer.getId());

		assertNotNull(complementaryFormationDTOSaved);
	}
	
	@Test
	public void updateComplementaryFormationVolunteerOfOtherONG_negative() throws OperationNotAllowedException {
		
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
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
	
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.updateComplementaryFormation(token, complementaryFormationDTO, 1L));
	}
	
	@Test
	public void updateComplementaryFormationBeneficiaryOfOtherONG_negative() throws OperationNotAllowedException {
		
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
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
    	
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.updateComplementaryFormation(token, complementaryFormationDTO, 1L));
	}
	
	@Test
	public void updateComplementaryFormationWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(beneficiary.getUsername());
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        
        ComplementaryFormationDTO complementaryFormationDTO = createComplementaryFormation();
    	
		assertThrows(OperationNotAllowedException.class, () -> complementaryFormationServiceImpl.updateComplementaryFormation(token, complementaryFormationDTO, 1L));
	}

	@Test
	public void deleteComplementaryFormationOfVolunteerTest() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		complementaryFormationServiceImpl.deleteComplementaryFormation(Long.valueOf(1), token);
	}
	
	@Test
	public void deleteComplementaryFormationOfBeneficiaryTest() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		complementaryFormationServiceImpl.deleteComplementaryFormation(Long.valueOf(1), token);
	}
	
	@Test
	public void deleteComplementaryFormationWithOutBeingONG_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Beneficiary beneficiary = createBeneficiary();
		beneficiary.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(beneficiary.getUsername());
        when(beneficiaryRepository.findById(eq(beneficiary.getId()))).thenReturn(Optional.of(beneficiary));
        	
		assertThrows(OperationNotAllowedException.class, () -> complementaryFormationServiceImpl.deleteComplementaryFormation(1L, token));
	}
	
	@Test
	public void deleteComplementaryFormationThatNotExists_negative() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);

		String token = "Bearertoken";
        when(jwtUtils.getUserNameFromJwtToken(eq(token))).thenReturn(ong.getUsername());
        when(ongRepository.findByUsername(eq(ong.getUsername()))).thenReturn(ong);
        when(volunteerRepository.findById(eq(volunteer.getId()))).thenReturn(Optional.of(volunteer));
        	
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.deleteComplementaryFormation(3L, token));
	}
	
	@Test
	public void deleteComplementaryFormationOfBeneficiaryTest_negative() throws OperationNotAllowedException {
		
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
        
        ComplementaryFormation complementaryFormation = new ComplementaryFormation();
        complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");
	
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.deleteComplementaryFormation(1L, token));
		
	}
	
	@Test
	public void getComplementaryFormationByBeneficiaryTest() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(complementaryFormationRepository.findComplementaryFormationByBeneficiary(beneficiary.getId())).thenReturn(Optional.of(complementaryFormations));

		List<ComplementaryFormationDTO> complementaryFormationsOfBeneficiary = complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(beneficiary.getId(), token);
		assertNotNull(complementaryFormationsOfBeneficiary);
		assertEquals(complementaryFormationsOfBeneficiary.size(), 1);
	}
	
	@Test
	public void getComplementaryFormationOfBeneficiaryWhoNotExistsTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

		String token = "tokenprueba";
		Ong ong = createOng();
		beneficiary.setOng(ong);
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(complementaryFormationRepository.findComplementaryFormationByBeneficiary(beneficiary.getId())).thenReturn(Optional.of(complementaryFormations));

		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(45L, token));

	}
	
	@Test
	public void getComplementaryFormationOfBenificaryOfOtherONG_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

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
		
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("prueba");
		when(ongRepository.findByUsername("prueba")).thenReturn(ong2);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(complementaryFormationRepository.findComplementaryFormationByBeneficiary(beneficiary.getId())).thenReturn(Optional.of(complementaryFormations));

		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(1L, token));

	}
	
	@Test
	public void getComplementaryFormationOfBeneficiaryWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Beneficiary beneficiary = createBeneficiary();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");
		
		String token = "tokenprueba";
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("luis14");
		when(beneficiaryRepository.findByUsername("luis14")).thenReturn(beneficiary);
		when(complementaryFormationRepository.findComplementaryFormationByBeneficiary(beneficiary.getId())).thenReturn(Optional.of(complementaryFormations));

		assertThrows(OperationNotAllowedException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(1L, token));

	}
	
	@Test
	public void getComplementaryFormationByVolunteerTest() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(complementaryFormationRepository.findComplementaryFormationByVolunteer(volunteer.getId())).thenReturn(Optional.of(complementaryFormations));

		List<ComplementaryFormationDTO> complementaryFormationsOfVolunteer = complementaryFormationServiceImpl.getComplementaryFormationByVolunteer(volunteer.getId(), token);
		assertNotNull(complementaryFormationsOfVolunteer);
		assertEquals(complementaryFormationsOfVolunteer.size(), 1);

	}
	
	@Test
	public void getComplementaryFormationOfVolunteerWhoNotExistsTest_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

		String token = "tokenprueba";
		Ong ong = createOng();
		volunteer.setOng(ong);
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(volunteer));
		when(complementaryFormationRepository.findComplementaryFormationByVolunteer(volunteer.getId())).thenReturn(Optional.of(complementaryFormations));

		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(45L, token));

	}
	
	@Test
	public void getComplementaryFormationOfVolunteerOfOtherONG_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer();
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");

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
		
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("prueba");
		when(ongRepository.findByUsername("prueba")).thenReturn(ong2);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(complementaryFormationRepository.findComplementaryFormationByVolunteer(volunteer.getId())).thenReturn(Optional.of(complementaryFormations));

		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(1L, token));

	}
	
	@Test
	public void getComplementaryFormationOfVolunteerWithOutBeingONGTest_negative() throws OperationNotAllowedException {
		
		Volunteer volunteer = createVolunteer(); 
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");


		String token = "tokenprueba";
		List<ComplementaryFormation> complementaryFormations = List.of(complementaryFormation);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("luis14");
		when(volunteerRepository.findByUsername("luis14")).thenReturn(volunteer);
		when(complementaryFormationRepository.findComplementaryFormationByVolunteer(volunteer.getId())).thenReturn(Optional.of(complementaryFormations));

		assertThrows(OperationNotAllowedException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(1L, token));

	}
	
	@Test
	public void getComplementaryFormationOfVolunteeryByIDTest() throws OperationNotAllowedException {
		
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
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setVolunteer(volunteer);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		
		volunteer.setOng(ong);
		ComplementaryFormationDTO complementaryFormationDTO = complementaryFormationServiceImpl.getComplementaryFormationById(Long.valueOf(1), token);
		assertNotNull(complementaryFormationDTO);
		assertEquals(complementaryFormationDTO.getId(), 1);
		assertEquals(complementaryFormationDTO.getName(), "ESO");
		assertEquals(complementaryFormationDTO.getOrganization(), "Junta de Andalucía");
		assertEquals(complementaryFormationDTO.getDate(), LocalDate.of(2022, 3, 3));
		assertEquals(complementaryFormationDTO.getPlace(), "Sevilla");

	}
	
	@Test
	public void getComplementaryFormationOfBeneficiaryByIDTest() throws OperationNotAllowedException {
		
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
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		
		beneficiary.setOng(ong);
		ComplementaryFormationDTO complementaryFormationDTO = complementaryFormationServiceImpl.getComplementaryFormationById(Long.valueOf(1), token);
		assertNotNull(complementaryFormationDTO);
		assertEquals(complementaryFormationDTO.getId(), 1);
		assertEquals(complementaryFormationDTO.getName(), "ESO");
		assertEquals(complementaryFormationDTO.getOrganization(), "Junta de Andalucía");
		assertEquals(complementaryFormationDTO.getDate(), LocalDate.of(2022, 3, 3));
		assertEquals(complementaryFormationDTO.getPlace(), "Sevilla");
	}
	
	@Test
	public void getComplementaryFormationThatNotExistsByIDTest_negative() throws OperationNotAllowedException {
		
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
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		
		
		beneficiary.setOng(ong);
		assertThrows(UsernameNotFoundException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationById(45L, token));

	}
	
	@Test
	public void getComplementaryFormationWithOutPermission_negative() throws OperationNotAllowedException {
		
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
		when(ongRepository.findByUsername("luis14")).thenReturn(ong);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(account.getUsername());
		
		ComplementaryFormation complementaryFormation = new ComplementaryFormation();
		complementaryFormation.setBeneficiary(beneficiary);
		complementaryFormation.setId(1L);
		complementaryFormation.setName("ESO");
		complementaryFormation.setOrganization("Junta de Andalucía");
		complementaryFormation.setDate(LocalDate.of(2022, 3, 3));
		complementaryFormation.setPlace("Sevilla");
		when(complementaryFormationRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(complementaryFormation));
		
		
		beneficiary.setOng(ong);
		assertThrows(OperationNotAllowedException.class, () -> complementaryFormationServiceImpl.getComplementaryFormationById(45L, token));

	}

}
