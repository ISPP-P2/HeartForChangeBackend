package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.WorkExperienceDTO;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.repository.WorkExperienceRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.WorkExperienceService;

@Service
public class WorkExperienceServiceImpl implements WorkExperienceService {

	private static final Logger logger = LoggerFactory.getLogger(WorkExperienceServiceImpl.class);

	private WorkExperienceRepository workExperienceRepository;
	private AccountRepository accountRepository;
	private BeneficiaryRepository beneficiaryRepository;
	private VolunteerRepository volunteerRepository;
	private ONGRepository ongRepository;
	private JwtUtils jwtUtils;
	
	
	/*
	 * Dependency injection
	 */
	public WorkExperienceServiceImpl(WorkExperienceRepository workExperienceRepository, AccountRepository accountRepository,
			BeneficiaryRepository beneficiaryRepository, VolunteerRepository volunteerRepository, ONGRepository ongRepository,
			JwtUtils jwtUtils) {
		super();
		this.workExperienceRepository = workExperienceRepository;
		this.accountRepository = accountRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.volunteerRepository = volunteerRepository;
		this.ongRepository = ongRepository;
		this.jwtUtils = jwtUtils;
	}



	/*
	 * Get work experience by id
	 * @Params Long id
	 * @Params String token
	 * @Return WorkExperienceDTO
	 */
	@Override
	public WorkExperienceDTO getWorkExperienceById(Long id, String token) throws OperationNotAllowedException{
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
		
		if(ong!=null || volunteer!=null) {
			Optional<WorkExperience> workExperience = workExperienceRepository.findById(id);
			if (!workExperience.isPresent()) {
				throw new UsernameNotFoundException("Work Experience not exist!");
			} else {
				if(rol == RolAccount.VOLUNTEER) {
					if(workExperience.get().getBeneficiary() == null) {
						if(workExperience.get().getVolunteer().getId()==volunteer.getId()) {
							return new WorkExperienceDTO(workExperience.get());
						}else {
							throw new UsernameNotFoundException("You don't have access!");
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else{
					if(workExperience.get().getBeneficiary() != null) {
						if(workExperience.get().getBeneficiary().getOng().getId()==ong.getId()) {
							return new WorkExperienceDTO(workExperience.get());
						}else {
							throw new UsernameNotFoundException("You don't have access!");
						}
					}
					else if(workExperience.get().getVolunteer() != null) {
						if(workExperience.get().getVolunteer().getOng().getId()==ong.getId()) {
							return new WorkExperienceDTO(workExperience.get());
						}else {
							throw new UsernameNotFoundException("Work Experience not exist!");
						}
					}else {
						throw new UsernameNotFoundException("Work Experience not exist!");
					}
				}
			}
		}else{
			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
		}
		
		
	}

	
	/*
	 * Get all work experiences of an volunteer by his id
	 * @Params Long volunteerId
	 * @Params String token
	 * @Return List<WorkExperienceDTO>
	 */
	@Override
	public List<WorkExperienceDTO> getWorkExperienceByVolunteer(Long volunteerId, String token) throws OperationNotAllowedException{
				
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Volunteer loggedVolunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong loggedOng = ongRepository.findByUsername(username);
		
		Optional<List<WorkExperience>> workExperiences = workExperienceRepository.findWorkExperienceByVolunteerId(volunteerId);
  		
  		Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);
  		
  		List<WorkExperienceDTO> workExperiencesDTO = new ArrayList<>();
  		
  		if(loggedOng != null || loggedVolunteer != null) {
  	  		if(volunteer.isPresent()){
  	  			if(workExperiences.isPresent()) {
  	  				if(loggedOng != null) {
  	  					if(volunteer.get().getOng().equals(loggedOng)) {
	  	  					for (WorkExperience workExperience : workExperiences.get()) {
								WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
								workExperiencesDTO.add(workExperienceDTO);
	  	  					}
  	  					}else {
  	  						throw new UsernameNotFoundException("This volunteer does not belong to your ONG!");
  	  					}
  	  				}else if(loggedVolunteer != null) {
  	  					if(loggedVolunteer.equals(volunteer.get())) {
	  	  					for (WorkExperience workExperience : workExperiences.get()) {
								WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
								workExperiencesDTO.add(workExperienceDTO);
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
  		
  		return workExperiencesDTO;		
	}
	
	/*
	 * Get all work experiences of an beneficiary by his id
	 * @Params Long beneficiaryId
	 * @Params String token
	 * @Return WorkExperienceDTO
	 */
	@Override
	public List<WorkExperienceDTO> getWorkExperienceByBeneficiary(Long beneficiaryId, String token) throws OperationNotAllowedException{
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		
		Optional<List<WorkExperience>> workExperiences = workExperienceRepository.findWorkExperienceByBeneficiaryId(beneficiaryId);
  		
  		Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(beneficiaryId);
  		
  		List<WorkExperienceDTO> workExperiencesDTO = new ArrayList<>();
  		
  		if(ong != null) {
  	  		if(beneficiary.isPresent()){
  	  			if(workExperiences.isPresent()) {
  					if(beneficiary.get().getOng().equals(ong)) {
  	  					for (WorkExperience workExperience : workExperiences.get()) {
							WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
							workExperiencesDTO.add(workExperienceDTO);
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
  		
  		return workExperiencesDTO;	
	}

	
	/*
     * Save a work experience
     * @Params String token
     * @Params Long id
     * @Params WorkExperienceDTO workExperienceDTO
     * @Return WorkExperienceDTO
     */
    @Override
    public WorkExperienceDTO saveWorkExperience(WorkExperienceDTO workExperienceDTO, Long id, String token) throws OperationNotAllowedException{

    	String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
    	
    	WorkExperience workExperience = new WorkExperience(workExperienceDTO);
    	workExperience.setId(Long.valueOf(0));
    	
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(id);
        Optional<Volunteer> volunteer = volunteerRepository.findById(id);
        
        if(ong!=null) {
            if(beneficiary.isPresent()) {
            	if(beneficiary.get().getOng().equals(ong)) {
            		logger.info("Work Experience saved associated with id={}", id);
            		workExperience.setBeneficiary(beneficiary.get());
	        		WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience);
	                return new WorkExperienceDTO(workExperienceSaved);
            	}else{
            		throw new UsernameNotFoundException("You cannot edit information about a beneficiary who does not belong to your ONG!");
            	}
            	
            }else if(volunteer.isPresent()) {
            	if(volunteer.get().getOng().equals(ong)) {
            		workExperience.setVolunteer(volunteer.get());
            		logger.info("Work Experience saved associated with id={}", id);
	        		WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience);
	                return new WorkExperienceDTO(workExperienceSaved);
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
     * Update a work experience
     * @Params String token
     * @Params WorkExperienceDTO workExperienceDTO
     * @Params Long id
     * @Return WorkExperienceDTO
     */
	@Override
	public WorkExperienceDTO updateWorkExperience(String token, WorkExperienceDTO workExperienceDTO, Long id) throws OperationNotAllowedException{
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		
		Optional<WorkExperience> workExperience = workExperienceRepository.findById(id);
		logger.info("Work Experience is updating with id={}", id);
		
		if(ong!=null) {
			if (workExperience.isPresent()) {
				if(workExperience.get().getBeneficiary() != null) {
					if(workExperience.get().getBeneficiary().getOng().getId() == ong.getId()) {
						workExperience.get().setJob(workExperienceDTO.getJob());
						workExperience.get().setPlace(workExperienceDTO.getPlace());
						workExperience.get().setReasonToFinish(workExperienceDTO.getReasonToFinish());
						workExperience.get().setTime(workExperienceDTO.getTime());
						try {
							WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience.get());
							return new WorkExperienceDTO(workExperienceSaved);
						} catch (Exception e) {
							throw new UsernameNotFoundException(e.getMessage());
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(workExperience.get().getVolunteer() != null) {
					if(workExperience.get().getVolunteer().getOng().getId() == ong.getId()) {
						workExperience.get().setJob(workExperienceDTO.getJob());
						workExperience.get().setPlace(workExperienceDTO.getPlace());
						workExperience.get().setReasonToFinish(workExperienceDTO.getReasonToFinish());
						workExperience.get().setTime(workExperienceDTO.getTime());
						try {
							WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience.get());
							return new WorkExperienceDTO(workExperienceSaved);
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
				throw new UsernameNotFoundException("Work Experience not exist!");
			}
		}else {
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
  		}
	}

	
	/*
     * Delete a work experience
     * @Params String token
     * @Params Long id
     * @Return void
     */
	@Override
	public void deleteWorkExperience(Long id, String token) throws OperationNotAllowedException {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		
		Optional<WorkExperience> workExperience = workExperienceRepository.findById(id);
		
		if(ong!=null) {
			if (workExperience.isPresent()) {
				if(workExperience.get().getBeneficiary() != null) {
					if(workExperience.get().getBeneficiary().getOng().getId() == ong.getId()) {
						workExperienceRepository.delete(workExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(workExperience.get().getVolunteer() != null) {
					if(workExperience.get().getVolunteer().getOng().getId() == ong.getId()) {
						workExperienceRepository.delete(workExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			} else {
				throw new UsernameNotFoundException("Work Experience not exist!");
			}
		}else {
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
  		}
	}
	
}
