package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.GrantDTO;
import com.ispp.heartforchange.entity.Grant;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.repository.GrantRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.GrantService;

@Service
public class GrantServiceImpl implements GrantService {

	private static final Logger logger = LoggerFactory.getLogger(GrantServiceImpl.class);

	private GrantRepository grantRepository;
	private ONGRepository ongRepository;
	private JwtUtils jwtUtils;

	/*
	 * Dependency injection
	 */
	public GrantServiceImpl(GrantRepository grantRepository, ONGRepository ongRepository, JwtUtils jwtUtils) {
		super();
		this.grantRepository = grantRepository;
		this.ongRepository = ongRepository;
		this.jwtUtils = jwtUtils;
	}
	
	/*
	 * Get a grant by ong
	 * @Params String token
	 * @Return List<GrantDTO>
	 */
	@Override
	public List<GrantDTO> getGrantsByOng(String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		List<Grant> grants = grantRepository.findByOng(ong);
		List<GrantDTO> grantsDTO = new ArrayList<>();
		for (Grant grant : grants) {
			GrantDTO grantDTO = new GrantDTO(grant);
			grantsDTO.add(grantDTO);
		}
		return grantsDTO;
	}
	
	/*
	 * Get a grant by id
	 * @Params String token
	 * @Params Long id
	 * @Return GrantDTO
	 */
	@Override
	public GrantDTO getGrantById(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Grant> grant = grantRepository.findById(id);

		if (!grant.isPresent()) {
			throw new UsernameNotFoundException("Grant not found!");
		} else {
			if (grant.get().getOng().getId() == ong.getId()) {
				return new GrantDTO(grant.get());
			} else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}
	}
	
	/*
	 * Save a grant
	 * @Params String token
	 * @Params GrantDTO
	 * @Return GrantDTO
	 */
	@Override
	public GrantDTO saveGrant(GrantDTO grantDTO, String token) {
		Grant grant = new Grant(grantDTO);
		grant.setId(Long.valueOf(0));
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);

		grant.setOng(ong);
		logger.info("Saving Grant with ong={} and justification={}", grant.getOng(), grant.getJustification());
		try {
			Grant grantSaved = grantRepository.save(grant);
			return new GrantDTO(grantSaved);
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	/*
	 * Save a grant
	 * @Params String token
	 * @Params GrantDTO
	 * @Return GrantDTO
	 */
	@Override
	public GrantDTO updateGrant(String token, GrantDTO grantDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Grant> grant = grantRepository.findById(grantDTO.getId());
		logger.info("Grant is updating with id={}", grantDTO.getId());

		if (grant.isPresent()) {
			if (grant.get().getOng().getId() == ong.getId()) {
				grant.get().setAmount(grantDTO.getAmount());
				grant.get().setGubernamental(grantDTO.getGubernamental());
				grant.get().setJustification(grantDTO.getJustification());
				grant.get().setPrivateGrant(grantDTO.getPrivateGrant());
				grant.get().setState(grantDTO.getState());
				try {
					Grant grantSaved = grantRepository.save(grant.get());
					return new GrantDTO(grantSaved);
				} catch (Exception e) {
					throw new UsernameNotFoundException(e.getMessage());
				}
			} else {
				throw new UsernameNotFoundException("Update dennied!");
			}
		} else {
			throw new UsernameNotFoundException("This grant not exist!");
		}
	}
	
	/*
	 * Save a grant
	 * @Params String token
	 * @Long id
	 * @Return void
	 */
	@Override
	public void deleteGrant(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Grant> grant = grantRepository.findById(id);
		if (grant.isPresent()) {
			if (grant.get().getOng().getId() == ong.getId()) {
				System.out.println(grant);
				grantRepository.delete(grant.get());
			} else {
				throw new UsernameNotFoundException("Error deleting grant");
			}

		} else {
			throw new UsernameNotFoundException("This grant not exist!");
		}
	}

}
