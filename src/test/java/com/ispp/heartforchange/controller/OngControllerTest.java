package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;

import com.ispp.heartforchange.dto.GrantDTO;
import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.entity.Grant;
import com.ispp.heartforchange.entity.GrantState;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.OngServiceImpl;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OngControllerTest {

	@Mock
	private OngServiceImpl ongServiceImpl;
	
	@Mock
	private ONGRepository ongRepository;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private ONGController ongController;
	
	public Ong createValidOng() throws OperationNotAllowedException {
		Ong ong = new Ong();
		ong.setCif("D44577716");
		ong.setDescription("Ayudamos a los niños del mundo");
		ong.setEmail("unicef-esp@gmail.com");
		ong.setId(Long.valueOf(0));
		ong.setName("Unicef España");
		ong.setPassword("asdf1234");
		ong.setRolAccount(RolAccount.ONG);
		ong.setUsername("unicef-esp");
		ong.setGrants(new ArrayList<>());
		ong.setListAppointments(new ArrayList<>());
		ong.setTasks(new ArrayList<>());
		ong.setPeople(new ArrayList<>());
		ong.setPerson(new ArrayList<>());
		return ong;
	}
	
	public Ong createInvalidOng() throws OperationNotAllowedException {
		Ong ong = new Ong();
		ong.setCif("ASD123"); // Invalid CIF
		ong.setDescription("Ayudamos a los niños del mundo");
		ong.setEmail("unicef-esp@gmail.com");
		ong.setId(Long.valueOf(0));
		ong.setName("Unicef España");
		ong.setPassword("asdf1234");
		ong.setRolAccount(RolAccount.ONG);
		ong.setUsername("unicef-esp");
		return ong;
	}
	
	@Test
	public void saveOngTest() throws OperationNotAllowedException {
		Ong ong = createValidOng();
		OngDTO ongDTO = new OngDTO(ong);
		
		when(ongServiceImpl.saveOng(ongDTO)).thenReturn(ongDTO);

		ResponseEntity<?> res = ongController.saveOng(ongDTO);
		assertEquals(res, ResponseEntity.ok(ongDTO));
	}
	
	@Test
	public void getOngTest() throws OperationNotAllowedException {
		Ong ong = createValidOng();
		OngDTO ongDTO = new OngDTO(ong);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(ongServiceImpl.getOng("oken")).thenReturn(ongDTO);

		ResponseEntity<?> res = ongController.getOng(request);

		assertEquals(res, ResponseEntity.ok(ongDTO));
	}
	
	/*@Test
	public void updateOngTest() throws OperationNotAllowedException {
		Ong newOng = createValidOng();
		newOng.setDescription("Descripción actualizada");
		OngDTO newOngDTO = new OngDTO(newOng);

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(ongServiceImpl.updateOng("oken", newOngDTO)).thenReturn(newOngDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.getUserNameFromJwtToken("oken")).thenReturn(newOng.getUsername());

		ResponseEntity<?> res = ongController.updateOng(request, newOngDTO);
		assertEquals(res, ResponseEntity.ok(newOngDTO));
	}*/
	
	@Test
	public void deleteOngTest() throws OperationNotAllowedException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);

		ResponseEntity<?> res = ongController.deleteOng(request);

		assertEquals(res, ResponseEntity.ok("ONG deleted"));
	}
	
	
}
