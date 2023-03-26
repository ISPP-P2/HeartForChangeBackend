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
import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.TaskServiceImpl;


@RestController
@RequestMapping("/activities")
public class ActivityController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private TaskServiceImpl taskService;
	private JwtUtils jwtUtils;

	/*
	 * Dependency injection
	 */
	public ActivityController(TaskServiceImpl volunteerServiceImpl, JwtUtils jwtUtils) {
		super();
		this.taskService = volunteerServiceImpl;
		this.jwtUtils = jwtUtils;
	}
	
	
	/*
	 * Get activity by id
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
	 * Save activity
	 * 
	 * @Param TaskDTO
	 * 
	 * @Return ResponseEntity
	 */
	
	@PostMapping("/new")
	public ResponseEntity<?> save(HttpServletRequest request, @RequestBody TaskDTO task){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			TaskDTO taskSave = taskService.saveActivity(jwt,task);
			logger.info("Task saved with name=={}", taskSave.getName());
			return ResponseEntity.ok(taskSave);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	/*
	 * Get all activity by ong and volunteer
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/ong/get/all")
	public ResponseEntity<?> getByOng(HttpServletRequest request){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		
			List<TaskDTO> tasks = taskService.getActivityByOng(jwt);
			return ResponseEntity.ok(tasks);
	}




	/*
	 * Get all activity by ong and volunteer that are not finish
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/ong/get/date")
	public ResponseEntity<?> getByOngNotFinish(HttpServletRequest request){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		
			List<TaskDTO> tasks = taskService.getActivityByOngNotFinish(jwt);
			return ResponseEntity.ok(tasks);
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(HttpServletRequest request,@RequestBody TaskDTO task, @PathVariable("id") Long id){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		
		try {
			TaskDTO taskupdate = taskService.updateActivity(jwt, id, task);
			logger.info("Task update with name=={}", taskupdate.getName());
			return ResponseEntity.ok(taskupdate);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/*
	 * Delete Activity
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
			taskService.deleteActivity(jwt, id);
			return ResponseEntity.ok("Task deleted");
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Get accepted attendances in an Activity by ONG
	 * 
	 * @Param HttpServletRequest request
	 * @Param Long id
	 *
	 * @Return ResponseEntity<?>
	 */

	
	@GetMapping("/get/{id}/attendances/volunteer/accepted")
	public ResponseEntity<?> getAttendancesByIdAccepted(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<VolunteerDTO> attendances = taskService.getAllVoluntariesByTaskAccepted(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get/{id}/attendances/volunteer")
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
			List<VolunteerDTO> attendances = taskService.getAllVoluntariesByTask(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Get Attendances of a Volunteer by himself
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/volunteer/get/attendances")
	public ResponseEntity<?> getAttendancesByPersonId(HttpServletRequest request) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getAllAttendancesVolunteerByVolunteer(jwt);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	/*
	 * Get Attendances of a Volunteer by Ong
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	@GetMapping("/volunteer/get/{id}/attendances")
	public ResponseEntity<?> getAttendancesByPersonId(HttpServletRequest request, @PathVariable("id") Long id ) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getAllAttendancesOngByVolunteer(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/*
	 * Get petition of a task by Ong
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	
	@GetMapping("/get/{id}/petitions") 
	public ResponseEntity<?> getPetitionsById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getPetitionsByTask(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Get petition of a task by Ong
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	
	
	@GetMapping("/get/{id}/attendances/list") 
	public ResponseEntity<?> getAttendancesByTask(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getAttendancesByActivity(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/get/{id}/petition/state/") 
	public ResponseEntity<?> getStatePetitionByVolunteer(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			AttendanceDTO attendances = taskService.getPetitionStateByVolunteer(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/get/{idPerson}/{idTask}/petition/state/") 
	public ResponseEntity<?> getStatePetitionByONG(HttpServletRequest request, @PathVariable("idPerson") Long idPerson, @PathVariable("idTask") Long idTask) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			AttendanceDTO attendances = taskService.getPetitionStateByONG(jwt, idPerson, idTask);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
