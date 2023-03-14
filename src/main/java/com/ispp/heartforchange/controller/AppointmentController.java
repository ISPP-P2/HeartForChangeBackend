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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ispp.heartforchange.dto.AppointmentDTO;
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
	 * Get all appointments
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping
	public ResponseEntity<?> getAllAppointments() {
		List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
		return ResponseEntity.ok(appointments);
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
	public ResponseEntity<?> getAppointmentById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AppointmentDTO appointment = appointmentService.getAppointmentById(id, jwt);
		return ResponseEntity.ok(appointment);
	}
	
	
	/*
     * Get all appointments by ong username
     * 
     * @Param HttpServletRequest
     * @Param String username
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/ong/{username}")
    public ResponseEntity<?> getAppointmentByOngUsername(HttpServletRequest request,
            @PathVariable("username") String username) {
        String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByONG(username, jwt);
        return ResponseEntity.ok(appointments);
    }
	
	
	/*
	* Save appointment
	* 
	* @Param AppointmentDTO appointmentDTO
	* @Param String username 
	* 
	* @Return ResponseEntity
	*/
	@PostMapping("/save/{username}")
	  public ResponseEntity<?> saveAppointment(HttpServletRequest request, @Valid @RequestBody AppointmentDTO appointmentDTO, 
	          @PathVariable("username") String username) {

	  String jwt = null;

	  String headerAuth = request.getHeader("Authorization");

	  if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
	      jwt = headerAuth.substring(7, headerAuth.length());
	  }
	  if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
	      return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
	  }

	  AppointmentDTO appointmentSaved = appointmentService.saveAppointment(appointmentDTO, username);
	  logger.info("Appointment saved associated with {}", username);
	  return ResponseEntity.ok(appointmentSaved);
	  }
	
		
	/*
	 * Update appointment
	 * 
	 * @Param AppointmentDTO appointmentDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update")
	public ResponseEntity<?> updateAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO, HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AppointmentDTO appointmentSaved = appointmentService.updateAppointment(jwt, appointmentDTO);
		logger.info("Appointment saved with id={}", appointmentSaved.getId());
		return ResponseEntity.ok(appointmentSaved);
	}
	
	
	/*
	 * Delete appointment
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable("id") Long id, HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		appointmentService.deleteAppointment(id, jwt);
		return ResponseEntity.ok("Appointment deleted");
	}
	

}
