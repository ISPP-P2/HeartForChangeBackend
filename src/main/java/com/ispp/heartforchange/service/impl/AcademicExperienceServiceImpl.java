package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AcademicExperienceDTO;
import com.ispp.heartforchange.dto.GrantDTO;
import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.entity.AcademicExperience;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Grant;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Volunteer;
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
	private AccountRepository accountRepository;
	private ONGRepository ongRepository;
	private JwtUtils jwtUtils;
	
	public AcademicExperienceServiceImpl(AcademicExperienceRepository academicExperienceRepository,
			VolunteerRepository volunteerRepository, BeneficiaryRepository beneficiaryRepository,
			JwtUtils jwtUtils, AccountRepository accountRepository, ONGRepository ongRepository) {
		super();
		this.academicExperienceRepository = academicExperienceRepository;
		this.volunteerRepository = volunteerRepository;
		this.beneficiaryRepository = beneficiaryRepository;
		this.jwtUtils = jwtUtils;
		this.accountRepository = accountRepository;
		this.ongRepository = ongRepository;
	}
	
	/*
	 * Get all academic experiences
	 * @Return List<AcademicExperienceDTO>
	 */
	@Override
	public List<AcademicExperienceDTO> getAllAcademicExp() {
		List<AcademicExperience> academicExps = academicExperienceRepository.findAll();
		List<AcademicExperienceDTO> academicExpsDTOs = new ArrayList<>();
		for(AcademicExperience academicExp: academicExps) {
			AcademicExperienceDTO academicExperienceDTO = new AcademicExperienceDTO(
					academicExp.getId(),
					academicExp.getSpeciality(),
					academicExp.getEndingYear(),
					academicExp.getSatisfactionDegree(),
					academicExp.getEducationalLevel());
			academicExpsDTOs.add(academicExperienceDTO);
		}
		return academicExpsDTOs;
	}
	
	/*
	 * Get academic experience by volunteer
	 * 
	 * @Param Long id
	 * 
	 * @Return List<AcademicExperienceDTO>
	 */
	@Override
	public AcademicExperienceDTO getAcademicExpByID(Long id, String token) {
		
		String username = jwtUtils.getUserNameFromJwtToken(token);
        RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
        Optional<AcademicExperience> academicExperience = academicExperienceRepository.findById(id);

        if (!academicExperience.isPresent()) {
            throw new UsernameNotFoundException("Academy Experience not found!");
        } else {
            if(rol == RolAccount.VOLUNTEER) {
                if(academicExperience.get().getVolunteer().getUsername().equals(username)) {
                    return new AcademicExperienceDTO(academicExperience.get());
                }
                else {
                    throw new UsernameNotFoundException("You don't have access!");
                }
            }
            else{
                if(academicExperience.get().getBeneficiary() != null) {
                    if(academicExperience.get().getBeneficiary().getOng().getUsername().equals(username)) {
                        return new AcademicExperienceDTO(academicExperience.get());
                    }
                    else {
                        throw new UsernameNotFoundException("You don't have access!");
                    }
                }
                else if(academicExperience.get().getVolunteer() != null) {
                    if(academicExperience.get().getVolunteer().getOng().getUsername().equals(username)) {
                        return new AcademicExperienceDTO(academicExperience.get());
                    }
                    else {
                        throw new UsernameNotFoundException("You don't have access!");
                    }
                }
                else {
                    throw new UsernameNotFoundException("You don't have access!");
                }
            }
        }
	}
	
	/*
	 * Get academic experience by volunteer
	 * 
	 * @Param String volunteerUserName
	 * @Param String token
	 * 
	 * @Return List<AcademicExperienceDTO>
	 */
	@Override
    public List<AcademicExperienceDTO> getAcademicExperienceByVolunteerUsername(String volunteerUserName, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
        Optional<List<AcademicExperience>> academicExperience = academicExperienceRepository.findByVolunteer(volunteerUserName);
        List<AcademicExperienceDTO> res = new ArrayList<>();
        
        if (!academicExperience.isPresent()) {
            throw new UsernameNotFoundException("Academic Experience not found!");
        } else {
            if(rol == RolAccount.ONG) {
              
                if(academicExperience.get().get(0).getVolunteer().getOng().getUsername().equals(username)) {
                    for(AcademicExperience acaExp: academicExperience.get()) {
                    	AcademicExperienceDTO acadExpDTO = new AcademicExperienceDTO(acaExp);
                    	res.add(acadExpDTO);
                    }
                }else {
                    throw new UsernameNotFoundException("You don't have access!");
                }
            }
            else if(rol == RolAccount.VOLUNTEER) {
                if (username.equals(volunteerUserName)) {
                	for(AcademicExperience acaExp: academicExperience.get()) {
                    	AcademicExperienceDTO acadExpDTO = new AcademicExperienceDTO(acaExp);
                    	res.add(acadExpDTO);
                    }
                }else {
                    throw new UsernameNotFoundException("You don't have access!");
                }
            }else {
                throw new UsernameNotFoundException("You don't have access!");
            }
        }
        return res;
    }
	
	/*
	 * Get academic experience by beneficiary
	 * 
	 * @Param String beneficiaryUserName
	 * @Param String token
	 * 
	 * @Return List<AcademicExperienceDTO>
	 */
	@Override
    public List<AcademicExperienceDTO> getAcademicExperienceByBeneficiaryUsername(String beneficiaryUserName, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
        Optional<List<AcademicExperience>> academicExperience = academicExperienceRepository.findByBeneficiary(beneficiaryUserName);
        List<AcademicExperienceDTO> res = new ArrayList<>();
        
        if (!academicExperience.isPresent()) {
            throw new UsernameNotFoundException("Academic Experience not found!");
        } else {
            if(rol == RolAccount.ONG) {
              
                if(academicExperience.get().get(0).getBeneficiary().getOng().getUsername().equals(username)) {
                    for(AcademicExperience acaExp: academicExperience.get()) {
                    	AcademicExperienceDTO acadExpDTO = new AcademicExperienceDTO(acaExp);
                    	res.add(acadExpDTO);
                    }
                }else {
                    throw new UsernameNotFoundException("You don't have access!");
                }
            }
            else {
                throw new UsernameNotFoundException("You don't have access!");
            }
        }
        return res;
    }
	
	/*
	 * Save an academic experience
	 * @Params String token
	 * @Params AcademicExperienceDTO
	 * @Return AcademicExperienceDTO
	 */
	@Override
	public AcademicExperienceDTO saveAcademicExperience(AcademicExperienceDTO academicExperienceDTO,
			String username) {
		
		AcademicExperience acadExp = new AcademicExperience(academicExperienceDTO);
		acadExp.setId(Long.valueOf(0));
		List<Beneficiary> benfs = beneficiaryRepository.findAll();
		List<Volunteer> voluns = volunteerRepository.findAll();
		Beneficiary beneficiaryAux = new Beneficiary();
		Volunteer volunteerAux = new Volunteer();
		
		for (Beneficiary ben: benfs) {
			if(ben.getUsername().equals(username)) {
				beneficiaryAux=ben;
				acadExp.setBeneficiary(ben);
			}
		}
		
		for (Volunteer vol: voluns) {
			if(vol.getUsername().equals(username)) {
				volunteerAux=vol;
				acadExp.setVolunteer(vol);
			}
		}

		logger.info("Academic Experience saved associated with {}", username);
		try {
			if(volunteerAux.getUsername() !=null || beneficiaryAux.getUsername() != null) {
				AcademicExperience acdemicExpSaved = academicExperienceRepository.save(acadExp);
				System.out.println(voluns);
				System.out.println(benfs);
				System.out.println(acdemicExpSaved);
				return new AcademicExperienceDTO(acdemicExpSaved);
			}
			else {
				throw new UsernameNotFoundException("The username doesn´t exist");
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	/*
     * Update a academic experience
     * @Params String token
     * @Params AcademicExperienceDTO
     * @Return AcademicExperienceDTO
     */
	@Override
	public AcademicExperienceDTO updateAcademicExperience(AcademicExperienceDTO academicExperienceDTO, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
		Optional<AcademicExperience> academicExp = academicExperienceRepository.findById(academicExperienceDTO.getId());
		logger.info("Work Experience is updating with id={}", academicExperienceDTO.getId());
		
		if (academicExp.isPresent()) {
			if(rol == RolAccount.VOLUNTEER) {
				if(academicExp.get().getVolunteer().getUsername().equals(username)) {
					academicExp.get().setEducationalLevel(academicExperienceDTO.getEducationalLevel());;
					academicExp.get().setEndingYear(academicExperienceDTO.getEndingYear());;
					academicExp.get().setSatisfactionDegree(academicExperienceDTO.getSatisfactionDegree());;
					academicExp.get().setSpeciality(academicExperienceDTO.getSpeciality());;
					try {
						AcademicExperience AcademicExperienceSaved = academicExperienceRepository.save(academicExp.get());
						return new AcademicExperienceDTO(AcademicExperienceSaved);
					} catch (Exception e) {
						throw new UsernameNotFoundException(e.getMessage());
					}
				}else {
					throw new UsernameNotFoundException("You don't have access!");
				}
			}
			else{
				if(academicExp.get().getBeneficiary() != null) {
					if(academicExp.get().getBeneficiary().getOng().getUsername().equals(username)) {
						academicExp.get().setEducationalLevel(academicExperienceDTO.getEducationalLevel());
						academicExp.get().setEndingYear(academicExperienceDTO.getEndingYear());
						academicExp.get().setSatisfactionDegree(academicExperienceDTO.getSatisfactionDegree());
						academicExp.get().setSpeciality(academicExperienceDTO.getSpeciality());
						try {
							AcademicExperience workExperienceSaved = academicExperienceRepository.save(academicExp.get());
							return new AcademicExperienceDTO(workExperienceSaved);
						} catch (Exception e) {
							throw new UsernameNotFoundException(e.getMessage());
						}
					}else {
						throw new UsernameNotFoundException("You don't have access!");
					}
				}
				else if(academicExp.get().getVolunteer() != null) {
					if(academicExp.get().getVolunteer().getOng().getUsername().equals(username)) {
						academicExp.get().setEducationalLevel(academicExperienceDTO.getEducationalLevel());
						academicExp.get().setEndingYear(academicExperienceDTO.getEndingYear());
						academicExp.get().setSatisfactionDegree(academicExperienceDTO.getSatisfactionDegree());
						academicExp.get().setSpeciality(academicExperienceDTO.getSpeciality());
						try {
							AcademicExperience workExperienceSaved = academicExperienceRepository.save(academicExp.get());
							return new AcademicExperienceDTO(workExperienceSaved);
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
			throw new UsernameNotFoundException("Academic Experience does not exist!");
		}
	}
	
	/*
     * Delete a academic experience
     * @Params String token
     * @Params Long id
     * @Return void
     */
    @Override
    public void deleteAcademicExperience(Long id, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        RolAccount rol = accountRepository.findByUsername(username).getRolAccount();
        Optional<AcademicExperience> academicExperience = academicExperienceRepository.findById(id);

        if (academicExperience.isPresent()) {
            if(rol == RolAccount.VOLUNTEER) {
                if(academicExperience.get().getVolunteer().getUsername().equals(username)) {
                    academicExperienceRepository.delete(academicExperience.get());
                }else {
                    throw new UsernameNotFoundException("You don't have access!");
                }
            }
            else{
                if(academicExperience.get().getBeneficiary() != null) {
                    if(academicExperience.get().getBeneficiary().getOng().getUsername().equals(username)) {
                        academicExperienceRepository.delete(academicExperience.get());
                    }else {
                        throw new UsernameNotFoundException("You don't have access!");
                    }
                }
                else if(academicExperience.get().getVolunteer() != null) {
                    if(academicExperience.get().getVolunteer().getOng().getUsername().equals(username)) {
                        academicExperienceRepository.delete(academicExperience.get());
                    }else {
                        throw new UsernameNotFoundException("You don't have access!");
                    }
                }else {
                    throw new UsernameNotFoundException("You don't have access!");
                }
            }

        } else {
            throw new UsernameNotFoundException("Academic Experience not exist!");
        }
    }
	

}
