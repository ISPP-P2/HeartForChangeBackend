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
	 * Get all task
	 * 
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping
	public ResponseEntity<?> getAllTask(){
		List<TaskDTO> tasks = taskService.getAll();
		return ResponseEntity.ok(tasks);
	}
	
	
	/*
	 * Get task by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getTaskById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		TaskDTO task = taskService.getById(id, username);
		return ResponseEntity.ok(task);
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

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		
		TaskDTO taskSave = taskService.saveTask(task, username);
		logger.info("Task saved with name=={}", taskSave.getName());
		return ResponseEntity.ok(taskSave);
		
	}
	
	/*
	 * Get all task by ong
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("ong/{username}")
	public ResponseEntity<?> getByOng(@PathVariable("username") String username){
		List<TaskDTO> tasks = taskService.getByOng(username);
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

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		
		TaskDTO taskupdate = taskService.updateTask(id, task, username);
		logger.info("Task update with name=={}", taskupdate.getName());
		return ResponseEntity.ok(taskupdate);
		
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
		
		taskService.deleteTask(id, jwt);
		return ResponseEntity.ok("Task deleted");
	}
	
}
