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
	 * Get all work experiences
	 * @Return List<WorkExperienceDTO>
	 */
	@Override
	public List<WorkExperienceDTO> getAllWorkExperiences() {
		List<WorkExperience> workExperiences = workExperienceRepository.findAll();
		List<WorkExperienceDTO> workExperiencesDTO = new ArrayList<>();
		for (WorkExperience workExperience : workExperiences) {
			WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
			workExperiencesDTO.add(workExperienceDTO);
		}
		return workExperiencesDTO;
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
				throw new UsernameNotFoundException("Not Found: Work Experience not exist!");
			} else {
				if(rol == RolAccount.VOLUNTEER) {
					if(workExperience.get().getVolunteer().getId()==volunteer.getId()) {
						return new WorkExperienceDTO(workExperience.get());
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
							throw new UsernameNotFoundException("Not Found: Work Experience not exist!");
						}
					}else {
						throw new UsernameNotFoundException("Not Found: Work Experience not exist!");
					}
				}
			}
		}else{
			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
		}
		
		
	}

	
	/*
	 * Get all work experiences of an volunteer by his username
	 * @Params Long volunteerId
	 * @Params String token
	 * @Return List<WorkExperienceDTO>
	 */
	@Override
	public List<WorkExperienceDTO> getWorkExperienceByVolunteer(Long volunteerId, String token) throws OperationNotAllowedException{
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<List<WorkExperience>> workExperiences = workExperienceRepository.findWorkExperienceByVolunteerId(volunteerId);
		Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
		
		List<Volunteer> auxVolunteers = volunteerRepository.findAll();
  		boolean exception = true;
  		for(Volunteer v: auxVolunteers) {
  			if (v.getId()==volunteerId){
  				exception = false;
  			}
  		}

  		if(exception==true) {
  			throw new UsernameNotFoundException("Not Found: The volunteer with this ID doesn't exist!");

  		}
  		
  		if(ong!=null || volunteer!=null) {
			List<WorkExperienceDTO> workExperiencesDTO = new ArrayList<>();
			
			if (!workExperiences.isPresent()) {
				throw new UsernameNotFoundException("Not Found: Work Experience not exist!");
			} else {
				if(rol == RolAccount.ONG) {
					if(workExperiences.get().size() == 0) {
		 				throw new UsernameNotFoundException("Not Found: This volunteer has not work experiences!");
		 			}else {
		 				if(workExperiences.get().get(0).getVolunteer().getOng().getId() == ong.getId()) {
							for (WorkExperience workExperience : workExperiences.get()) {
								WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
								workExperiencesDTO.add(workExperienceDTO);
							}
							return workExperiencesDTO;
						}else {
							throw new UsernameNotFoundException("You don't have access!");
						}
		 			}
				}else if(rol == RolAccount.VOLUNTEER) {
					if(workExperiences.get().size() == 0) {
		 				throw new UsernameNotFoundException("Not Found: This volunteer has not work experiences!");
		 			}else {
						if (workExperiences.get().get(0).getVolunteer().getId() == volunteer.getId()) {
							for (WorkExperience workExperience : workExperiences.get()) {
								WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
								workExperiencesDTO.add(workExperienceDTO);
							}
							return workExperiencesDTO;
						}else {
							throw new UsernameNotFoundException("You don't have access!");
						}
		 			}
		 		}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
  		}else {
  			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
  		}
		
	}
	
	/*
	 * Get all work experiences of an beneficiary by his username
	 * @Params Long beneficiaryId
	 * @Params String token
	 * @Return WorkExperienceDTO
	 */
	@Override
	public List<WorkExperienceDTO> getWorkExperienceByBeneficiary(Long beneficiaryId, String token) throws OperationNotAllowedException{
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<List<WorkExperience>> workExperiences = workExperienceRepository.findWorkExperienceByBeneficiaryId(beneficiaryId);
		Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
		
		List<Beneficiary> auxBeneficiaries = beneficiaryRepository.findAll();
  		boolean exception = true;
  		for(Beneficiary b: auxBeneficiaries) {
  			if (b.getId()==beneficiaryId){
  				exception = false;
  			}
  		}

  		if(exception==true) {
  			throw new UsernameNotFoundException("Not Found: The beneficiary with this ID doesn't exist!");

  		}
		
  		if(ong!=null || volunteer!=null) {
			List<WorkExperienceDTO> workExperiencesDTO = new ArrayList<>();
			
			if (!workExperiences.isPresent()) {
				throw new UsernameNotFoundException("Not Found: Work Experience not exist!");
			} else {
				if(rol == RolAccount.ONG) {
					if(workExperiences.get().size() == 0) {
		 				throw new UsernameNotFoundException("Not Found: This beneficiary has not work experiences!");
		 			}else {
		 				if(workExperiences.get().get(0).getBeneficiary().getId() == beneficiaryId) {
							for (WorkExperience workExperience : workExperiences.get()) {
								WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
								workExperiencesDTO.add(workExperienceDTO);
							}
							return workExperiencesDTO;
						}else {
							throw new UsernameNotFoundException("You don't have access!");
						}
		 			}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
  		}else {
  			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
  		}
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
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
    	
    	WorkExperience workExperience = new WorkExperience(workExperienceDTO);
    	workExperience.setId(Long.valueOf(0));
        List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();
        List<Volunteer> volunteers = volunteerRepository.findAll();
        Beneficiary beneficiaryAux = new Beneficiary();
        Volunteer volunteerAux = new Volunteer();
        
        for (Beneficiary b: beneficiaries) {
            if(b.getId() == id) {
            	beneficiaryAux = b;
            	workExperience.setBeneficiary(b);
            }
        }

        for (Volunteer v: volunteers) {
            if(v.getId() == id) {
            	volunteerAux = v;
            	workExperience.setVolunteer(v);
            }
        }

        if(ong!=null || volunteer!=null) {
	        try {
	        	if(volunteerAux.getId() != null) {
	        		if(rol == RolAccount.ONG && volunteerAux.getOng().getId() == ong.getId()) {
		        		logger.info("Work Experience saved associated with id={}", id);
		        		WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience);
		                return new WorkExperienceDTO(workExperienceSaved);
	        		}else if(rol == RolAccount.VOLUNTEER && volunteerAux.getId() == volunteer.getId()){
	        			logger.info("Work Experience saved associated with id={}", id);
		        		WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience);
		                return new WorkExperienceDTO(workExperienceSaved);
	        		}else {
						throw new UsernameNotFoundException("You don't have access!");
	        		}
	        	}else if(beneficiaryAux.getId() != null) {
	        		if(rol == RolAccount.ONG && beneficiaryAux.getOng().getId() == ong.getId()) {
		        		logger.info("Work Experience saved associated with id={}", id);
		        		WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience);
		                return new WorkExperienceDTO(workExperienceSaved);
	        		}else {
						throw new UsernameNotFoundException("You don't have access!");
	        		}
	        	}else {
	        		throw new UsernameNotFoundException("Not Found: The volunteer or beneficiary doesn't exist");
	        	}
	        } catch (Exception e) {
	            throw new UsernameNotFoundException(e.getMessage());
	        }
        }else {
  			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
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
		Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
		
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<WorkExperience> workExperience = workExperienceRepository.findById(id);
		logger.info("Work Experience is updating with id={}", id);
		
		if(ong!=null || volunteer!=null) {
			if (workExperience.isPresent()) {
				if(rol == RolAccount.VOLUNTEER) {
					if(workExperience.get().getVolunteer().getId() == volunteer.getId()) {
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
				else{
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
				}
	
			} else {
				throw new UsernameNotFoundException("Not Found: Work Experience not exist!");
			}
		}else {
  			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
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
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
		
		Optional<WorkExperience> workExperience = workExperienceRepository.findById(id);
		
		if(ong!=null || volunteer!=null) {
			if (workExperience.isPresent()) {
				if(rol == RolAccount.VOLUNTEER) {
					if(workExperience.get().getVolunteer().getId() == volunteer.getId()) {
						workExperienceRepository.delete(workExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else{
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
				}
	
			} else {
				throw new UsernameNotFoundException("Not Found: Work Experience not exist!");
			}
		}else {
  			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
  		}
	}
	
}
