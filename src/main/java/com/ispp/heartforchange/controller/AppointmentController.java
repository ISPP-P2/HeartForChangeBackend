package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ispp.heartforchange.dto.AppointmentDTO;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.AppointmentServiceImpl;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	private AppointmentServiceImpl appointmentService;
	private JwtUtils jwtUtils;
	
	/*
	 * Dependency injection
	 */
	public AppointmentController(AppointmentServiceImpl appointmentService, JwtUtils jwtUtils) {
		super();
		this.appointmentService = appointmentService;
		this.jwtUtils = jwtUtils;
	}
	
	
	
	/*
	 * Get appointment by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getAppointmentById(HttpServletRequest request, @PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			AppointmentDTO appointment = appointmentService.getAppointmentById(id, jwt);
			return ResponseEntity.ok(appointment);
		}catch(OperationNotAllowedException e) {
 			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
 		}catch(Exception e) {
 			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
 		}
		
	}
	

	/*
	 * Get beneficiary by appointment id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}/beneficiary")
	public ResponseEntity<?> getBeneficiaryByAppointment(HttpServletRequest request, @PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			BeneficiaryDTO beneficiary = appointmentService.getBeneficiaryByAppointmentId(id, jwt);
			return ResponseEntity.ok(beneficiary);
		}catch(OperationNotAllowedException e) {
 			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
 		}catch(Exception e) {
 			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
 		}
		
	}
	
	
	/*
     * Get all appointments by ong
     * 
     * @Param HttpServletRequest
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get")
    public ResponseEntity<?> getAppointmentByOng(HttpServletRequest request) throws OperationNotAllowedException {
        String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        try {
        	List<AppointmentDTO> appointments = appointmentService.getAppointmentsByONG(jwt);
            return ResponseEntity.ok(appointments);
        }
        catch(OperationNotAllowedException e) {
 			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
 		}catch(Exception e) {
 			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
 		}
    }
    
    /*
     * Get all appointments by beneficiary id
     * 
     * @Param HttpServletRequest
     * @Param Long id
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/beneficiary/{id}")
    public ResponseEntity<?> getAppointmentByBeneficiary(HttpServletRequest request,
            @PathVariable("id") Long id) throws OperationNotAllowedException {
        String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        try {
        	List<AppointmentDTO> appointments = appointmentService.getAppointmentsByBeneficiary(id, jwt);
            return ResponseEntity.ok(appointments);
        }
        catch(OperationNotAllowedException e) {
 			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
 		}catch(Exception e) {
 			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
 		}
    }
	
	
	/*
	* Save appointment
	* 
	* @Param AppointmentDTO appointmentDTO
	* @Param Long id 
	* 
	* @Return ResponseEntity
	*/
	@PostMapping("/save/{id}")
	  public ResponseEntity<?> saveAppointment(HttpServletRequest request, @Valid @RequestBody AppointmentDTO appointmentDTO, 
	          @PathVariable("id") Long id) throws OperationNotAllowedException {

		  String jwt = null;
	
		  String headerAuth = request.getHeader("Authorization");
	
		  if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
		      jwt = headerAuth.substring(7, headerAuth.length());
		  }
		  if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
		      return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		  }
	
		  try {
			  AppointmentDTO appointmentSaved = appointmentService.saveAppointment(jwt, appointmentDTO, id);
			  logger.info("Appointment saved associated with id={}", id);
			  return ResponseEntity.ok(appointmentSaved);
		  }
		  catch(OperationNotAllowedException e) {
			  return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		  }catch(Exception e) {
			  return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		  }
	  }
	
		
	/*
	 * Update appointment
	 * 
	 * @Param AppointmentDTO appointmentDTO
	 * @Param Long id 
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO, @PathVariable("id") Long id,
			HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			AppointmentDTO appointmentSaved = appointmentService.updateAppointment(jwt, appointmentDTO, id);
			logger.info("Appointment saved with id={}", id);
			return ResponseEntity.ok(appointmentSaved);
		}
		catch(OperationNotAllowedException e) {
 			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
 		}catch(Exception e) {
 			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
 		}
	}
	
	
	/*
	 * Delete appointment
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable("id") Long id, HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			appointmentService.deleteAppointment(id, jwt);
			return ResponseEntity.ok("Appointment deleted");
		}
		catch(OperationNotAllowedException e) {
 			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
 		}catch(Exception e) {
 			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
 		}
	}
	

}