package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.security.service.AccountDetailsServiceImpl;
import com.ispp.heartforchange.service.impl.AccountServiceImpl;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountControllerTest {

	@Mock
	private JwtUtils jwtUtils;
	@Mock
	private AccountDetailsServiceImpl accountDetailsServiceImpl;
	@Mock
	private AccountServiceImpl accountServiceImpl;
	@Mock
	private AccountRepository accountRepository;
	@InjectMocks
	private AccountController accountController;

	@Test
	public void authenticateUserTest() throws OperationNotAllowedException {
		SigninRequestDTO request = new SigninRequestDTO("asdf1234", "asdf2134");
		Account account = new Account(Long.valueOf(1), "asdf@gmail.com", "asdf1234", "asdf1234", RolAccount.VOLUNTEER);
		when(accountRepository.findByUsername(request.getUsername())).thenReturn(account);
		SigninResponseDTO signinResponseDTO = new SigninResponseDTO("Bearertoken", "Refreshtoken");
		when(accountServiceImpl.authenticateUser(request)).thenReturn(signinResponseDTO);

		ResponseEntity<?> res = accountController.authenticateUser(request);
		assertEquals(res, ResponseEntity.ok(signinResponseDTO));
	}

	@Test
	public void authenticateUserTestNeg() throws OperationNotAllowedException {
		SigninRequestDTO request = new SigninRequestDTO("asdf1234", "asdf2134");
		Account account = new Account(Long.valueOf(1), "asdf@gmail.com", "asdf1234", "asdf1234",
				RolAccount.BENEFICIARY);
		when(accountRepository.findByUsername(request.getUsername())).thenReturn(account);
		SigninResponseDTO signinResponseDTO = new SigninResponseDTO("Bearertoken", "Refreshtoken");
		when(accountServiceImpl.authenticateUser(request)).thenReturn(signinResponseDTO);

		ResponseEntity<?> res = accountController.authenticateUser(request);
		assertEquals(res, new ResponseEntity<String>("You don´t have permissions or account doesn´t exists",
				HttpStatus.BAD_REQUEST));
	}

	@Test
	public void getAccountsTest() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("asdf1234");
		UserDetails userDetailsMock = Mockito.mock(UserDetails.class);
		Mockito.when(userDetailsMock.getUsername()).thenReturn("asdf1234");
		Mockito.when(userDetailsMock.getPassword()).thenReturn("asdf1234");
		Mockito.when(userDetailsMock.getAuthorities()).thenReturn(Collections.emptyList());
		when(accountDetailsServiceImpl.loadUserByUsername("asdf1234")).thenReturn(userDetailsMock);
		when(jwtUtils.generateJwtToken(any())).thenReturn("Bearertoken2");
		when(jwtUtils.generateJwtRefreshToken(any())).thenReturn("Refreshtoken2");

		ResponseEntity<?> res = accountController.getAccounts(request);
		assertEquals(res, ResponseEntity.ok(new SigninResponseDTO("Bearertoken2", "Refreshtoken2")));
	}

	@Test
	public void getAccountsTestNeg() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("a");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn("asdf1234");
		UserDetails userDetailsMock = Mockito.mock(UserDetails.class);
		Mockito.when(userDetailsMock.getUsername()).thenReturn("asdf1234");
		Mockito.when(userDetailsMock.getPassword()).thenReturn("asdf1234");
		Mockito.when(userDetailsMock.getAuthorities()).thenReturn(Collections.emptyList());
		when(accountDetailsServiceImpl.loadUserByUsername("asdf1234")).thenReturn(userDetailsMock);
		when(jwtUtils.generateJwtToken(any())).thenReturn("Bearertoken2");
		when(jwtUtils.generateJwtRefreshToken(any())).thenReturn("Refreshtoken2");

		ResponseEntity<?> res = accountController.getAccounts(request);
		assertEquals(res, new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST));
	}

}
