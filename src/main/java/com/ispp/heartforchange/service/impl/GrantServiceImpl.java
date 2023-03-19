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
import com.ispp.heartforchange.entity.GrantState;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
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
	public List<GrantDTO> getGrantsByOng(String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		List<Grant> grants = grantRepository.findByOng(ong);
		List<GrantDTO> grantsDTO = new ArrayList<>();
		
		if(ong!=null) {
			for (Grant grant : grants) {
				if(grant.getOng().getId() == ong.getId()) {
					GrantDTO grantDTO = new GrantDTO(grant);
					grantsDTO.add(grantDTO);
				}
			}
			return grantsDTO;
		}else{
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
 		}	
	}
	
	
	/*
	 * Get total amount of accepted grants by ong
	 * @Params String token
	 * @Return GrantDTO
	 */
	@Override
	public Double getTotalAmountAcceptedGrantsByOng(String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		List<Grant> grants = grantRepository.findByOng(ong);
		
		Double amount = 0.0;
		
		if(ong!=null) {
			for (Grant grant : grants) {
				if(grant.getOng().getId() == ong.getId()) {
					if(grant.getState() == GrantState.ACCEPTED) {
						amount += grant.getAmount();
					}
				}
			}
			return amount;
		}else{
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
 		}	
	}
	
	
	/*
	 * Get a grant by id
	 * @Params String token
	 * @Params Long id
	 * @Return GrantDTO
	 */
	@Override
	public GrantDTO getGrantById(Long id, String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Grant> grant = grantRepository.findById(id);

		if(ong!=null) {
			if (!grant.isPresent()) {
				throw new UsernameNotFoundException("Not Found: Grant not exist!");
			} else {
				if (grant.get().getOng().getId() == ong.getId()) {
					return new GrantDTO(grant.get());
				} else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
		}else{
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
 		}
	}
	
	/*
	 * Save a grant
	 * @Params String token
	 * @Params GrantDTO grantDTO
	 * @Return GrantDTO
	 */
	@Override
	public GrantDTO saveGrant(GrantDTO grantDTO, String token) throws OperationNotAllowedException {
		
		Grant grant = new Grant(grantDTO);
		grant.setId(Long.valueOf(0));
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);

		if(ong!=null) {
			grant.setOng(ong);
			try {
				Grant grantSaved = grantRepository.save(grant);
				return new GrantDTO(grantSaved);
			} catch (Exception e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
		}else{
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
 		}
	}
	
	/*
	 * Save a grant
	 * @Params String token
	 * @Params GrantDTO grantDTO
	 * @Return GrantDTO
	 */
	@Override
	public GrantDTO updateGrant(String token, GrantDTO grantDTO, Long id) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Grant> grant = grantRepository.findById(id);
		logger.info("Grant is updating with id={}", id);

		if(ong!=null) {
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
					throw new UsernameNotFoundException("You don't have access!");
				}
			} else {
				throw new UsernameNotFoundException("Not Found: Grant not exist!");
			}
		}else{
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
 		}
	}
	
	/*
	 * Save a grant
	 * @Params String token
	 * @Long id
	 * @Return void
	 */
	@Override
	public void deleteGrant(Long id, String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Grant> grant = grantRepository.findById(id);
		
		if(ong!=null) {
			if (grant.isPresent()) {
				if (grant.get().getOng().getId() == ong.getId()) {
					grantRepository.delete(grant.get());
				} else {
					throw new UsernameNotFoundException("You don't have access!");
				}
	
			} else {
				throw new UsernameNotFoundException("Not Found: Grant not exist!");
			}
		}else{
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
 		}
	}

}
