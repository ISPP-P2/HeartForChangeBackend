package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ispp.heartforchange.dto.GrantDTO;
import com.ispp.heartforchange.entity.Grant;
import com.ispp.heartforchange.entity.GrantState;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.GrantServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GrantControllerTest {

	@Mock
	private GrantServiceImpl grantService;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private GrantController grantController;

	@Test
	public void testInsert() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.saveGrant(grantDTO, "oken")).thenReturn(grantDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = grantController.saveGrant(grantDTO, request);
		assertEquals(res, ResponseEntity.ok(grantDTO));
	}
	
	@Test
	public void testInsertNeg() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.saveGrant(grantDTO, "oken")).thenReturn(grantDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn(null);

		ResponseEntity<?> res = grantController.saveGrant(grantDTO, request);
		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void testInsertNeg2() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(grantService.saveGrant(grantDTO, "oken")).thenReturn(grantDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = grantController.saveGrant(grantDTO, request);
		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void updateTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.updateGrant("oken", grantDTO, Long.valueOf(1))).thenReturn(grantDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = grantController.updateGrant(grantDTO, request, Long.valueOf(1));
		assertEquals(res, ResponseEntity.ok(grantDTO));
	}
	
	@Test
	public void updateTestNeg() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.updateGrant("oken", grantDTO, Long.valueOf(1))).thenReturn(grantDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn(null);

		ResponseEntity<?> res = grantController.updateGrant(grantDTO, request, Long.valueOf(1));
		assertEquals(res,  new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void updateTestNeg2() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(grantService.updateGrant("oken", grantDTO, Long.valueOf(1))).thenReturn(grantDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = grantController.updateGrant(grantDTO, request, Long.valueOf(1));
		assertEquals(res,  new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}

	@Test
	public void getGrantsByOngTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);
		List<GrantDTO> grants = List.of(grantDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.getGrantsByOng("oken")).thenReturn(grants);

		ResponseEntity<?> res = grantController.getGrantsByOng(request);

		assertEquals(res, ResponseEntity.ok(grants));
	}
	
	@Test
	public void getGrantsByOngTestNeg() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);
		List<GrantDTO> grants = List.of(grantDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn(null);
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.getGrantsByOng("oken")).thenReturn(grants);

		ResponseEntity<?> res = grantController.getGrantsByOng(request);

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void getGrantsByOngTestNeg2() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);
		List<GrantDTO> grants = List.of(grantDTO);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(grantService.getGrantsByOng("oken")).thenReturn(grants);

		ResponseEntity<?> res = grantController.getGrantsByOng(request);

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void getTotalAmountAcceptedGrantsByOngTest() throws OperationNotAllowedException {

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.getTotalAmountAcceptedGrantsByOng("oken")).thenReturn(200.);

		ResponseEntity<?> res = grantController.getTotalAmountAcceptedGrantsByOng(request);

		assertEquals(res, ResponseEntity.ok(200.));
	}
	
	@Test
	public void getTotalAmountAcceptedGrantsByOngTestNeg() throws OperationNotAllowedException {

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn(null);
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.getTotalAmountAcceptedGrantsByOng("oken")).thenReturn(200.);

		ResponseEntity<?> res = grantController.getTotalAmountAcceptedGrantsByOng(request);

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void getTotalAmountAcceptedGrantsByOngTestNeg2() throws OperationNotAllowedException {

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(grantService.getTotalAmountAcceptedGrantsByOng("oken")).thenReturn(200.);

		ResponseEntity<?> res = grantController.getTotalAmountAcceptedGrantsByOng(request);

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}

	@Test
	public void getGrantsByIdTest() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.getGrantById(Long.valueOf(1), "oken")).thenReturn(grantDTO);

		ResponseEntity<?> res = grantController.getGrantById(request, Long.valueOf(1));

		assertEquals(res, ResponseEntity.ok(grantDTO));
	}
	
	@Test
	public void getGrantsByIdTestNeg() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn(null);
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(grantService.getGrantById(Long.valueOf(1), "oken")).thenReturn(grantDTO);

		ResponseEntity<?> res = grantController.getGrantById(request, Long.valueOf(1));

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void getGrantsByIdTestNeg2() throws OperationNotAllowedException {
		Grant grant = new Grant(Long.valueOf(0), true, true, GrantState.REFORMULATION, "justification", 200., null);
		GrantDTO grantDTO = new GrantDTO(grant);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(grantService.getGrantById(Long.valueOf(1), "oken")).thenReturn(grantDTO);

		ResponseEntity<?> res = grantController.getGrantById(request, Long.valueOf(1));

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void deleteGrantTest() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);

		ResponseEntity<?> res = grantController.deleteGrant( Long.valueOf(1), request);

		assertEquals(res, ResponseEntity.ok("Grant deleted"));
	}
	
	@Test
	public void deleteGrantTestNeg() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn(null);
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);

		ResponseEntity<?> res = grantController.deleteGrant( Long.valueOf(1), request);

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void deleteGrantTestNeg2() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);

		ResponseEntity<?> res = grantController.deleteGrant( Long.valueOf(1), request);

		assertEquals(res, new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST));
	}
}
