package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.ComplementaryFormationDTO;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.ComplementaryFormation;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ComplementaryFormationRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.ComplementaryFormationService;

@Service
public class ComplementaryFormationServiceImpl implements ComplementaryFormationService{
	
	private static final Logger logger = LoggerFactory.getLogger(GrantServiceImpl.class);

	private ComplementaryFormationRepository complementaryFormationRepository;
	private VolunteerRepository volunteerRepository;
	private BeneficiaryRepository beneficiaryRepository;
	private AccountRepository accountRepository;
	private JwtUtils jwtUtils;

	/*
	 * Dependency injection
	 */
	public ComplementaryFormationServiceImpl(ComplementaryFormationRepository 
			complementaryFormationRepository, AccountRepository accountRepository, 
			VolunteerRepository volunteerRepository, BeneficiaryRepository beneficiaryRepository, 
			JwtUtils jwtUtils) {
		
		super();
		this.complementaryFormationRepository = complementaryFormationRepository;
		this.volunteerRepository = volunteerRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.accountRepository = accountRepository;
		this.jwtUtils = jwtUtils;
	}
	
	
	/*
	 * Get all complementary formations
	 * @Return List<ComplementaryFormationDTO>
	 */
	@Override
	public List<ComplementaryFormationDTO> getAllComplementaryFormations() {
		
		List<ComplementaryFormation> complementaryFormations = complementaryFormationRepository.findAll();
		List<ComplementaryFormationDTO> complementaryFormationsDTO = new ArrayList<>();
		
		for (ComplementaryFormation complementaryFormation : complementaryFormations) {
			
			ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
			complementaryFormationsDTO.add(complementaryFormationDTO);
		}
		
		return complementaryFormationsDTO;
	}
	
	
	
