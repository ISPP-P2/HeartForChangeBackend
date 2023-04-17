package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AppointmentRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OngServiceImplTest {
	
	@InjectMocks
	private OngServiceImpl ongServiceImpl;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private SigninResponseDTO signinResponseDTO;
	
	@Mock
	private ONGRepository ongRepository; 
	
	@Mock
	private VolunteerRepository volunteerRepository; 
	
	@Mock
	private BeneficiaryRepository beneficiaryRepository; 
	
	@Mock
	private AppointmentRepository appointmentRepository;
	
	@Mock
	private AccountServiceImpl accountServiceImpl; 
	
	@Mock
	private PasswordEncoder encoder;

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
	public void saveOngPositiveTest() throws OperationNotAllowedException {

		OngDTO ongDTO = new OngDTO(createValidOng());

		when(ongRepository.save(Mockito.any(Ong.class))).thenAnswer(invocation -> {
		    Ong ong = invocation.getArgument(0);
		    ong.setId(1L);
		    return ong;
		});
		
		String token = "AdminToken";
		Mockito.when(signinResponseDTO.getToken()).thenReturn("AdminToken");
		Mockito.when(jwtUtils.getUserNameFromJwtToken("AdminToken")).thenReturn("admin");

		OngDTO result = ongServiceImpl.saveOng(ongDTO, token);

		assertNotNull(result);
		assertEquals(1L, result.getId().longValue());
		assertEquals("Unicef España", result.getName());
		assertEquals("D44577716", result.getCif());
		assertEquals("Ayudamos a los niños del mundo", result.getDescription());
		assertEquals("unicef-esp@gmail.com", result.getEmail());
		assertEquals(encoder.encode("asdf1234"), result.getPassword());
		assertEquals(RolAccount.ONG, result.getRolAccount());
		assertEquals("unicef-esp", result.getUsername());
		verify(ongRepository, times(1)).save(any(Ong.class));
	}

	@Test
	public void saveOngNegativeTest() throws OperationNotAllowedException {

	    OngDTO ongDTO = new OngDTO(createInvalidOng()); 

		String token = "AdminToken";
		Mockito.when(signinResponseDTO.getToken()).thenReturn("AdminToken");
		Mockito.when(jwtUtils.getUserNameFromJwtToken("AdminToken")).thenReturn("admin");
		
	    assertThrows(IllegalArgumentException.class, () -> ongServiceImpl.saveOng(ongDTO, token));
	}
	
	@Test
	public void getOngPositiveTest() throws OperationNotAllowedException {

		Ong ong = createValidOng();		
	    
		Mockito.when(signinResponseDTO.getToken()).thenReturn("Token");
		Mockito.when(jwtUtils.getUserNameFromJwtToken("Token")).thenReturn("unicef-esp");
		Mockito.when(ongRepository.findByUsername("unicef-esp")).thenReturn(ong);
			
		OngDTO ongDTOgot;
		ongDTOgot = ongServiceImpl.getOng("Token");
		assertNotNull(ongDTOgot);
		assertEquals(new OngDTO(ong), ongDTOgot);	
		
	}
	
	@Test 
	public void getOngNegativeTest() throws OperationNotAllowedException {			
		assertThrows(OperationNotAllowedException.class, () -> ongServiceImpl.getOng("InvalidToken"));
	}
	
	@Test
	public void updateOngPositiveTest() throws OperationNotAllowedException {
		Ong oldOng = createValidOng();
		Ong newOng = createValidOng();
		newOng.setDescription("Ayudamos a los niños más necesitados del mundo");
		OngDTO newOngDto = new OngDTO(newOng);

		String token = "Token";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("unicef-esp");
		when(ongRepository.findByUsername("unicef-esp")).thenReturn(oldOng);
		when(ongRepository.save(any())).thenReturn(newOng);
		
		OngDTO updatedDTO = ongServiceImpl.updateOng(token, newOngDto);

		assertNotNull(updatedDTO);
		assertEquals(updatedDTO.getDescription(),"Ayudamos a los niños más necesitados del mundo");
	}
	
	@Test
	public void updateOngNegativeTest() throws OperationNotAllowedException {
		//Not logged ong
		Ong newOng = createValidOng();
		newOng.setDescription("Ayudamos a los niños más necesitados del mundo");
		OngDTO newOngDto = new OngDTO(newOng);

		String token = "InvalidToken";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(null);
		
		assertThrows(OperationNotAllowedException.class, () -> ongServiceImpl.updateOng(token, newOngDto));
	}
	
	@Test
	public void updateOngNegativeTest2() throws OperationNotAllowedException {
		//Invalid ong input
		Ong oldOng = createValidOng();
		Ong newOng = createInvalidOng();
		OngDTO newOngDto = new OngDTO(newOng);

		String token = "Token";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("unicef-esp");
		when(ongRepository.findByUsername("unicef-esp")).thenReturn(oldOng);
		
		assertThrows(IllegalArgumentException.class, () -> ongServiceImpl.updateOng(token, newOngDto));
	}
	
	@Test
	public void deleteOngPositiveTest() throws OperationNotAllowedException {
		Ong ong = createValidOng();

		String token = "Token";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("unicef-esp");
		when(ongRepository.findByUsername("unicef-esp")).thenReturn(ong);
		when(volunteerRepository.findVolunteersByOng("unicef-esp")).thenReturn(new ArrayList<>());
		when(beneficiaryRepository.findBeneficiariesByOng("unicef-esp")).thenReturn(new ArrayList<>());
		when(appointmentRepository.findByOng(ong)).thenReturn(new ArrayList<>());

		ongServiceImpl.deleteOng(token);
	}
	
	@Test
	public void deleteOngNegativeTest() throws OperationNotAllowedException {
		//Not logged ong
		String token = "Token";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(null);
	
		assertThrows(OperationNotAllowedException.class, () -> ongServiceImpl.deleteOng(token));
	}
	
	
}
