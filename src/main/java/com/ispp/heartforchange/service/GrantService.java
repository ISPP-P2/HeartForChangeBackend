package com.ispp.heartforchange.service;

import java.util.List;

import com.ispp.heartforchange.dto.GrantDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;

public interface GrantService {

	List<GrantDTO> getGrantsByOng(String token) throws OperationNotAllowedException;

	GrantDTO getGrantById(Long id, String token) throws OperationNotAllowedException;
	
	Integer getTotalAmountAcceptedGrantsByOng(String token) throws OperationNotAllowedException;

	GrantDTO saveGrant(GrantDTO grantDTO, String token) throws OperationNotAllowedException;

	GrantDTO updateGrant(String token, GrantDTO grantDTO, Long id) throws OperationNotAllowedException;

	void deleteGrant(Long id, String token) throws OperationNotAllowedException;

}
