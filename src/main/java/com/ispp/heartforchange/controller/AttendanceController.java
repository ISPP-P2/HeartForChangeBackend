package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.entity.AttendanceType;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.AttendanceServiceImpl;


@Controller
@RequestMapping("/attendances")
public class AttendanceController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private AttendanceServiceImpl attendanceService;
	private JwtUtils jwtUtils;
	
	
	
	/*
	 * Dependency injection
	 */
	public AttendanceController(AttendanceServiceImpl attendanceService, JwtUtils jwtUtils) {
		super();
		this.attendanceService = attendanceService;
		this.jwtUtils = jwtUtils;
	}
	
		
	/*
	 * Get attendance by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getAttendanceById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendance = attendanceService.getAttendanceById(id, jwt);
		return ResponseEntity.ok(attendance);
	}
	
	/*
	 * Get all attendances by id task
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/get/task/{idTask}")
	public ResponseEntity<?> getAllAttendancesByTaskId(HttpServletRequest request, @PathVariable("idTask") Long idTask) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		List<AttendanceDTO> attendances = attendanceService.getAllAttendanceByIdTask(idTask, jwt);
		return ResponseEntity.ok(attendances);
	}
	
	/*
	 * Create new Petition by a Volunteer.
	 * 
	 * @Param HttpServletRequest
	 * @Param Long idTask
	 * 
	 * @Return ResponseEntity
	 */
		
	
	@PostMapping("/new/{idTask}")
	public ResponseEntity<?> createPetition(HttpServletRequest request, @PathVariable("idTask") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendance = attendanceService.createPetition(id, jwt);
		logger.info("Created Petition on Task with name={} by User with username={}");
		return ResponseEntity.ok(attendance);
	}
	
	/*
	 * Delete Petition by a Volunteer.
	 * 
	 * @Param HttpServletRequest
	 * @Param Long idAttendance
	 * 
	 * @Return ResponseEntity
	 */
	 
	@PostMapping("/cancel/{id}")
	public ResponseEntity<?> deletePetition(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO atendanceDTO = attendanceService.cancelPetition(id, jwt);
		return ResponseEntity.ok(atendanceDTO);
	}
	 
	 
	/*
	 * Accept Petition by a ONG.
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	
	@PutMapping("/accept/{id}")
	public ResponseEntity<?> acceptPetition(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendance = attendanceService.acceptPetition(id, jwt);
		return ResponseEntity.ok(attendance);
	}
	
	/*
	 * Deny Petition by a ONG.
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/deny/{id}")
	public ResponseEntity<?> denyPetition(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendance = attendanceService.denyPetition(id, jwt);
		return ResponseEntity.ok(attendance);
	}
	
	/*
	 * Confirm Attendance by a ONG.
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * @Param int type
	 * 
	 * @Return ResponseEntity
	 */
	
	
	@PutMapping("/confirm/{id}/{type}")
	public ResponseEntity<?> confirmAttendance(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("type") int type) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		if(!List.of(0,1,2).contains(type)) {
			return new ResponseEntity<String>("Type not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendance = attendanceService.confirmAttendance(jwt, AttendanceType.values()[type], id);
		return ResponseEntity.ok(attendance);
	}
	
	
	/*
	 * Add beneficiary to Attendance by a ONG.
	 * 
	 * @Param HttpServletRequest
	 * @Param Long idTask
	 * @Param Long idPerson
	 * 
	 * @Return ResponseEntity
	 */
	
	@PostMapping("/add/{idTask}/{idPerson}")
	public ResponseEntity<?> addBeneficiary(HttpServletRequest request,
			@PathVariable("idTask") Long idTask,@PathVariable("idPerson") Long idPerson) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendanceDTO = attendanceService.addBeneficiary(idTask, jwt, idPerson);
		return ResponseEntity.ok(attendanceDTO);
	}
	
	/*
	 * Delete beneficiary from Attendance by a ONG.
	 * 
	 * @Param HttpServletRequest
	 * @Param Long idTask
	 * @Param Long idPerson
	 * 
	 * @Return ResponseEntity
	 */
	
	@PostMapping("/quit/{idTask}/{idPerson}")
	public ResponseEntity<?> deleteBeneficiary(HttpServletRequest request,
			@PathVariable("idTask") Long idTask,@PathVariable("idPerson") Long idPerson) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		attendanceService.deleteBeneficiary(idTask, jwt, idPerson);
		return ResponseEntity.ok("Attendance Deleted");
	}

	@PostMapping("/quit/activity/{idTask}")
	public ResponseEntity<?> deleteAttendanceByAttendance(HttpServletRequest request,
											   @PathVariable("idTask") Long idTask) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");
		System.out.println("hola");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		System.out.println("hola");
		attendanceService.deleteAttendanceByVolunteer(idTask, jwt);
		return ResponseEntity.ok("Attendance Deleted");
	}
	
	
	
	
	
	
	
	

}
