package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AcademicExperienceDTO;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AcademicExperienceRepository;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.AcademicExperienceService;

@Service
public class AcademicExperienceServiceImpl implements AcademicExperienceService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private AcademicExperienceRepository academicExperienceRepository;
	private VolunteerRepository volunteerRepository;
	private BeneficiaryRepository beneficiaryRepository;
	private ONGRepository ongRepository;
	private AccountRepository accountRepository;
	private JwtUtils jwtUtils;
	
	public AcademicExperienceServiceImpl(AcademicExperienceRepository academicExperienceRepository,
			VolunteerRepository volunteerRepository, BeneficiaryRepository beneficiaryRepository,
			JwtUtils jwtUtils, AccountRepository accountRepository, ONGRepository ongRepository) {
		super();
		this.academicExperienceRepository = academicExperienceRepository;
		this.volunteerRepository = volunteerRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.ongRepository = ongRepository;
		this.jwtUtils = jwtUtils;
		this.accountRepository = accountRepository;
	}
	
	/*
	 * Get academic experience by volunteer
	 * 
	 * @Param Long id
	 * @Param String token
	 * 
	 * @Return AcademicExperienceDTO
	 */
	@Override
	public AcademicExperienceDTO getAcademicExpByID(Long id, String token) throws OperationNotAllowedException{
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
        RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
        Volunteer volunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong ong = ongRepository.findByUsername(username);
		
        if(ong!=null || volunteer!=null) {
			Optional<AcademicExperience> academicExperience = academicExperienceRepository.findById(id);
			if (!academicExperience.isPresent()) {
				throw new UsernameNotFoundException("Academic Experience not exist!");
			} else {
				if(rol == RolAccount.VOLUNTEER) {
					if(academicExperience.get().getBeneficiary() == null && academicExperience.get().getVolunteer().getId()==volunteer.getId()) {
						return new AcademicExperienceDTO(academicExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else{
					if(academicExperience.get().getBeneficiary() != null) {
						if(academicExperience.get().getBeneficiary().getOng().getId()==ong.getId()) {
							return new AcademicExperienceDTO(academicExperience.get());
						}else {
							throw new UsernameNotFoundException("You don't have access!");
						}
					}
					else if(academicExperience.get().getVolunteer() != null) {
						if(academicExperience.get().getVolunteer().getOng().getId()==ong.getId()) {
							return new AcademicExperienceDTO(academicExperience.get());
						}else {
							throw new UsernameNotFoundException("Academic Experience not exist!");
						}
					}else {
						throw new UsernameNotFoundException("Academic Experience not exist!");
					}
				}
			}
		}else{
			throw new OperationNotAllowedException("You must be a volunteer or an ONG to use this method.");
		}
	}
	
	/*
	 * Get academic experience by volunteer
	 * 
	 * @Param Long volunteerId
	 * @Param String token
	 * 
	 * @Return List<AcademicExperienceDTO>
	 */
	@Override
    public List<AcademicExperienceDTO> getAcademicExperienceByVolunteer(Long volunteerId, String token) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Volunteer loggedVolunteer = volunteerRepository.findVolunteerByUsername(username);
		Ong loggedOng = ongRepository.findByUsername(username);
		
		Optional<List<AcademicExperience>> academicExperiences = academicExperienceRepository.findAcademicExperienceByVolunteerId(volunteerId);
  		
  		Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);
  		
  		List<AcademicExperienceDTO> academicExperiencesDTO = new ArrayList<>();
  		
  		if(loggedOng != null || loggedVolunteer != null) {
  	  		if(volunteer.isPresent()){
  	  			if(academicExperiences.isPresent()) {
  	  				if(loggedOng != null) {
  	  					if(volunteer.get().getOng().equals(loggedOng)) {
	  	  					for (AcademicExperience academicExperience : academicExperiences.get()) {
	  	  						AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
								academicExperiencesDTO.add(academicExperienceDTO);
	  	  					}
  	  					}else {
  	  						throw new UsernameNotFoundException("This volunteer does not belong to your ONG!");
  	  					}
  	  				}else if(loggedVolunteer != null) {
  	  					if(loggedVolunteer.equals(volunteer.get())) {
	  	  					for (AcademicExperience academicExperience : academicExperiences.get()) {
	  	  						AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
								academicExperiencesDTO.add(academicExperienceDTO);
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
  		
  		return academicExperiencesDTO;		
    }
	
	/*
	 * Get academic experience by beneficiary
	 * 
	 * @Param Long beneficiaryId
	 * @Param String token
	 * 
	 * @Return List<AcademicExperienceDTO>
	 */
	@Override
	public List<AcademicExperienceDTO> getAcademicExperienceByBeneficiary(Long beneficiaryId, String token) throws OperationNotAllowedException{
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		
		Optional<List<AcademicExperience>> academicExperiences = academicExperienceRepository.findAcademicExperienceByBeneficiaryId(beneficiaryId);
  		
  		Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(beneficiaryId);
  		
  		List<AcademicExperienceDTO> academicExperiencesDTO = new ArrayList<>();
  		
  		if(ong != null) {
  	  		if(beneficiary.isPresent()){
  	  			if(academicExperiences.isPresent()) {
  					if(beneficiary.get().getOng().equals(ong)) {
  	  					for (AcademicExperience academicExperience : academicExperiences.get()) {
  	  						AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(academicExperience);
							academicExperiencesDTO.add(academicExperienceDTO);
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
  		
  		return academicExperiencesDTO;	
	}
	
	/*
	 * Save an academic experience
	 * 
	 *
	 * @Param String token
	 * @Param AcademicExperienceDTO
	 * @Param Long id
	 * 
	 * @Return AcademicExperienceDTO
	 */
	@Override
	public AcademicExperienceDTO saveAcademicExperience(AcademicExperienceDTO academicExperienceDTO,
			Long id, String token) throws OperationNotAllowedException {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
    	
    	AcademicExperience academicExperience = new AcademicExperience(academicExperienceDTO);
    	academicExperience.setId(Long.valueOf(0));
    	
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(id);
        Optional<Volunteer> volunteer = volunteerRepository.findById(id);
        
        if(ong!=null) {
            if(beneficiary.isPresent()) {
            	if(beneficiary.get().getOng().equals(ong)) {
            		logger.info("Academic Experience saved associated with id={}", id);
            		academicExperience.setBeneficiary(beneficiary.get());
	        		AcademicExperience academicExperienceSaved = academicExperienceRepository.save(academicExperience);
	                return new AcademicExperienceDTO(academicExperienceSaved);
            	}else{
            		throw new UsernameNotFoundException("You cannot edit information about a beneficiary who does not belong to your ONG!");
            	}
            	
            }else if(volunteer.isPresent()) {
            	if(volunteer.get().getOng().equals(ong)) {
            		academicExperience.setVolunteer(volunteer.get());
            		logger.info("Academic Experience saved associated with id={}", id);
            		AcademicExperience academicExperienceSaved = academicExperienceRepository.save(academicExperience);
	                return new AcademicExperienceDTO(academicExperienceSaved);
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
     * Update a academic experience
     * 
     * @Param String token
     * @Param Long id
     * @Param AcademicExperienceDTO
     * 
     * @Return AcademicExperienceDTO
     */
	@Override
	public AcademicExperienceDTO updateAcademicExperience(AcademicExperienceDTO academicExperienceDTO, String token, Long id) throws OperationNotAllowedException {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<AcademicExperience> academicExperience = academicExperienceRepository.findById(id);
		logger.info("Academic Experience is updating with id={}", id);
		
		if(ong!=null) {
			if (academicExperience.isPresent()) {
				if(academicExperience.get().getBeneficiary() != null) {
					if(academicExperience.get().getBeneficiary().getOng().getId() == ong.getId()) {
						academicExperience.get().setSpeciality(academicExperienceDTO.getSpeciality());
						academicExperience.get().setSatisfactionDegree(academicExperienceDTO.getSatisfactionDegree());
						academicExperience.get().setEducationalLevel(academicExperienceDTO.getEducationalLevel());
						academicExperience.get().setEndingYear(academicExperienceDTO.getEndingYear());

						try {
							AcademicExperience academicExperienceSaved = academicExperienceRepository.save(academicExperience.get());
							return new AcademicExperienceDTO(academicExperienceSaved);
						} catch (Exception e) {
							throw new UsernameNotFoundException(e.getMessage());
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(academicExperience.get().getVolunteer() != null) {
					if(academicExperience.get().getVolunteer().getOng().getId() == ong.getId()) {
						academicExperience.get().setSpeciality(academicExperienceDTO.getSpeciality());
						academicExperience.get().setSatisfactionDegree(academicExperienceDTO.getSatisfactionDegree());
						academicExperience.get().setEducationalLevel(academicExperienceDTO.getEducationalLevel());
						academicExperience.get().setEndingYear(academicExperienceDTO.getEndingYear());
						try {
							AcademicExperience academicExperienceSaved = academicExperienceRepository.save(academicExperience.get());
							return new AcademicExperienceDTO(academicExperienceSaved);
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
				throw new UsernameNotFoundException("Academic Experience not exist!");
			}
		}else {
  			throw new OperationNotAllowedException("You must be an ONG to use this method.");
  		}
	}
	
	/*
     * Delete a academic experience
     * 
     * @Params String token
     * @Params Long id
     * 
     * @Return void
     */
    @Override
    public void deleteAcademicExperience(Long id, String token) throws OperationNotAllowedException {
        String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
        Optional<AcademicExperience> academicExperience = academicExperienceRepository.findById(id);
        
        if(ong!=null) {
			if (academicExperience.isPresent()) {
				if(academicExperience.get().getBeneficiary() != null) {
					if(academicExperience.get().getBeneficiary().getOng().getId() == ong.getId()) {
						academicExperienceRepository.delete(academicExperience.get());
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(academicExperience.get().getVolunteer() != null) {
					if(academicExperience.get().getVolunteer().getOng().getId() == ong.getId()) {
						academicExperienceRepository.delete(academicExperience.get());
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
