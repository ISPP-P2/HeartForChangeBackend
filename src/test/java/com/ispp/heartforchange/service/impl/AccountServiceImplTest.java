package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;

import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
