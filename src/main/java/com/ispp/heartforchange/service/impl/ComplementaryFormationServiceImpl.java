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
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.expections.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ComplementaryFormationRepository;
import com.ispp.heartforchange.repository.ONGRepository;
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
	private ONGRepository ongRepository;
	private JwtUtils jwtUtils;

	/*
	 * Dependency injection
	 */
	public ComplementaryFormationServiceImpl(ComplementaryFormationRepository 
			complementaryFormationRepository, AccountRepository accountRepository, 
			VolunteerRepository volunteerRepository, BeneficiaryRepository beneficiaryRepository,
			ONGRepository ongRepository, JwtUtils jwtUtils) {
		
		super();
		this.complementaryFormationRepository = complementaryFormationRepository;
		this.volunteerRepository = volunteerRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.accountRepository = accountRepository;
		this.ongRepository = ongRepository;
		this.jwtUtils = jwtUtils;
	}
	
	
	
	
	/*
	 * Get complementary formation by id
	 * @Params Long id
	 * @Params String token
	 * @Return ComplementaryFormationDTO
	 */
	@Override
	public ComplementaryFormationDTO getComplementaryFormationById(Long id, String token) throws OperationNotAllowedException{
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
		
		if(ong!=null || volunteer!=null) {
			
			Optional<ComplementaryFormation> complementaryFormation = complementaryFormationRepository.findById(id);
			if (!complementaryFormation.isPresent()) {
				throw new UsernameNotFoundException("Complementary Formation not exist!");
				
			} else {
				if(rol == RolAccount.VOLUNTEER) {

					
					if(complementaryFormation.get().getBeneficiary() == null && complementaryFormation.get().getVolunteer().getId()==volunteer.getId()) {
						return new ComplementaryFormationDTO(complementaryFormation.get());
						
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				
				}
				else{
					if(complementaryFormation.get().getBeneficiary() != null) {
						
						if(complementaryFormation.get().getBeneficiary().getOng().getId()==ong.getId()) {
							return new ComplementaryFormationDTO(complementaryFormation.get());
						}else {
							throw new UsernameNotFoundException("You don't have access!");
						}
					}
					
					else if(complementaryFormation.get().getVolunteer() != null) {
						
						if(complementaryFormation.get().getVolunteer().getOng().getId()==ong.getId()) {
							return new ComplementaryFormationDTO(complementaryFormation.get());
							
						}else {
							throw new UsernameNotFoundException("Complementary Formation not exist!");
						}
						
					}else {
						throw new UsernameNotFoundException("Complementary Formation not exist!");
					}
				}
			}
			
		}else{
			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
		}
		
		
	}
	
	
	
	/*
	 * Get all complementary formations of an volunteer
	 * @Params Long volunteerId
	 * @Params String token
	 * @Return List<ComplementaryFormationDTO>
	 */
	@Override
	public List<ComplementaryFormationDTO> getComplementaryFormationByVolunteer(Long 
			volunteerId, String token) throws OperationNotAllowedException {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Volunteer loggedVolunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong loggedOng = ongRepository.findByUsername(username);
		
		Optional<List<ComplementaryFormation>> complementaryFormations = complementaryFormationRepository.findComplementaryFormationByVolunteer(volunteerId);
  		
  		Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);
  		
  		List<ComplementaryFormationDTO> complementaryFormationsDTO = new ArrayList<>();
  		
  		if(loggedOng != null || loggedVolunteer != null) {
  	  		if(volunteer.isPresent()){
  	  			if(complementaryFormations.isPresent()) {
  	  				if(loggedOng != null) {
  	  					if(volunteer.get().getOng().equals(loggedOng)) {
	  	  					for (ComplementaryFormation complementaryFormation : complementaryFormations.get()) {
	  	  						ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
	  	  						complementaryFormationsDTO.add(complementaryFormationDTO);
	  	  					}
  	  					}else {
  	  						throw new UsernameNotFoundException("This volunteer does not belong to your ONG!");
  	  					}
  	  				}else if(loggedVolunteer != null) {
  	  					if(loggedVolunteer.equals(volunteer.get())) {
	  	  					for (ComplementaryFormation complementaryFormation : complementaryFormations.get()) {
	  	  						ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
	  	  						complementaryFormationsDTO.add(complementaryFormationDTO);
	  	  					}
  	  					}else {
  	  						throw new UsernameNotFoundException("You cannot get information about other volunteers.");
  	  					}
  	  				}
  	  			}
  	  		}else {
  	  			throw new UsernameNotFoundException("The volunteer with this ID doesn't exist!");
  	  		}
  		}else {
  			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
  		}
  		
  		return complementaryFormationsDTO;		
	}
		
	
	
	
	/*
	 * Get all complementary formations of an beneficiary
	 * @Params String beneficiaryId
	 * @Params String token
	 * @Return ComplementaryFormationDTO
	 */
	@Override
	public List<ComplementaryFormationDTO> getComplementaryFormationByBeneficiary(Long 
			beneficiaryId, String token) throws OperationNotAllowedException {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		
		Optional<List<ComplementaryFormation>> complementaryFormations = complementaryFormationRepository.findComplementaryFormationByBeneficiary(beneficiaryId);
  		
  		Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(beneficiaryId);
  		
  		List<ComplementaryFormationDTO> complementaryFormationsDTO = new ArrayList<>();
  		
  		if(ong != null) {
  	  		if(beneficiary.isPresent()){
  	  			if(complementaryFormations.isPresent()) {
  					if(beneficiary.get().getOng().equals(ong)) {
  	  					for (ComplementaryFormation complementaryFormation : complementaryFormations.get()) {
  	  						ComplementaryFormationDTO complementaryFormationDTO = new ComplementaryFormationDTO(complementaryFormation);
							complementaryFormationsDTO.add(complementaryFormationDTO);
  	  					}
  					}else {
  						throw new UsernameNotFoundException("This beneficiary does not belong to your ONG!");
  					}
  	  			}
  	  		}else {
  	  			throw new UsernameNotFoundException("The beneficiary with this ID doesn't exist!");
  	  		}
  		}else {
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
  		}
  		
  		return complementaryFormationsDTO;	
	}
	
	
	
	/*
     * Save a complementary formation
     * @Params String token
     * @Params Long id
     * @Params ComplementaryFormationDTO complementaryFormationDTO
     * @Return ComplementaryFormationDTO
     */
    @Override
    public ComplementaryFormationDTO saveComplementaryFormation(ComplementaryFormationDTO 
    		complementaryFormationDTO, Long id, String token) throws OperationNotAllowedException {
    	
    	String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
    	
    	ComplementaryFormation complementaryFormation = new ComplementaryFormation(complementaryFormationDTO);
    	complementaryFormation.setId(Long.valueOf(0));
    	
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(id);
        Optional<Volunteer> volunteer = volunteerRepository.findById(id);
        
        if(ong!=null) {
            if(beneficiary.isPresent()) {
            	if(beneficiary.get().getOng().equals(ong)) {
            		logger.info("Complementary Formation saved associated with id={}", id);
            		complementaryFormation.setBeneficiary(beneficiary.get());
            		ComplementaryFormation complementaryFormationSaved = complementaryFormationRepository.save(complementaryFormation);
	                return new ComplementaryFormationDTO(complementaryFormationSaved);
            	}else{
            		throw new UsernameNotFoundException("You cannot edit information about a beneficiary who does not belong to your ONG!");
            	}
            	
            }else if(volunteer.isPresent()) {
            	if(volunteer.get().getOng().equals(ong)) {
            		complementaryFormation.setVolunteer(volunteer.get());
            		logger.info("Complementary Formation saved associated with id={}", id);
            		ComplementaryFormation complementaryFormationSaved = complementaryFormationRepository.save(complementaryFormation);
	                return new ComplementaryFormationDTO(complementaryFormationSaved);
            	}else{
            		throw new UsernameNotFoundException("You cannot edit information about a volunteer who does not belong to your ONG!");
            	}
            }else {
            	throw new UsernameNotFoundException("The volunteer or beneficiary doesn't exist");
            }
        }else {
        	throw new OperationNotAllowedException("You must be an ONG to use this method.");
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
			ComplementaryFormationDTO complementaryFormationDTO, Long id) throws OperationNotAllowedException {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<ComplementaryFormation> complementaryFormation = complementaryFormationRepository.findById(id);
		logger.info("Complementary formation is updating with id={}", complementaryFormationDTO.getId());
		
		
		if (ong!=null) {
			if(complementaryFormation.isPresent()) {
				if(complementaryFormation.get().getBeneficiary() != null) {
					if(complementaryFormation.get().getBeneficiary().getOng().getId() == ong.getId()) {
						
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
					if(complementaryFormation.get().getVolunteer().getOng().getId() == ong.getId()) {
						
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
				
			} else {
				throw new UsernameNotFoundException("Complementary Formation not exist!");
			}
			
		}else {
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
  		}
		
	}

	
	/*
     * Delete a complementary formation
     * @Params String token
     * @Params Long id
     * @Return void
     */
	@Override
	public void deleteComplementaryFormation(Long id, String token) throws OperationNotAllowedException {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<ComplementaryFormation> complementaryFormation = complementaryFormationRepository.findById(id);
		
		if (ong!=null) {
			
			if(complementaryFormation.isPresent()) {
				if(complementaryFormation.get().getBeneficiary() != null) {
					
					if(complementaryFormation.get().getBeneficiary().getOng().getId() == ong.getId()) {
						complementaryFormationRepository.delete(complementaryFormation.get());
						
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				
				else if(complementaryFormation.get().getVolunteer() != null) {
					
					if(complementaryFormation.get().getVolunteer().getOng().getId() == ong.getId()) {
						complementaryFormationRepository.delete(complementaryFormation.get());
						
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
					
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
				
			} else {
				throw new UsernameNotFoundException("Complementary Formation not exist!");
			}
			
		}else {
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
  		}
	}

}
