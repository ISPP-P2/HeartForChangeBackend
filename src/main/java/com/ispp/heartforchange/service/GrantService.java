package com.ispp.heartforchange.service;

import java.util.List;

import com.ispp.heartforchange.dto.GrantDTO;

public interface GrantService {

	List<GrantDTO> getGrantsByOng(String token);

	GrantDTO getGrantById(Long id, String token);

	GrantDTO saveGrant(GrantDTO grantDTO, String token);

	GrantDTO updateGrant(String token, GrantDTO grantDTO);

	void deleteGrant(Long id, String token);

}
