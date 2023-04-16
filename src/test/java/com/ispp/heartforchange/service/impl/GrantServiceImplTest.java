package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.GrantDTO;
import com.ispp.heartforchange.entity.Grant;
import com.ispp.heartforchange.entity.GrantState;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.GrantRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GrantServiceImplTest {

	@InjectMocks
	private GrantServiceImpl grantService;

	@Mock
	private GrantRepository grantRepository;

	@Mock
	private OngServiceImpl ongServiceImpl;

	@Mock
	private JwtUtils jwtUtils;

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
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);
		String token = "tokenprueba";
		Ong ong = createOng();

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(grantRepository.save(any())).thenReturn(grant);

		GrantDTO grantSaved = grantService.saveGrant(grantDTO, token);
		assertNotNull(grantSaved);
	}

	@Test
	public void getGrantsByOngTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);

		String token = "tokenprueba";
		Ong ong = createOng();
		grant.setOng(ong);
		List<Grant> grants = List.of(grant);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(grantRepository.findByOng(ong)).thenReturn(grants);

		List<GrantDTO> grantsS = grantService.getGrantsByOng(token);
		assertNotNull(grantsS);
	}

	@Test
	public void getTotalAmountAcceptedGrantsByOngTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.ACCEPTED, "justification", 200., null);

		String token = "tokenprueba";
		Ong ong = createOng();
		grant.setOng(ong);
		List<Grant> grants = List.of(grant);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(grantRepository.findByOng(ong)).thenReturn(grants);

		Double amount = grantService.getTotalAmountAcceptedGrantsByOng(token);

		assertEquals(200., amount);
	}

	@Test
	public void getGrantByIdTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.ACCEPTED, "justification", 200., null);

		String token = "tokenprueba";
		Ong ong = createOng();
		grant.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(grantRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(grant));

		GrantDTO grantDTO = grantService.getGrantById(Long.valueOf(1), token);

		assertNotNull(grantDTO);
	}

	@Test
	public void updateGrantTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.ACCEPTED, "justification", 200., null);
		Grant newGrant = grant;
		newGrant.setAmount(500.);
		GrantDTO grantDto = new GrantDTO(grant);
		grantDto.setAmount(500.);

		String token = "tokenprueba";
		Ong ong = createOng();
		grant.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(grantRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(grant));
		when(grantRepository.save(any())).thenReturn(newGrant);

		GrantDTO grantDTO = grantService.updateGrant(token, grantDto, Long.valueOf(1));

		assertNotNull(grantDTO);
	}

	@Test
	public void deleteGrantTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.ACCEPTED, "justification", 200., null);

		String token = "tokenprueba";
		Ong ong = createOng();
		grant.setOng(ong);

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(grantRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(grant));
		grantService.deleteGrant(Long.valueOf(1), token);
	}
}
