package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.PersonDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.dto.UpdatePasswordDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.ComplementaryFormation;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AcademicExperienceRepository;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.AttendanceRepository;
import com.ispp.heartforchange.repository.ComplementaryFormationRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.repository.WorkExperienceRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VolunteerServiceImplTest {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@InjectMocks
	private VolunteerServiceImpl volunteerService;
	
	@InjectMocks
	private OngServiceImpl ongServiceImpl;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private SigninResponseDTO signinResponseDTO;
	
	@Mock
	private ONGRepository ongRepository; 
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private AcademicExperienceRepository academicExperienceRepository;
	

	@Mock
	private AttendanceRepository attendanceRepository;
	
	@Mock
	private WorkExperienceRepository workExperienceRepository;
	
	@Mock
	private ComplementaryFormationRepository complementaryFormationRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private VolunteerRepository volunteerRepository;
	
	@Mock
	private AccountServiceImpl accountServiceImpl; 
	
	@Mock
	private PasswordEncoder encoder;

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
	public void testInsertVolunteer() throws OperationNotAllowedException {
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		String token = "tokenprueba";
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.save(any())).thenReturn(volunteer);
		
		VolunteerDTO volunteerSave = volunteerService.saveVolunteer(volunteerDTO, token);
		assertNotNull(volunteerSave);
	} 
	
	
	@Test
	public void getVolunteerByOngTest() throws OperationNotAllowedException{
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario2");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		
		String token = "tokenprueba";
		volunteer.setOng(ong);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(volunteerRepository.findAll()).thenReturn(java.util.Arrays.asList(volunteer));
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		
		List<VolunteerDTO> people = volunteerService.getVolunteersByOng("test");
		assertEquals(1, people.size());
		
		when(volunteerRepository.findAll()).thenReturn(new ArrayList<>());
		people = volunteerService.getVolunteersByOng("test");
		assertTrue(people.isEmpty());
	}
	

	
	@Test
	public void getVolunteerByIdTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario2");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		String token = "tokenprueba";
		volunteer.setOng(ong);
		
		
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		
		VolunteerDTO volunteerDTO = volunteerService.getVolunteerById(Long.valueOf(1), "test");
		assertNotNull(volunteerDTO);
	}
	
	@Test
	public void getVolunteerByIdWithoutPermissionTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario2");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		String token = "tokenprueba";
		volunteer.setOng(ong);
		ong.setRolAccount(RolAccount.BENEFICIARY);
		
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		
	
		assertThrows(UsernameNotFoundException.class,() -> volunteerService.getVolunteerById(Long.valueOf(0), "test"));

	}
	
    @Test()
    public void testGetVolunteerByIdPermissionDenied() throws OperationNotAllowedException {
    	Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario2");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
        
		Account account = new Account(volunteer.getId(), volunteer.getEmail(), volunteer.getUsername(), volunteer.getPassword(), volunteer.getRolAccount());

		
        when(volunteerRepository.findById(volunteer.getId())).thenReturn(Optional.of(volunteer));
        when(accountRepository.findByUsername(volunteer.getUsername())).thenReturn(account);
        Mockito.when(volunteerRepository.findByUsername(volunteer.getUsername())).thenReturn(new Volunteer());
        
		assertThrows(UsernameNotFoundException.class,() -> volunteerService.getVolunteerById(volunteer.getId(), volunteer.getUsername()));
    }
	
	@Test
	public void updateVolunteerTest() throws OperationNotAllowedException{
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		Volunteer newVolunteer = volunteer;
		newVolunteer.setSexCrimes(true);
		VolunteerDTO volunteerDTO = new VolunteerDTO(volunteer, "10:00 y 11:00", false);
		volunteerDTO.setSexCrimes(true);
		volunteer.setOng(ong);
		
		String token = "tokenprueba";
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(volunteerRepository.save(any())).thenReturn(newVolunteer);
		when(accountRepository.findByUsername("test")).thenReturn(account);

		
		VolunteerDTO volunteerDTO2 = volunteerService.updateVolunteer( Long.valueOf(1), volunteerDTO, "test");
		assertNotNull(volunteerDTO2);
	}
	
	@Test
	public void getNumberVolunteerByOngTest() throws OperationNotAllowedException{
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario2");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		
		String token = "tokenprueba";
		volunteer.setOng(ong);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(volunteerRepository.findAll()).thenReturn(java.util.Arrays.asList(volunteer));
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		
		Integer people = volunteerService.getNumberOfVolunteersByOng("test");
		assertEquals(1, people);
		
		when(volunteerRepository.findAll()).thenReturn(new ArrayList<>());
		people = volunteerService.getNumberOfVolunteersByOng("test");
		assertTrue(people == 0);
	}
	
	
	
	@Test
	public void updatePasswordVolunteerTest() throws OperationNotAllowedException{
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario2");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
		updatePasswordDTO.setPassword("nueva");
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, null, null);
		
		String token = "tokenprueba";
		volunteer.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(volunteer.getId())).thenReturn(Optional.of(volunteer));
		when(passwordEncoder.encode(anyString())).thenReturn("password");
		when(volunteerRepository.save(any())).thenReturn(volunteer);

		
		VolunteerDTO volunteerDTO2 = volunteerService.updateVolunteerPassword(Long.valueOf(0), updatePasswordDTO, token);
		assertNotNull(volunteerDTO2);
		assertEquals(volunteer.getId(), volunteerDTO2.getId());
		System.out.println(volunteerDTO2);
		//assertEquals("password", volunteer.getPassword());
	}
	
	
	@Test
	public void updateVolunteerWithBeneficiaryTest() throws OperationNotAllowedException{
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		Volunteer newVolunteer = volunteer;
		newVolunteer.setSexCrimes(true);
		VolunteerDTO volunteerDTO = new VolunteerDTO(volunteer, "10:00 y 11:00", false);
		volunteerDTO.setSexCrimes(true);
		volunteer.setOng(ong);
		ong.setRolAccount(RolAccount.BENEFICIARY);
		String token = "tokenprueba";
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(volunteerRepository.save(any())).thenReturn(newVolunteer);
		when(accountRepository.findByUsername("test")).thenReturn(account);

		assertThrows(UsernameNotFoundException.class,() -> volunteerService.updateVolunteer(Long.valueOf(1), volunteerDTO, "test"));
	}
	
	@Test
	public void updateOtherVolunteerTest() throws OperationNotAllowedException{
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario2");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		Volunteer newVolunteer = volunteer;
		newVolunteer.setSexCrimes(true);
		VolunteerDTO volunteerDTO = new VolunteerDTO(volunteer, "10:00 y 11:00", false);
		volunteerDTO.setSexCrimes(true);
		volunteer.setOng(ong);
		String token = "tokenprueba";
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());

		Person person2 = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto2 = new PersonDTO(person2);
		Volunteer volunteer2 = new Volunteer(personDto2, "10:00 y 11:00", false);
		volunteer.setEmail("mario@hotmail.com");
		volunteer.setUsername("mario233");
		volunteer.setPassword("asdasd");
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer2));
		when(volunteerRepository.save(any())).thenReturn(newVolunteer);
		when(accountRepository.findByUsername("test")).thenReturn(account);

		assertThrows(UsernameNotFoundException.class,() -> volunteerService.updateVolunteer(Long.valueOf(1), volunteerDTO, "test"));
	}
	
	@Test
	public void deleteVolunteerTest() throws OperationNotAllowedException {
		Ong ong = createOng();
        Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
		PersonDTO personDto = new PersonDTO(person);
		Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setOng(ong);
		
		AcademicExperience ae = new AcademicExperience(Long.valueOf(0), "Degree 1", 2010, 2014, "Universidad", null, volunteer);
		WorkExperience we = new WorkExperience(Long.valueOf(1), "Trabajo", "10:30", "Sevilla", "Ninguna", volunteer, null);
		ComplementaryFormation cf = new ComplementaryFormation(Long.valueOf(1), "Nombre", "Organiacion", LocalDate.of(2012, 01, 01), "Sevilla", null, volunteer);
		Attendance a = new Attendance();
		when(academicExperienceRepository.findAcademicExperienceByVolunteerId(Long.valueOf(1))).thenReturn(Optional.of(java.util.Arrays.asList(ae)));
		when(workExperienceRepository.findWorkExperienceByVolunteerId(Long.valueOf(1))).thenReturn(Optional.of(java.util.Arrays.asList(we)));
		when(complementaryFormationRepository.findComplementaryFormationByVolunteer(Long.valueOf(1))).thenReturn(Optional.of(java.util.Arrays.asList(cf)));
		when(accountRepository.findByUsername("test")).thenReturn(account);
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(volunteerRepository.findByUsername("test")).thenReturn(volunteer);
		when(attendanceRepository.findByPersonId(Long.valueOf(0))).thenReturn(Optional.of(a));
		volunteerService.deleteVolunteer(Long.valueOf(1), "test");
	}
	
}