	/*
	 * Get complementary formation by id
	 * @Params Long id
	 * @Params String token
	 * @Return List<ComplementaryFormationDTO>
	 */
	@Override
	public ComplementaryFormationDTO getComplementaryFormationById(Long id, String token) {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<ComplementaryFormation> complementaryFormation = complementaryFormationRepository.findById(id);

		if (!complementaryFormation.isPresent()) {
			
			throw new UsernameNotFoundException("Complementary formation not found!");
			
		} else {
			
			if(rol == RolAccount.VOLUNTEER) {
				if(complementaryFormation.get().getVolunteer().getUsername().equals(username)) {
					
					return new ComplementaryFormationDTO(complementaryFormation.get());
					
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
			else{
				if(complementaryFormation.get().getBeneficiary() != null) {
					if(complementaryFormation.get().getBeneficiary().getOng().getUsername().equals(username)) {
						
						return new ComplementaryFormationDTO(complementaryFormation.get());
						
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(complementaryFormation.get().getVolunteer() != null) {
					
					if(complementaryFormation.get().getVolunteer().getOng().getUsername().equals(username)) {
						
						return new ComplementaryFormationDTO(complementaryFormation.get());
						
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
					
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
		}
	}
	
	
	
	/*
	 * Get all complementary formations of an volunteer
	 * @Params String volunteerUsername
	 * @Params String token
	 * @Return List<ComplementaryFormationDTO>
	 */
	@Override
	public List<ComplementaryFormationDTO> getComplementaryFormationByVolunteer(String 
			volunteerUsername, String token) {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		
		List<Volunteer> aux = volunteerRepository.findAll();
		int a = 0;
		for(Volunteer v: aux) {
			if (volunteerUsername.equals(v.getUsername())){
				a = 1;
			}
		}
		
		if(a==0) {
			throw new UsernameNotFoundException("The username doesn't exist!");

		}
		
		Optional<List<ComplementaryFormation>> complementaryFormations = complementaryFormationRepository.findComplementaryFormationByVolunteer(volunteerUsername);
		
		List<ComplementaryFormationDTO> complementaryFormationsDTO = new ArrayList<>();
		
		
		
		
		if (!complementaryFormations.isPresent()) {
			throw new UsernameNotFoundException("Complementary Formation not found!");
			
		} else {
			
			if(rol == RolAccount.ONG) {
				
				if(complementaryFormations.get().get(0).getVolunteer().getOng().getUsername().equals(username)) {
					for (ComplementaryFormation complementaryFormation : complementaryFormations.get()) {
						
						ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
						complementaryFormationsDTO.add(complementaryFormationDTO);
					}
					
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
				
			}else if(rol == RolAccount.VOLUNTEER) {
				
				if (username.equals(volunteerUsername)) {
					for (ComplementaryFormation complementaryFormation: complementaryFormations.get()) {
						
						ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
						complementaryFormationsDTO.add(complementaryFormationDTO);
					}
					
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
				
			}else {
				throw new UsernameNotFoundException("You don't have access!");
			}
			
		}
		
		return complementaryFormationsDTO;
	}
		
	
	
	
	/*
	 * Get all complementary formations of an beneficiary
	 * @Params String beneficiaryUsername
	 * @Params String token
	 * @Return ComplementaryFormationDTO
	 */
	@Override
	public List<ComplementaryFormationDTO> getComplementaryFormationByBeneficiary(String 
			beneficiaryUsername, String token) {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<List<ComplementaryFormation>> complementaryFormations = complementaryFormationRepository.findComplementaryFormationByBeneficiary(beneficiaryUsername);
		
		List<ComplementaryFormationDTO> complementaryFormationsDTO = new ArrayList<>();
		
		List<Beneficiary> aux = beneficiaryRepository.findAll();
		Integer a = 0;
		
		for(Beneficiary b: aux) {
			if (beneficiaryUsername.equals(b.getUsername())){
				a = 1;
			}
		
		}
		
		if(a==0) {
			throw new UsernameNotFoundException("The username doesn't exist!");

		}
		
		if (!complementaryFormations.isPresent()) {
			throw new UsernameNotFoundException("Complementary Formation not found!");
			
		} else {
			if(rol == RolAccount.ONG) {
				
				if(complementaryFormations.get().get(0).getBeneficiary().getOng().getUsername().equals(username)) {
					for (ComplementaryFormation complementaryFormation: complementaryFormations.get()) {
						
						ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
						complementaryFormationsDTO.add(complementaryFormationDTO);
					}
					
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
				
			}else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}
		return complementaryFormationsDTO;
		
	}
	
	
	
	/*
     * Save a complementary formation
     * @Params String token
     * @Params ComplementaryFormationDTO complementaryFormationDTO
     * @Return ComplementaryFormationDTO
     */
    @Override
    public ComplementaryFormationDTO saveComplementaryFormation(ComplementaryFormationDTO 
    		complementaryFormationDTO, String username) {

    	ComplementaryFormation complementaryFormation = new ComplementaryFormation(complementaryFormationDTO);
    	complementaryFormation.setId(Long.valueOf(0));
        List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();
        Beneficiary bAux = new Beneficiary();
        List<Volunteer> volunteers = volunteerRepository.findAll();
        Volunteer vAux = new Volunteer();


        for (Beneficiary beneficiary: beneficiaries) {
            if(beneficiary.getUsername().equals(username)) {
            	bAux = beneficiary;
            	complementaryFormation.setBeneficiary(beneficiary);
            }
        }

        for (Volunteer volunteer: volunteers) {
            if(volunteer.getUsername().equals(username)) {
            	vAux = volunteer;
            	complementaryFormation.setVolunteer(volunteer);
            }
        }

        logger.info("Complementary formation saved associated with {}", username);
        
        try {
        	
        	if(vAux.getUsername() != null || bAux.getUsername() != null) {
        		
        		ComplementaryFormation complementaryFormationSaved = complementaryFormationRepository.save(complementaryFormation);
                return new ComplementaryFormationDTO(complementaryFormationSaved);
                
        	}else {
        		throw new UsernameNotFoundException("The username doesn't exist");
        	}
        	
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    
    /*
     * Update a complementary formation
     * @Params String token
     * @Params ComplementaryFormationDTO complementaryFormationDTO
     * @Return ComplementaryFormationDTO
     */
	@Override
	public ComplementaryFormationDTO updateComplementaryFormation(String token, 
			ComplementaryFormationDTO complementaryFormationDTO) {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<ComplementaryFormation> complementaryFormation = complementaryFormationRepository.findById(complementaryFormationDTO.getId());
		logger.info("Complementary formation is updating with id={}", complementaryFormationDTO.getId());
		
		
		if (complementaryFormation.isPresent()) {
			if(rol == RolAccount.VOLUNTEER) {
				if(complementaryFormation.get().getVolunteer().getUsername().equals(username)) {
					complementaryFormation.get().setName(complementaryFormationDTO.getName());
					complementaryFormation.get().setOrganization(complementaryFormationDTO.getOrganization());
					complementaryFormation.get().setDate(complementaryFormationDTO.getDate());
					complementaryFormation.get().setPlace(complementaryFormationDTO.getPlace());
					try {
						ComplementaryFormation complementaryFormationSaved = complementaryFormationRepository.save(complementaryFormation.get());
						return new ComplementaryFormationDTO(complementaryFormationSaved);
					} catch (Exception e) {
						throw new UsernameNotFoundException(e.getMessage());
					}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
			else{
				if(complementaryFormation.get().getBeneficiary() != null) {
					if(complementaryFormation.get().getBeneficiary().getOng().getUsername().equals(username)) {
						complementaryFormation.get().setName(complementaryFormationDTO.getName());
						complementaryFormation.get().setOrganization(complementaryFormationDTO.getOrganization());
						complementaryFormation.get().setDate(complementaryFormationDTO.getDate());
						complementaryFormation.get().setPlace(complementaryFormationDTO.getPlace());
						try {
							ComplementaryFormation complementaryFormationSaved = complementaryFormationRepository.save(complementaryFormation.get());
							return new ComplementaryFormationDTO(complementaryFormationSaved);
						} catch (Exception e) {
							throw new UsernameNotFoundException(e.getMessage());
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(complementaryFormation.get().getVolunteer() != null) {
					if(complementaryFormation.get().getVolunteer().getOng().getUsername().equals(username)) {
						complementaryFormation.get().setName(complementaryFormationDTO.getName());
						complementaryFormation.get().setOrganization(complementaryFormationDTO.getOrganization());
						complementaryFormation.get().setDate(complementaryFormationDTO.getDate());
						complementaryFormation.get().setPlace(complementaryFormationDTO.getPlace());
						try {
							ComplementaryFormation complementaryFormationSaved = complementaryFormationRepository.save(complementaryFormation.get());
							return new ComplementaryFormationDTO(complementaryFormationSaved);
						} catch (Exception e) {
							throw new UsernameNotFoundException(e.getMessage());
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}

		} else {
			throw new UsernameNotFoundException("Complementary formation not exist!");
		}
	}

	
	/*
     * Delete a complementary formation
     * @Params String token
     * @Params Long id
     * @Return void
     */
	@Override
	public void deleteComplementaryFormation(Long id, String token) {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<ComplementaryFormation> complementaryFormation = complementaryFormationRepository.findById(id);
		
		if (complementaryFormation.isPresent()) {
			
			if(rol == RolAccount.VOLUNTEER) {
				if(complementaryFormation.get().getVolunteer().getUsername().equals(username)) {
					
					complementaryFormationRepository.delete(complementaryFormation.get());
					
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
			else{
				
				if(complementaryFormation.get().getBeneficiary() != null) {
					if(complementaryFormation.get().getBeneficiary().getOng().getUsername().equals(username)) {
						
						complementaryFormationRepository.delete(complementaryFormation.get());
						
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(complementaryFormation.get().getVolunteer() != null) {
					
					if(complementaryFormation.get().getVolunteer().getOng().getUsername().equals(username)) {
						complementaryFormationRepository.delete(complementaryFormation.get());
						
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
					
				}else {
					
					throw new UsernameNotFoundException("You don't have access!");
				}
			}

		} else {
			throw new UsernameNotFoundException("Complementary formation not exist!");
		}
	}

}
