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
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.entity.WorkExperience;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
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
	private JwtUtils jwtUtils;
	
	
	/*
	 * Dependency injection
	 */
	public WorkExperienceServiceImpl(WorkExperienceRepository workExperienceRepository, AccountRepository accountRepository,
			BeneficiaryRepository beneficiaryRepository, VolunteerRepository volunteerRepository, JwtUtils jwtUtils) {
		super();
		this.workExperienceRepository = workExperienceRepository;
		this.accountRepository = accountRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.volunteerRepository = volunteerRepository;
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
	public WorkExperienceDTO getWorkExperienceById(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<WorkExperience> workExperience = workExperienceRepository.findById(id);

		if (!workExperience.isPresent()) {
			throw new UsernameNotFoundException("Work Experience not found!");
		} else {
			if(rol == RolAccount.VOLUNTEER) {
				if(workExperience.get().getVolunteer().getUsername().equals(username)) {
					return new WorkExperienceDTO(workExperience.get());
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
			else{
				if(workExperience.get().getBeneficiary() != null) {
					if(workExperience.get().getBeneficiary().getOng().getUsername().equals(username)) {
						return new WorkExperienceDTO(workExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(workExperience.get().getVolunteer() != null) {
					if(workExperience.get().getVolunteer().getOng().getUsername().equals(username)) {
						return new WorkExperienceDTO(workExperience.get());
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
	 * Get all work experiences of an volunteer by his username
	 * @Params String volunteerUserName
	 * @Params String token
	 * @Return List<WorkExperienceDTO>
	 */
	@Override
	public List<WorkExperienceDTO> getWorkExperienceByVolunteerUsername(String volunteerUserName, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<List<WorkExperience>> workExperiences = workExperienceRepository.findWorkExperienceByVolunteerUserName(volunteerUserName);
		
		List<Volunteer> auxVolunteers = volunteerRepository.findAll();
  		boolean exception = true;
  		for(Volunteer v: auxVolunteers) {
  			if (volunteerUserName.equals(v.getUsername())){
  				exception = false;
  			}
  		}

  		if(exception==true) {
  			throw new UsernameNotFoundException("The username of the volunteer doesn't exist!");

  		}
  		
		List<WorkExperienceDTO> workExperiencesDTO = new ArrayList<>();
		
		if (!workExperiences.isPresent()) {
			throw new UsernameNotFoundException("Work Experience not found!");
		} else {
			if(rol == RolAccount.ONG) {
				if(workExperiences.get().size() == 0) {
	 				throw new UsernameNotFoundException("This volunteer has not work experiences!");
	 			}else {
	 				if(workExperiences.get().get(0).getVolunteer().getOng().getUsername().equals(username)) {
						for (WorkExperience workExperience : workExperiences.get()) {
							WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
							workExperiencesDTO.add(workExperienceDTO);
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
	 			}
			}else if(rol == RolAccount.VOLUNTEER) {
				if(workExperiences.get().size() == 0) {
	 				throw new UsernameNotFoundException("This volunteer has not work experiences!");
	 			}else {
					if (username.equals(volunteerUserName)) {
						for (WorkExperience workExperience : workExperiences.get()) {
							WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
							workExperiencesDTO.add(workExperienceDTO);
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
	 			}
	 		}else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}
		return workExperiencesDTO;
	}
	
	/*
	 * Get all work experiences of an beneficiary by his username
	 * @Params String beneficiaryUserName
	 * @Params String token
	 * @Return WorkExperienceDTO
	 */
	@Override
	public List<WorkExperienceDTO> getWorkExperienceByBeneficiaryUsername(String beneficiaryUserName, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<List<WorkExperience>> workExperiences = workExperienceRepository.findWorkExperienceByBeneficiaryUserName(beneficiaryUserName);
		
		List<Beneficiary> auxBeneficiaries = beneficiaryRepository.findAll();
  		boolean exception = true;
  		for(Beneficiary b: auxBeneficiaries) {
  			if (beneficiaryUserName.equals(b.getUsername())){
  				exception = false;
  			}
  		}

  		if(exception==true) {
  			throw new UsernameNotFoundException("The username of the beneficiary doesn't exist!");

  		}
		
		List<WorkExperienceDTO> workExperiencesDTO = new ArrayList<>();
		
		if (!workExperiences.isPresent()) {
			throw new UsernameNotFoundException("Work Experience not found!");
		} else {
			if(rol == RolAccount.ONG) {
				if(workExperiences.get().size() == 0) {
	 				throw new UsernameNotFoundException("This beneficiary has not work experiences!");
	 			}else {
	 				if(workExperiences.get().get(0).getBeneficiary().getOng().getUsername().equals(username)) {
						for (WorkExperience workExperience : workExperiences.get()) {
							WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO(workExperience);
							workExperiencesDTO.add(workExperienceDTO);
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
	 			}
			}else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}
		return workExperiencesDTO;
	}

	
	/*
     * Save a work experience
     * @Params String token
     * @Params WorkExperienceDTO workExperienceDTO
     * @Return WorkExperienceDTO
     */
    @Override
    public WorkExperienceDTO saveWorkExperience(WorkExperienceDTO workExperienceDTO, String username) {

    	WorkExperience workExperience = new WorkExperience(workExperienceDTO);
    	workExperience.setId(Long.valueOf(0));
        List<Beneficiary> beneficiaries = beneficiaryRepository.findAll();
        List<Volunteer> volunteers = volunteerRepository.findAll();
        Beneficiary beneficiaryAux = new Beneficiary();
        Volunteer volunteerAux = new Volunteer();
        for (Beneficiary beneficiary: beneficiaries) {
            if(beneficiary.getUsername().equals(username)) {
            	beneficiaryAux = beneficiary;
            	workExperience.setBeneficiary(beneficiary);
            }
        }

        for (Volunteer volunteer: volunteers) {
            if(volunteer.getUsername().equals(username)) {
            	volunteerAux = volunteer;
            	workExperience.setVolunteer(volunteer);
            }
        }

        logger.info("Work Experience saved associated with {}", username);
        try {
        	if(volunteerAux.getUsername() != null || beneficiaryAux.getUsername() != null) {
        		WorkExperience workExperienceSaved = workExperienceRepository.save(workExperience);
                return new WorkExperienceDTO(workExperienceSaved);
        	}else {
        		throw new UsernameNotFoundException("The username doesn't exist");
        	}
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    
    /*
     * Update a work experience
     * @Params String token
     * @Params WorkExperienceDTO workExperienceDTO
     * @Return WorkExperienceDTO
     */
	@Override
	public WorkExperienceDTO updateWorkExperience(String token, WorkExperienceDTO workExperienceDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<WorkExperience> workExperience = workExperienceRepository.findById(workExperienceDTO.getId());
		logger.info("Work Experience is updating with id={}", workExperienceDTO.getId());
		
		if (workExperience.isPresent()) {
			if(rol == RolAccount.VOLUNTEER) {
				if(workExperience.get().getVolunteer().getUsername().equals(username)) {
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
					if(workExperience.get().getBeneficiary().getOng().getUsername().equals(username)) {
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
					if(workExperience.get().getVolunteer().getOng().getUsername().equals(username)) {
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
			throw new UsernameNotFoundException("Work Experience not exist!");
		}
	}

	
	/*
     * Delete a work experience
     * @Params String token
     * @Params Long id
     * @Return void
     */
	@Override
	public void deleteWorkExperience(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<WorkExperience> workExperience = workExperienceRepository.findById(id);
		
		if (workExperience.isPresent()) {
			if(rol == RolAccount.VOLUNTEER) {
				if(workExperience.get().getVolunteer().getUsername().equals(username)) {
					workExperienceRepository.delete(workExperience.get());
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
			else{
				if(workExperience.get().getBeneficiary() != null) {
					if(workExperience.get().getBeneficiary().getOng().getUsername().equals(username)) {
						workExperienceRepository.delete(workExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(workExperience.get().getVolunteer() != null) {
					if(workExperience.get().getVolunteer().getOng().getUsername().equals(username)) {
						workExperienceRepository.delete(workExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}

		} else {
			throw new UsernameNotFoundException("Work Experience not exist!");
		}
	}
	
}
