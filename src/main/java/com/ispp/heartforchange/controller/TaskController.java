package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.TaskServiceImpl;


@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

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
	 * Get task by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getTaskById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			TaskDTO task = taskService.getById(jwt, id);
			return ResponseEntity.ok(task);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Save task
	 * 
	 * @Param TaskDTO
	 * 
	 * @Return ResponseEntity
	 */
	
	@PostMapping("/new")
	public ResponseEntity<?> save(HttpServletRequest request, @Valid @RequestBody TaskDTO task){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			TaskDTO taskSave = taskService.saveActivity(jwt,task); // Borrar
			logger.info("Task saved with name=={}", taskSave.getName());
			return ResponseEntity.ok(taskSave);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
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
	
	/*
	 * Update task
	 * 
	 * @Param TaskDTO
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(HttpServletRequest request, @Valid @RequestBody TaskDTO task, @PathVariable("id") Long id){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		
		try {
			TaskDTO taskupdate = taskService.updateActivity(jwt, id, task); // Borrar
			logger.info("Task update with name=={}", taskupdate.getName());
			return ResponseEntity.ok(taskupdate);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/*
	 * Delete task
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteGrant(@PathVariable("id") Long id, HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			taskService.deleteActivity(jwt, id); // Borrar
			return ResponseEntity.ok("Task deleted");
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@GetMapping("/get/{id}/attendances")
	public ResponseEntity<?> getAttendancesById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getAllAttendancesByTask(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/volunteer/get/{id}/attendances")
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
			List<AttendanceDTO> attendances = taskService.getAllAttendancesByVolunteer(jwt, id);
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
