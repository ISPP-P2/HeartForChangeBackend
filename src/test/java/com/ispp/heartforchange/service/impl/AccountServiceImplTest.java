package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;

import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountServiceImplTest {

	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	@Test
	public void authenticateUserTest() throws OperationNotAllowedException {
		SigninRequestDTO signinRequestDTO = new SigninRequestDTO("asdf1234", "asdf1234");
		when(jwtUtils.generateJwtToken(any())).thenReturn("Bearertoken");
		when(jwtUtils.generateJwtRefreshToken(any())).thenReturn("Refreshtoken");
		SigninResponseDTO res = accountServiceImpl.authenticateUser(signinRequestDTO);
		assertEquals(res, new SigninResponseDTO("Bearertoken", "Refreshtoken"));
	}
}
