package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.TaskServiceImpl;


@RestController
@RequestMapping("/tasks")
public class TaskController {

	private TaskServiceImpl taskService;
	private JwtUtils jwtUtils;

	/*
	 * Dependency injection
	 */
	public TaskController(TaskServiceImpl volunteerServiceImpl, JwtUtils jwtUtils) {
		super();
		this.taskService = volunteerServiceImpl;
		this.jwtUtils = jwtUtils;
	}
	
	
	

	
	

	
	/*
	 * Get all tasks by ong
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/ong/get")
	public ResponseEntity<?> getByOng(HttpServletRequest request){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		
			List<TaskDTO> tasks = taskService.getByOng(jwt);
			return ResponseEntity.ok(tasks);
	}
	
	
	
	@GetMapping("/beneficiary/get/{id}/attendances")
	public ResponseEntity<?> getAttendancesByPersonId(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getAllAttendancesByBeneficiary(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/count")
	public ResponseEntity<?> getNumberOfTasks(HttpServletRequest request) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			Integer count = taskService.getNumberOfTasks(jwt);
			return ResponseEntity.ok(count);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
