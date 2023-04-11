package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OngServiceImplTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@InjectMocks
	private OngServiceImpl ongServiceImpl;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private SigninResponseDTO signinResponseDTO;
	
	@Mock
	private ONGRepository ongRepository; 
	
	@Mock
	private AccountServiceImpl accountServiceImpl; 
	
	@Mock
	private PasswordEncoder encoder;

	@Test
	public void testSaveOng_Positive() throws OperationNotAllowedException {
		OngDTO ongDTO = new OngDTO();
		ongDTO.setName("Unicef España");
		ongDTO.setCif("D44577716");
		ongDTO.setDescription("Ayudamos a los niños del mundo");
		ongDTO.setEmail("unicef-esp@gmail.com");
		ongDTO.setPassword("unicef-esp-password-123");
		ongDTO.setRolAccount(RolAccount.ONG);
		ongDTO.setUsername("unicef-esp");

		when(ongRepository.save(Mockito.any(Ong.class))).thenAnswer(invocation -> {
		    Ong ong = invocation.getArgument(0);
		    ong.setId(1L);
		    return ong;
		});

		OngDTO result = ongServiceImpl.saveOng(ongDTO);

		assertNotNull(result);
		assertEquals(1L, result.getId().longValue());
		assertEquals("Unicef España", result.getName());
		assertEquals("D44577716", result.getCif());
		assertEquals("Ayudamos a los niños del mundo", result.getDescription());
		assertEquals("unicef-esp@gmail.com", result.getEmail());
		assertEquals(encoder.encode("unicef-esp-password-123"), result.getPassword());
		assertEquals(RolAccount.ONG, result.getRolAccount());
		assertEquals("unicef-esp", result.getUsername());
		verify(ongRepository, times(1)).save(any(Ong.class));
	}

	@Test
	public void testSaveOng_InvalidInput_ThrowsException() {

	    OngDTO ongDTO = new OngDTO(); 

		ongDTO.setName("Unicef España");
		ongDTO.setCif("CIF INVALIDO");
		ongDTO.setDescription("Ayudamos a los niños del mundo");
		ongDTO.setEmail(null);
		ongDTO.setPassword("unicef-esp-password-123");
		ongDTO.setRolAccount(RolAccount.ONG);
		ongDTO.setUsername("unicef-esp");

	    assertThrows(IllegalArgumentException.class, () -> ongServiceImpl.saveOng(ongDTO));
	}
	
	@Test
	public void testGetOng_Positive() {

	    OngDTO ongDTO = new OngDTO(); 

		ongDTO.setName("Unicef España");
		ongDTO.setCif("D44577716");
		ongDTO.setDescription("Ayudamos a los niños del mundo");
		ongDTO.setEmail("unicef-esp@gmail.com");
		ongDTO.setPassword("unicef-esp-password-123");
		ongDTO.setUsername("unicef-esp");
		
		Mockito.when(signinResponseDTO.getToken()).thenReturn("Token");
		Mockito.when(jwtUtils.getUserNameFromJwtToken("Token")).thenReturn("unicef-esp");
		Mockito.when(ongRepository.findByUsername("unicef-esp")).thenReturn(new Ong(ongDTO));
		
		OngDTO ongDTOgot;
		try {
			ongDTOgot = ongServiceImpl.getOng("Token");
			assertNotNull(ongDTOgot);
			assertEquals(ongDTO, ongDTOgot);	
		} catch (OperationNotAllowedException e) {
			e.printStackTrace();
		}


	}
}
