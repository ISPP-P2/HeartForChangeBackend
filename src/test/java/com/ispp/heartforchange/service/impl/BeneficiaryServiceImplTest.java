package com.ispp.heartforchange.service.impl;



import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.BeneficiaryDTO;

import com.ispp.heartforchange.dto.PersonDTO;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Appointment;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.ComplementaryFormation;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AcademicExperienceRepository;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.AppointmentRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ComplementaryFormationRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.WorkExperienceRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeneficiaryServiceImplTest {


	@InjectMocks
	private BeneficiaryServiceImpl beneficiaryService;
	
	@Mock
	private OngServiceImpl ongServiceImpl;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	AcademicExperienceRepository academicExperienceRepository;
	
	@Mock
	WorkExperienceRepository workExperienceRepository;
	
	@Mock
	ComplementaryFormationRepository complementaryFormationRepository;
	
	@Mock
	AppointmentRepository appointmentRepository;
	
	@Mock
	BeneficiaryRepository beneficiaryRepository;
	
	@Mock
	AccountRepository accountRepository;
	
	@Mock
	PasswordEncoder encoder;

	@Mock
	ONGRepository ongRepository;

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
		return ong;
	}

	
	@Test
	public void testInsert() throws OperationNotAllowedException {
		
		Ong ong = createOng(); 
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		PersonDTO personDto = new PersonDTO(person);
		Beneficiary beneficiary = new Beneficiary(personDto, "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		
		String token = "token";
				
		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.save(any())).thenReturn(beneficiary);

		BeneficiaryDTO beneficiarySaved = beneficiaryService.saveBeneficiary(beneficiaryDto, "test");
		assertNotNull(beneficiarySaved);
	}
	
	
	@Test
	public void testGetBeneficiaryById() throws OperationNotAllowedException {
		Ong ong = createOng();
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		PersonDTO personDto = new PersonDTO(person);
		Beneficiary beneficiary = new Beneficiary(personDto, "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		beneficiary.setOng(ong);
		
		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(beneficiaryRepository.findByUsername("test")).thenReturn(beneficiary);

		BeneficiaryDTO beneficiaryDto = beneficiaryService.getBeneficiaryById(Long.valueOf(1), "test");
		
		assertNotNull(beneficiaryDto);

	}
	
	
	@Test
	public void testGetBeneficiaries() throws OperationNotAllowedException {
		Ong ong = createOng();
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		PersonDTO personDto = new PersonDTO(person);
		Beneficiary beneficiary = new Beneficiary(personDto, "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		beneficiary.setOng(ong);
		List<Beneficiary> beneficiaries = List.of(beneficiary);
		
		
		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(beneficiaryRepository.findByUsername("test")).thenReturn(beneficiary);
		when(beneficiaryRepository.findAll()).thenReturn(beneficiaries);
		
		List<BeneficiaryDTO> beneficiariesDto = beneficiaryService.getAllBeneficiaresByOng("test");
		
		assertNotNull(beneficiariesDto);
		
		
		
	}
	
	@Test
	public void testUpdateBeneficiary() throws OperationNotAllowedException{
		Ong ong = createOng();
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		PersonDTO personDto = new PersonDTO(person);
		Beneficiary beneficiary = new Beneficiary(personDto, "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		beneficiary.setOng(ong);

		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(beneficiaryRepository.findByUsername("test")).thenReturn(beneficiary);
		when(beneficiaryRepository.save(any())).thenReturn(beneficiary);

		BeneficiaryDTO beneficiaryUpdated = beneficiaryService.updateBeneficiary(Long.valueOf(1), beneficiaryDto, "test");

		assertNotNull(beneficiaryUpdated);

	}
	
	@Test
	public void deleteBeneficiaryTest() throws OperationNotAllowedException{
		
		Ong ong = createOng();
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		PersonDTO personDto = new PersonDTO(person);
		Beneficiary beneficiary = new Beneficiary(personDto, "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		beneficiary.setOng(ong);

		when(academicExperienceRepository.findAcademicExperienceByBeneficiaryId(Long.valueOf(1))).thenReturn(Optional.of(new ArrayList<AcademicExperience>()));
		when(appointmentRepository.findAppointmentsByBeneficiaryId(Long.valueOf(1))).thenReturn(Optional.of(new ArrayList<Appointment>()));
		when(workExperienceRepository.findWorkExperienceByBeneficiaryId(Long.valueOf(1))).thenReturn(Optional.of(new ArrayList<WorkExperience>()));
		when(complementaryFormationRepository.findComplementaryFormationByBeneficiary(Long.valueOf(1))).thenReturn(Optional.of(new ArrayList<ComplementaryFormation>()));

		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(beneficiary));
		when(beneficiaryRepository.findByUsername("test")).thenReturn(beneficiary);
		
		beneficiaryService.deteleBeneficiary(Long.valueOf(1), "test");
	}
	
	
	@Test
	public void numberBeneficiaryTest() throws OperationNotAllowedException{
		
		Ong ong = createOng();
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		PersonDTO personDto = new PersonDTO(person);
		Beneficiary beneficiary = new Beneficiary(personDto, "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		beneficiary.setOng(ong);
		List<Beneficiary> beneficiaries = List.of(beneficiary);

		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findAll()).thenReturn(beneficiaries);
		when(beneficiaryRepository.findByUsername("test")).thenReturn(beneficiary);
		
		Integer number = beneficiaryService.getNumberBeneficiaresByOng("test");
		
		assertNotNull(number);
	}
	
	
}
