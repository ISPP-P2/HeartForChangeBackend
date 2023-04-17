package com.ispp.heartforchange.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
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


import com.ispp.heartforchange.dto.UpdatePasswordDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.VolunteerServiceImpl;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VolunteerControllerTest {


	@Mock
	private VolunteerServiceImpl volunteerService;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@InjectMocks
	private VolunteerController volunteerController;
	
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
	public void insertVolunteerTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(volunteerService.saveVolunteer(volunteerDTO, "oken")).thenReturn(volunteerDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		ResponseEntity<?> res = volunteerController.saveVolunteer(request, volunteerDTO);
		assertEquals(res, ResponseEntity.ok(volunteerDTO));
	}
	
	@Test
	public void insertVolunteerNoTokenTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(volunteerService.saveVolunteer(volunteerDTO, "oken")).thenReturn(volunteerDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		ResponseEntity<?> res = volunteerController.saveVolunteer(request, volunteerDTO);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
	}
	
	
	@Test
	public void updateVolunteerTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(volunteerService.updateVolunteer(Long.valueOf(1), volunteerDTO, "test")).thenReturn(volunteerDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		ResponseEntity<?> res = volunteerController.updateVolunteer(request, Long.valueOf(1), volunteerDTO);
		System.out.println(res);
		System.out.println(ResponseEntity.ok(volunteerDTO));
		assertEquals(HttpStatus.OK, res.getStatusCode());

	}
	
	@Test
	public void updateNegativeVolunteerTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());

		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(volunteerService.updateVolunteer(Long.valueOf(1), volunteerDTO, "test")).thenReturn(volunteerDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		ResponseEntity<?> res = volunteerController.updateVolunteer(request, Long.valueOf(1), volunteerDTO);
		System.out.println(res);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void updateVolunteerPasswordTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
		updatePasswordDTO.setPassword("nueva");
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(volunteerService.updateVolunteerPassword(Long.valueOf(1), updatePasswordDTO, "oken")).thenReturn(volunteerDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		ResponseEntity<?> res = volunteerController.updateVolunteerPassword(request, Long.valueOf(1), updatePasswordDTO);
		System.out.println(res);
		System.out.println(ResponseEntity.ok(volunteerDTO));
		assertEquals(HttpStatus.OK, res.getStatusCode());
	}
	
	@Test
	public void updateNegativeVolunteerPasswordTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
		updatePasswordDTO.setPassword("nueva");
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(volunteerService.updateVolunteerPassword(Long.valueOf(1), updatePasswordDTO, "oken")).thenReturn(volunteerDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		ResponseEntity<?> res = volunteerController.updateVolunteerPassword(request, Long.valueOf(1), updatePasswordDTO);
		System.out.println(res);
		System.out.println(ResponseEntity.ok(volunteerDTO));
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteVolunteerTest() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		
		ResponseEntity<?> res = volunteerController.deleteVolunteer(request, Long.valueOf(1));
		assertEquals(res, ResponseEntity.ok("Volunteer deleted"));
	}
	
	@Test
	public void deleteNegativeVolunteerTest() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		
		ResponseEntity<?> res = volunteerController.deleteVolunteer(request, Long.valueOf(1));
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void getVolunteerByIdTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(volunteerService.getVolunteerById(Long.valueOf(1), "test")).thenReturn(volunteerDTO);
		
		ResponseEntity<?> res = volunteerController.getVolunteerById(request, Long.valueOf(1));
		assertEquals(HttpStatus.OK, res.getStatusCode());
	}
	
	@Test
	public void getNegativeVolunteerByIdTest() throws OperationNotAllowedException {
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(volunteerService.getVolunteerById(Long.valueOf(1), "test")).thenReturn(volunteerDTO);
		
		ResponseEntity<?> res = volunteerController.getVolunteerById(request, Long.valueOf(1));
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getVolunteerByOngTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		person.setOng(ong);
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		List<VolunteerDTO> volunteers = List.of(volunteerDTO);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(volunteerService.getVolunteersByOng("test")).thenReturn(volunteers);
	
		ResponseEntity<?> res = volunteerController.getVolunteersByOng(request);
		assertEquals(HttpStatus.OK, res.getStatusCode());
	}
	
	@Test
	public void getNegativeVolunteerByOngTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		person.setOng(ong);
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		List<VolunteerDTO> volunteers = List.of(volunteerDTO);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(volunteerService.getVolunteersByOng("test")).thenReturn(volunteers);
	
		ResponseEntity<?> res = volunteerController.getVolunteersByOng(request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getNumberOfVolunteerByOngTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		person.setOng(ong);
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		Integer volunteers = List.of(volunteerDTO).size();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(volunteerService.getNumberOfVolunteersByOng("test")).thenReturn(volunteers);
	
		ResponseEntity<?> res = volunteerController.getNumberOfVolunteersByOng(request);
		assertEquals(HttpStatus.OK, res.getStatusCode());
	}
	
	@Test
	public void getNegativeNumberOfVolunteerByOngTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", null, new ArrayList<Attendance>());
		person.setOng(ong);
		VolunteerDTO volunteerDTO = new VolunteerDTO(person, "10:00 y 11:00", false);
		Integer volunteers = List.of(volunteerDTO).size();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(volunteerService.getNumberOfVolunteersByOng("test")).thenReturn(volunteers);
	
		ResponseEntity<?> res = volunteerController.getNumberOfVolunteersByOng(request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
}
