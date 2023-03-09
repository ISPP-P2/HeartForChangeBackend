package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
@RequestMapping("/attendance")
public class AttendanceController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private AttendanceServiceImpl attendanceService;
	private JwtUtils jwtUtils;
	private AuthenticationManager authenticationManager;
	
	public AttendanceController(AttendanceServiceImpl attendanceService, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		super();
		this.attendanceService = attendanceService;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
	}
	
	
	@GetMapping
	public ResponseEntity<?> getAllAttendances(){
		List<AttendanceDTO> attendances = attendanceService.getAll();
		return ResponseEntity.ok(attendances);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAttendanceByIdOng(HttpServletRequest request, @PathVariable("id") Long id) {
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
	
	@GetMapping("volunteer/{id}")
	public ResponseEntity<?> getAttendanceByIdVolunteer(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendance = attendanceService.getAttendanceByIdVolunteer(id, jwt);
		return ResponseEntity.ok(attendance);
	}
	
	
	@PostMapping("/task/new/{id}")
	public ResponseEntity<?> createPetition(HttpServletRequest request, @PathVariable("id") Long id) {
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
	
	@PostMapping("/task/delete/{id}")
	public ResponseEntity<?> deletePetition(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		attendanceService.deletePetition(id, jwt);
		return ResponseEntity.ok("Attendance Deleted");
	}
	
	
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
	
	
	@PutMapping("confirm/{id}/{type}")
	public ResponseEntity<?> comfirmAttendance(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("type") int type) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		if(!List.of(1,2,3).contains(type)) {
			return new ResponseEntity<String>("Type not valid", HttpStatus.BAD_REQUEST);
		}
		
		AttendanceDTO attendance = attendanceService.confirmAttendance(jwt, AttendanceType.values()[type], id);
		return ResponseEntity.ok(attendance);
	}
	
	
	
	
	@PostMapping("/task/add/{idTask}/{idPerson}")
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
	
	@PostMapping("/task/quit/{idTask}/{idPerson}")
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
	
	
	
	
	
	
	
	

}
