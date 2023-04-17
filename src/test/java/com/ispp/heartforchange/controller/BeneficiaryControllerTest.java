package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.PersonDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.BeneficiaryServiceImpl;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BeneficiaryControllerTest {
	
	@Mock
	private BeneficiaryServiceImpl beneficiaryServiceImpl;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@InjectMocks
	private BeneficiaryController beneficiaryController;
	
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
		
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		person.setOng(ong);
		person.setUsername("Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(beneficiaryServiceImpl.saveBeneficiary(beneficiaryDto, "test")).thenReturn(beneficiaryDto);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("test");
 

		ResponseEntity<?> res = beneficiaryController.saveBeneficiary(request, beneficiaryDto);
		assertEquals(res, ResponseEntity.ok(beneficiaryDto));	
		
	}
	
	@Test
	public void testNumberOfBeneficiary() throws OperationNotAllowedException {
		Ong ong = createOng();
		
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		person.setOng(ong);
		person.setUsername("Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		List<BeneficiaryDTO> beneficiaries = List.of(beneficiaryDto);
		
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(beneficiaryServiceImpl.getAllBeneficiaresByOng("test")).thenReturn(beneficiaries);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("test");
 

		ResponseEntity<?> res = beneficiaryController.getBeneficiariesByOng(request);
		assertEquals(res, ResponseEntity.ok(beneficiaries));	
		
	}
	
	@Test
	public void testGetBeneficiaryById() throws OperationNotAllowedException {
		Ong ong = createOng();
		
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		person.setOng(ong);
		person.setUsername("Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(beneficiaryServiceImpl.getBeneficiaryById(Long.valueOf(1), "test")).thenReturn(beneficiaryDto);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("test");
 

		ResponseEntity<?> res = beneficiaryController.getBeneficiaryById(Long.valueOf(1), request);
		assertEquals(res, ResponseEntity.ok(beneficiaryDto));	
		
	}
	
	@Test
	public void testNumberBeneficiaries() throws OperationNotAllowedException {
		Ong ong = createOng();
		
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		person.setOng(ong);
		person.setUsername("Prueba");
		
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(beneficiaryServiceImpl.getNumberBeneficiaresByOng("test")).thenReturn(1);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("test");
 

		ResponseEntity<?> res = beneficiaryController.getNumberBneneficiariesByOng(request);
		assertEquals(res, ResponseEntity.ok(1));	
		
	}
	
	
	@Test
	public void testGetBeneficiaryByOng() throws OperationNotAllowedException {
		Ong ong = createOng();
		
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		person.setOng(ong);
		person.setUsername("Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		List<BeneficiaryDTO> beneficiaries = List.of(beneficiaryDto);

		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(beneficiaryServiceImpl.getAllBeneficiaresByOng("test")).thenReturn(beneficiaries);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("test");
 

		ResponseEntity<?> res = beneficiaryController.getBeneficiariesByOng( request);
		assertEquals(res, ResponseEntity.ok(beneficiaries));	
		
	}
	
	@Test
	public void testUpdateBeneficiary() throws OperationNotAllowedException {
		Ong ong = createOng();
		
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		person.setOng(ong);
		person.setUsername("Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");

		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(beneficiaryServiceImpl.updateBeneficiary(Long.valueOf(1), beneficiaryDto,"test")).thenReturn(beneficiaryDto);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("test");
 

		ResponseEntity<?> res = beneficiaryController.updateBeneficiary( request, Long.valueOf(1), beneficiaryDto);
		assertEquals(res, ResponseEntity.ok(beneficiaryDto));	
		
	}
	
	@Test
	public void testDeleteBeneficiaries() throws OperationNotAllowedException {
		Ong ong = createOng();
		
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		person.setOng(ong);
		person.setUsername("Prueba");
		
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("test");
 

		ResponseEntity<?> res = beneficiaryController.deleteBeneficiary(request, Long.valueOf(1));
		assertEquals(res, ResponseEntity.ok("Beneficiary deleted"));	
		
	}
	
	
	
}

